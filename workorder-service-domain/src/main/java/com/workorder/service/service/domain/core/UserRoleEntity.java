package com.workorder.service.service.domain.core;

import com.google.common.collect.Lists;
import com.workorder.service.service.domain.OpEnum;
import com.workorder.service.service.domain.RoleEnum;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserRoleEntity {
    private final RoleEnum roleEnum;
    private final Map<RoleEnum, List<OpEnum>> roleOpMap = new ConcurrentHashMap<>();

    {
        roleOpMap.put(RoleEnum.DEVELOP, Lists.newArrayList(OpEnum.SUBMIT_WORK_ORDER, OpEnum.VIEW_WORK_ORDER, OpEnum.CANCEL_WORK_ORDER));
        roleOpMap.put(RoleEnum.EXECUTE, Lists.newArrayList(OpEnum.EXECUTE_WORK_ORDER, OpEnum.VIEW_WORK_ORDER, OpEnum.SUBMIT_WORK_ORDER, OpEnum.RETURNED));
        roleOpMap.put(RoleEnum.APPROVE, Lists.newArrayList(OpEnum.APPROVE_WORK_ORDER, OpEnum.VIEW_WORK_ORDER, OpEnum.RETURNED));
        roleOpMap.put(RoleEnum.ADMINISTRATOR, Lists.newArrayList(OpEnum.VIEW_WORK_ORDER, OpEnum.ADD_USER, OpEnum.ADD_TEMPLATE));
    }

    public UserRoleEntity(RoleEnum roleEnum) {
        this.roleEnum = roleEnum;
    }

    public boolean checkAllowOp(OpEnum opName) {
        boolean allowAny = roleOpMap.get(roleEnum).stream().anyMatch(o -> o == OpEnum.ALL);
        if (allowAny) {
            return true;
        }

        return roleOpMap.get(roleEnum).stream().anyMatch(f -> f.ordinal() == opName.ordinal());
    }
}
