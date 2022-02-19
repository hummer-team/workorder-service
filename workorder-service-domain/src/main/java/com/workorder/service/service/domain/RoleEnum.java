package com.workorder.service.service.domain;

import com.hummer.common.exceptions.AppException;

/**
 * @author guo.li
 */

public enum RoleEnum {
    DEVELOP(0),
    APPROVE(1),
    EXECUTE(2),
    ADMINISTRATOR(3);

    private final int code;

    RoleEnum(int code) {
        this.code = code;
    }

    public static RoleEnum getRoleByName(String name) {

        for (RoleEnum roleEnum : RoleEnum.values()) {
            if (roleEnum.name().equalsIgnoreCase(name)) {
                return roleEnum;
            }
        }

        throw new AppException(40000, "role code invalid");
    }
}
