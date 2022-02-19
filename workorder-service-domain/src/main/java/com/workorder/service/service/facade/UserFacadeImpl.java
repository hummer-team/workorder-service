package com.workorder.service.service.facade;

import com.hummer.common.exceptions.AppException;
import com.hummer.common.security.Aes;
import com.hummer.common.security.Md5;
import com.hummer.common.utils.ObjectCopyUtils;
import com.hummer.user.auth.plugin.context.UserContext;
import com.workorder.service.dao.UsersMapper;
import com.workorder.service.facade.UserFacade;
import com.workorder.service.facade.dto.request.UserCreatedReqDto;
import com.workorder.service.facade.dto.request.UserLoginReqDto;
import com.workorder.service.facade.dto.response.UserLoginRespDto;
import com.workorder.service.service.domain.RoleEnum;
import com.workorder.service.service.domain.core.UserEntity;
import com.workorder.service.support.model.po.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserFacadeImpl implements UserFacade {
    @Autowired
    private UsersMapper usersMapper;

    @Override
    public void createUser(UserCreatedReqDto req) {
        RoleEnum roleEnum = RoleEnum.getRoleByName(req.getRoleCode());
        req.setPassword(Md5.encryptMd5(Aes.encryptToStringByDefaultKeyIv(req.getPassword())));
        Users users = ObjectCopyUtils.copy(req, Users.class);
        users.setRoleCode(roleEnum.name());
        try {
            usersMapper.insert(users);
            log.info("user {} {} created success", req.getUserName(), req.getRoleCode());
        } catch (DuplicateKeyException e) {
            throw new AppException(40000, String.format("user %s already exists", req.getUserName()));
        }
    }

    @Override
    public UserLoginRespDto login(UserLoginReqDto req) {
        Users users = usersMapper.selectByUserName(req.getUsername());
        if (users == null) {
            throw new AppException(40004, "user or password invalid.");
        }
        String pwd = Md5.encryptMd5(Aes.encryptToStringByDefaultKeyIv(req.getPassword()));
        if (!pwd.equals(users.getPassword())) {
            throw new AppException(40004, "user or password invalid.");
        }

        RoleEnum roleEnum = RoleEnum.getRoleByName(users.getRoleCode());
        UserLoginRespDto respDto = new UserLoginRespDto();
        respDto.setUsername(users.getUserName());
        respDto.setUserId(users.getId());
        respDto.setRole(roleEnum.ordinal());
        respDto.setRoleCode(roleEnum.name());

        UserContext userContext = new UserEntity().saveContext(users);
        respDto.setToken(userContext.getToken());
        return respDto;
    }
}
