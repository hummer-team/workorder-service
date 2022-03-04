package com.workorder.service.api.controller;

import com.hummer.rest.model.ResourceResponse;
import com.hummer.user.auth.plugin.annotation.AuthChannelEnum;
import com.hummer.user.auth.plugin.annotation.UserAuthority;
import com.workorder.service.facade.UserFacade;
import com.workorder.service.facade.dto.request.UserCreatedReqDto;
import com.workorder.service.facade.dto.request.UserLoginReqDto;
import com.workorder.service.facade.dto.response.UserInfoRespDto;
import com.workorder.service.facade.dto.response.UserLoginRespDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author guo.li
 */
@RestController
@RequestMapping(value = "/v1")
public class UserController {
    @Autowired
    private UserFacade userFacade;

    @PostMapping("/user/new")
    @UserAuthority(channel = AuthChannelEnum.LOCAL, authorityCodes = {"ADD_USER"})
    public ResourceResponse createUser(@RequestBody @Valid UserCreatedReqDto dto, Errors errors) {
        userFacade.createUser(dto);
        return ResourceResponse.ok();
    }

    @PostMapping("/user/login")
    public ResourceResponse<UserLoginRespDto> login(@RequestBody @Valid UserLoginReqDto dto, Errors errors) {
        return ResourceResponse.ok(userFacade.login(dto));
    }

    @GetMapping("/user/list")
    @UserAuthority(channel = AuthChannelEnum.LOCAL)
    public ResourceResponse<List<UserInfoRespDto>> queryAllUser() {
        return ResourceResponse.ok(userFacade.queryUserByRoles());
    }
}
