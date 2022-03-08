package com.workorder.service.facade.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class QueryCurrentUserWorkOrderReqDto {
    @ApiModelProperty(notes = "value:0:全部,1000:待审批,2000:待执行,3000:完成,4000:驳回,5000:已取消")
    private int status;
    @NotNull(message = "begin date can't null")
    private Date beginDate;
    @NotNull(message = "end date can't null")
    private Date endDate;

    private String environment;
    private Integer templateId;
    private String workOrderName;
}
