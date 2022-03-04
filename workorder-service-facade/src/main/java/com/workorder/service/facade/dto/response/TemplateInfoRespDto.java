package com.workorder.service.facade.dto.response;

import lombok.Data;

import java.util.Collection;
import java.util.Date;

/**
 * @author guo.li
 */
@Data
public class TemplateInfoRespDto {
    private Integer id;
    private String name;
    private String code;
    private String projectCode;
    private Date createdDatetime;
    private Collection<String> envList;
    private String describe;
}
