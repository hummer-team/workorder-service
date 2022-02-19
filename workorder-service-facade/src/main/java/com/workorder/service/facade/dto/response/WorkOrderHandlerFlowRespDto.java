package com.workorder.service.facade.dto.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class WorkOrderHandlerFlowRespDto {
    private Integer id;
    private Integer workOrderId;
    @JSONField(serialize = false)
    private Integer templateId;
    @JSONField(serialize = false)
    private Integer handlerUserId;
    private String handlerUser;
    private Integer status;
    private String statusDes;
    private Date createdDatetime;
    private Date lastModifiedDatetime;
    private String memo;
    private String handlerBatchId;
}
