package com.workorder.service.facade.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * @author guo.li
 */
@Data
public class UserCreatedReqDto {
    @NotEmpty(message = "user name can't  null")
    private String userName;
    @NotEmpty(message = "pass word can't  null")
    @Length(min = 6, message = "password min length 6")
    private String password;
    @NotEmpty(message = "role code can't  null")
    private String roleCode;
}
