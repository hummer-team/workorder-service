package com.workorder.service.api.controller;

import com.hummer.rest.model.ResourceResponse;
import com.hummer.user.auth.plugin.annotation.AuthChannelEnum;
import com.hummer.user.auth.plugin.annotation.UserAuthority;
import com.workorder.service.facade.RoleFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author guo.li
 */
@RestController
@RequestMapping(value = "/v1")
public class UserRoleController {
    @Autowired
    private RoleFacade roleFacade;

    @GetMapping("/role/list")
    @UserAuthority(channel = AuthChannelEnum.LOCAL)
    public ResourceResponse<List<String>> getRoleList() {
        return ResourceResponse.ok(roleFacade.allRoles());
    }
}
