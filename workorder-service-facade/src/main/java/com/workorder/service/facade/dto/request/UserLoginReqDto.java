package com.workorder.service.facade.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
public class UserLoginReqDto {
    @NotEmpty(message = "user name can't  null")
    private String username;
    @NotEmpty(message = "pass word can't  null")
    @Length(min = 6, message = "password min length 6")
    private String password;
}
