package com.workorder.service.service.facade;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.hummer.common.exceptions.AppException;
import com.hummer.common.security.Aes;
import com.hummer.common.security.Md5;
import com.hummer.common.utils.ObjectCopyUtils;
import com.hummer.user.auth.plugin.context.UserContext;
import com.workorder.service.dao.TemplateMapper;
import com.workorder.service.dao.UsersMapper;
import com.workorder.service.facade.UserFacade;
import com.workorder.service.facade.dto.request.UserCreatedReqDto;
import com.workorder.service.facade.dto.request.UserLoginReqDto;
import com.workorder.service.facade.dto.response.TemplateRespDto;
import com.workorder.service.facade.dto.response.UserInfoRespDto;
import com.workorder.service.facade.dto.response.UserLoginRespDto;
import com.workorder.service.service.domain.RoleEnum;
import com.workorder.service.service.domain.core.UserEntity;
import com.workorder.service.support.model.po.Template;
import com.workorder.service.support.model.po.Users;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserFacadeImpl extends BaseWorkOrderFacade implements UserFacade {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private TemplateMapper templateMapper;

    @Override
    public void createUser(UserCreatedReqDto req) {
        //checkIsAllowOp(OpEnum.ADD_USER);

        RoleEnum roleEnum = RoleEnum.getRoleByName(req.getRoleCode());
        req.setPassword(Md5.encryptMd5(Aes.encryptToStringByDefaultKeyIv(req.getPassword())));
        Users users = ObjectCopyUtils.copy(req, Users.class);
        users.setRoleCode(roleEnum.name());
        if (roleEnum == RoleEnum.DEVELOP) {
            if (CollectionUtils.isEmpty(req.getTemplateIds())) {
                throw new AppException(40000, "user must spec work template");
            }
            users.setTemplateIds(Joiner.on(",").join(req.getTemplateIds()));
        }
        try {
            usersMapper.insert(users);
            log.info("user {} {} created success", req.getUserName(), req.getRoleCode());
        } catch (DuplicateKeyException e) {
            throw new AppException(40001, String.format("user %s already exists", req.getUserName()));
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
        respDto.setTemplates(Collections.emptyList());

        String templates = users.getTemplateIds();
        if (StringUtils.isNotEmpty(templates)) {
            List<Template> templateList = templateMapper.selectByPrimaryKeys(Collections2.transform(Splitter.on(",")
                    .splitToList(templates), Integer::parseInt));
            if (CollectionUtils.isNotEmpty(templateList)) {
                respDto.setTemplates(templateList.stream().map(t -> {
                    TemplateRespDto dto = new TemplateRespDto();
                    dto.setTemplateId(t.getId());
                    dto.setTemplateName(t.getName());
                    return dto;
                }).collect(Collectors.toList()));
            }
        }


        return respDto;
    }

    @Override
    public List<UserInfoRespDto> queryUserByRoles() {
        List<Users> users = usersMapper.selectUsersByRole(Lists.newArrayList(RoleEnum.APPROVE.name()
                , RoleEnum.DEVELOP.name(),RoleEnum.EXECUTE.name()));
        return ObjectCopyUtils.copyByList(users, UserInfoRespDto.class);
    }
}
