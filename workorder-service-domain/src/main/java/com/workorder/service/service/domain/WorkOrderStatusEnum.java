package com.workorder.service.service.domain;

/**
 * @author guo.li
 */

public enum WorkOrderStatusEnum {
    WAIT_APPROVE(1000, "待审批"),
    RETURNED(1010, "驳回"),
    APPROVE_OK(1020, "审批通过"),
    WAIT_EXECUTE(2000, "待执行"),
    EXECUTED(3000, "执行完成"),
    CANCELED(5000, "已取消");

    private final int code;
    private final String desc;

    WorkOrderStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static WorkOrderStatusEnum getByCode(int code) {
        for (WorkOrderStatusEnum work : values()) {
            if (work.getCode() == code) {
                return work;
            }
        }

        throw new IllegalArgumentException("not find status enum");
    }
}
