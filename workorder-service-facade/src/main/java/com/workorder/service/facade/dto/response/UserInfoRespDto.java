package com.workorder.service.facade.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class UserInfoRespDto {
    private String userName;
    private Integer id;
    private String roleCode;
    private Date createdDatetime;
}
