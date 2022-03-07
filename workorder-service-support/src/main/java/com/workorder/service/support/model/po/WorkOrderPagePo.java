package com.workorder.service.support.model.po;

import java.util.Date;

public class WorkOrderPagePo extends WorkOrder {
    private Date handlerDateTime;
    private String handlerUserName;
    private Integer lastNeedHandlerFlowId;

    public Integer getLastNeedHandlerFlowId() {
        return lastNeedHandlerFlowId;
    }

    public void setLastNeedHandlerFlowId(Integer lastNeedHandlerFlowId) {
        this.lastNeedHandlerFlowId = lastNeedHandlerFlowId;
    }

    public Date getHandlerDateTime() {
        return handlerDateTime;
    }

    public void setHandlerDateTime(Date handlerDateTime) {
        this.handlerDateTime = handlerDateTime;
    }

    public String getHandlerUserName() {
        return handlerUserName;
    }

    public void setHandlerUserName(String handlerUserName) {
        this.handlerUserName = handlerUserName;
    }
}
