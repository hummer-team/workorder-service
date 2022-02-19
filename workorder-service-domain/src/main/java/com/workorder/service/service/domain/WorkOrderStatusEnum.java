package com.workorder.service.service.domain;

public enum WorkOrderStatusEnum {
    WAIT_APPROVE(1000, "待审批"),
    WAIT_EXECUTE(2000, "待执行"),
    EXECUTED(3000, "完成"),
    RETURNED(4000, "驳回"),
    CANCELED(5000, "已取消");

    private int code;
    private String desc;

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
