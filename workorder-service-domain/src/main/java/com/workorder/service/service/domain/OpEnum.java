package com.workorder.service.service.domain;

/**
 * @author guo.li
 */

public enum OpEnum {
    SUBMIT_WORK_ORDER(10000),
    APPROVE_WORK_ORDER(10020),
    EXECUTE_WORK_ORDER(10030),
    ADMIN_WORK_ORDER(10040),
    VIEW_WORK_ORDER(10050),
    CANCEL_WORK_ORDER(10060),
    ALL(100000);

    private int code;

    private OpEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static OpEnum getByCode(int code) {
        for (OpEnum opEnum : values()) {
            if (opEnum.code == code) {
                return opEnum;
            }
        }

        throw new IllegalArgumentException("invalid op enum code");
    }
}
