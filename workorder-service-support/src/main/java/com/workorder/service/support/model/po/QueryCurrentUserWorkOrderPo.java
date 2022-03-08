package com.workorder.service.support.model.po;

import lombok.Data;

import java.util.Date;

@Data
public class QueryCurrentUserWorkOrderPo {
    private int status;
    private Date beginDate;
    private Date endDate;
    private Integer userId;
    private Integer type;

    private Integer templateId;
    private String workOrderName;
}
