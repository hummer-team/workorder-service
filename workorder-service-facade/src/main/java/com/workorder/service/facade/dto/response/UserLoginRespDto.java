package com.workorder.service.facade.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class UserLoginRespDto {
    private String username;
    private int userId;
    private String roleCode;
    private int role;
    private String token;
    private List<TemplateRespDto> templates;
}
