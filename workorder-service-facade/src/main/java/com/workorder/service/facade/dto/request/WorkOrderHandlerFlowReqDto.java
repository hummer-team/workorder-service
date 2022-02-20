package com.workorder.service.facade.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author guo.li
 */
@Data
public class WorkOrderHandlerFlowReqDto {
    @NotNull(message = "work order can't null")
    @Min(value = 1, message = "work order value must greater than 1")
    private Integer workOrderId;
    @NotNull(message = "work order can't null")
    @ApiModelProperty(notes = "10010:审批通过;4000:驳回;3000:执行完成")
    private Integer status;
    private String memo;
    @NotNull(message = "handler flowId can't null")
    private Integer flowId;
}
