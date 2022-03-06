package com.workorder.service.service.facade;

import com.hummer.common.exceptions.AppException;
import com.hummer.user.auth.plugin.context.UserContext;
import com.hummer.user.auth.plugin.holder.UserHolder;
import com.workorder.service.service.domain.OpEnum;
import com.workorder.service.service.domain.RoleEnum;
import com.workorder.service.service.domain.core.UserEntity;
import com.workorder.service.service.domain.core.UserRoleEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class BaseWorkOrderFacade {
    public RoleEnum getCurrentUserRole(UserContext userContext) {
        UserEntity userEntity = new UserEntity();
        return userEntity.getRoleByContext(userContext);
    }

    public RoleEnum getUserRoleByRole(String roleCode) {
        UserEntity userEntity = new UserEntity();
        return userEntity.getRoleByCode(roleCode);
    }

    public void checkIsAllowOp(OpEnum... opEnum) {
        UserContext userContext = UserHolder.getNotNull();
        RoleEnum currentUserRole = getCurrentUserRole(userContext);
        checkIsAllowOp(userContext, currentUserRole, opEnum);
    }

    public Map<OpEnum, Boolean> returnIsAllowOp(OpEnum... opEnum) {
        UserContext userContext = UserHolder.getNotNull();
        RoleEnum currentUserRole = getCurrentUserRole(userContext);
        return returnIsAllowOp(userContext, currentUserRole, opEnum);
    }

    public Map<OpEnum, Boolean> returnIsAllowOp(UserContext userContext, RoleEnum roleEnum, OpEnum... opEnum) {
        //todo AOP
        Map<OpEnum, Boolean> mapping = new ConcurrentHashMap<>();
        UserRoleEntity roleEntity = new UserRoleEntity(roleEnum);
        for (OpEnum op : opEnum) {
            boolean allow = roleEntity.checkAllowOp(op);
            mapping.put(op, allow);
            if (!allow) {
                log.warn("user {} {}, no permission", userContext.getUserName(), opEnum);
            }
        }
        return mapping;
    }

    public void checkIsAllowOp(UserContext userContext, RoleEnum roleEnum, OpEnum... opEnum) {
        //todo AOP
        UserRoleEntity roleEntity = new UserRoleEntity(roleEnum);
        for (OpEnum op : opEnum) {
            if (!roleEntity.checkAllowOp(op)) {
                log.warn("user {} {}, no permission", userContext.getUserName(), opEnum);
                throw new AppException(op.getCode(), String.format("user %s not allow operation work order %s"
                        , userContext.getUserName(), op.name()));
            }
        }
    }
}
