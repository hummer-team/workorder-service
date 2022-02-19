package com.workorder.service.facade.dto.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class WorkOrderRespDto {
    private Integer id;
    private String title;
    @JSONField(serialize = false)
    private Integer submitUserId;
    private String submitUser;
    private String submitUserName;
    private String templateName;
    private String projectCode;
    private Integer templateId;
    private Date createdDatetime;
    private String affixList;
    private String content;

    private List<WorkOrderHandlerFlowRespDto> handlerFlows;
}
