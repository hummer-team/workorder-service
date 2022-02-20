package com.workorder.service.facade.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class WorkOrderCancelReqDto {
    @NotNull(message = "work order id can't null")
    private Integer workOrderId;
    @NotNull(message = "reason can't null")
    @Length(min = 10, max = 200, message = "reason length rang is 10~200")
    private String reason;
}
