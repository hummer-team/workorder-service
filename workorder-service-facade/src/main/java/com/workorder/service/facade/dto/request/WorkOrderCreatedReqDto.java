package com.workorder.service.facade.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;

/**
 * @author guo.li
 */
@Data
public class WorkOrderCreatedReqDto {
    @NotEmpty(message = "work order title can't null")
    @Length(max = 500, message = "work order title max length is 500")
    private String title;
    @NotNull(message = "work order templateId can't null")
    private Integer templateId;
    @NotEmpty(message = "work order content can't null")
    private String content;
    /**
     * affix is Optional
     */
    private Map<String, byte[]> affixList;
    @NotNull(message = "expect completion time can't null")
    private Date expectDatetime;
    @NotEmpty(message = "environment type can't null")
    @ApiModelProperty(value = "PROD,UAT,SIT")
    private String environment;
}
