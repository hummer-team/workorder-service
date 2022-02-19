package com.workorder.service.facade.dto.request;

import lombok.Data;

import java.util.List;

/**
 * @author guo.li
 */
@Data
public class TemplateCreatedReqDto {
    private String name;
    private String code;
    private List<String> approveUserIds;
    private Integer executeUserId;
}
