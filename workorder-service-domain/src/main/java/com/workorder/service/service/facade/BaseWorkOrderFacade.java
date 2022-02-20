package com.workorder.service.service.facade;

import com.hummer.common.exceptions.AppException;
import com.hummer.user.auth.plugin.context.UserContext;
import com.workorder.service.service.domain.OpEnum;
import com.workorder.service.service.domain.RoleEnum;
import com.workorder.service.service.domain.core.UserEntity;
import com.workorder.service.service.domain.core.UserRoleEntity;
import lombok.extern.slf4j.Slf4j;

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

    public void checkIsAllowOp(UserContext userContext, RoleEnum roleEnum, OpEnum opEnum) {
        //todo AOP
        UserRoleEntity roleEntity = new UserRoleEntity(roleEnum);
        if (!roleEntity.checkAllowOp(opEnum)) {
            log.warn("user {} {}, no permission", userContext.getUserName(), opEnum);
            throw new AppException(opEnum.getCode(), String.format("user %s not allow operation work order %s"
                    , userContext.getUserName(), opEnum.name()));
        }
    }
}
