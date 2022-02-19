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
    ALL(100000);

    private int code;

    private OpEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
