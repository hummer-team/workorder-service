package com.workorder.service.service.domain.core;

import com.hummer.user.auth.plugin.context.UserContext;
import com.workorder.service.service.domain.RoleEnum;
import com.workorder.service.support.model.po.Users;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserEntity {

    public UserEntity() {

    }

    public UserContext saveContext(Users users) {
        UserContext userContext = new UserContext();
        userContext.setUserName(users.getUserName());
        userContext.setUserId(String.valueOf(users.getId()));
        Map<String, Object> map = new ConcurrentHashMap<>(1);
        map.put("role", users.getRoleCode());
        map.put("template", users.getTemplateIds());
        userContext.setData(map);
        userContext.saveContext(true);
        return userContext;
    }

    public RoleEnum getRoleByContext(UserContext context) {
        String val = (String) context.getData().get("role");
        return RoleEnum.getRoleByName(val);
    }

    public RoleEnum getRoleByCode(String roleCode) {
        return RoleEnum.getRoleByName(roleCode);
    }
}
