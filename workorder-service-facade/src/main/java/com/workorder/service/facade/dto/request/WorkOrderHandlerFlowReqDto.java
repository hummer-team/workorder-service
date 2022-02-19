package com.workorder.service.facade.dto.request;

import lombok.Data;

@Data
public class WorkOrderHandlerFlowReqDto {
    private Integer workOrderId;
    private Integer templateId;
    private Integer handlerUserId;
    private Integer status;
    private Integer actionType;
    private String memo;
    private String handlerBatchId;
}
