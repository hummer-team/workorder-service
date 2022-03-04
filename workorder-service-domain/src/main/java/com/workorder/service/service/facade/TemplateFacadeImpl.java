package com.workorder.service.service.facade;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.hummer.common.exceptions.AppException;
import com.hummer.common.utils.ObjectCopyUtils;
import com.hummer.user.auth.plugin.holder.UserHolder;
import com.workorder.service.dao.TemplateMapper;
import com.workorder.service.dao.UsersMapper;
import com.workorder.service.facade.TemplateFacade;
import com.workorder.service.facade.dto.request.TemplateCreatedReqDto;
import com.workorder.service.facade.dto.response.TemplateInfoRespDto;
import com.workorder.service.service.domain.OpEnum;
import com.workorder.service.service.domain.RoleEnum;
import com.workorder.service.support.model.po.Template;
import com.workorder.service.support.model.po.Users;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TemplateFacadeImpl extends BaseWorkOrderFacade implements TemplateFacade {
    @Autowired
    private TemplateMapper templateMapper;
    @Autowired
    private UsersMapper usersMapper;

    @Override
    public void createTemplate(TemplateCreatedReqDto req) {
        //checkIsAllowOp(OpEnum.ADD_TEMPLATE);
        List<Integer> localuIds = Lists.newCopyOnWriteArrayList(req.getApproveUserIds());
        localuIds.add(req.getExecuteUserId());
        List<Integer> userIds = localuIds.stream().distinct().collect(Collectors.toList());
        List<Users> usersList = usersMapper.selectUsersByIds(userIds);
        if (CollectionUtils.isEmpty(usersList)) {
            throw new AppException(40004, "user not find,can't add this template");
        }

        //check role
        for (Integer uId : req.getApproveUserIds()) {
            Optional<Users> users = usersList.stream().filter(p -> p.getId().equals(uId)).findFirst();
            if (users.isEmpty()) {
                throw new AppException(40000, "user %s invalid,can't add this template");
            }
            Users user = users.get();
            if (RoleEnum.valueOf(user.getRoleCode()) != RoleEnum.APPROVE) {
                throw new AppException(40001, String.format("user %s role is %s,can't add this template"
                        , user.getUserName(), user.getRoleCode()));
            }
        }

        Optional<Users> exUsers = usersList.stream().filter(p -> p.getId().equals(req.getExecuteUserId())).findFirst();
        if (exUsers.isEmpty()) {
            throw new AppException(40000, "user %s invalid,can't add this template");
        }
        Users user = exUsers.get();
        if (RoleEnum.valueOf(user.getRoleCode()) != RoleEnum.EXECUTE) {
            throw new AppException(40001, String.format("user %s role is %s,can't add this template"
                    , user.getUserName(), user.getRoleCode()));
        }
        String approveUserIds = Joiner.on(",").join(req.getApproveUserIds());
        Template template = new Template();
        template.setApproveUserIds(approveUserIds);
        template.setExecuteUserId(req.getExecuteUserId());
        template.setName(req.getName());
        template.setProjectCode(req.getProjectCode());
        template.setCreatedUserId(Integer.parseInt(UserHolder.getUserId()));
        try {
            templateMapper.insert(template);
            log.info("{} template {} created success", req.getName(), UserHolder.getUserName());
        } catch (DuplicateKeyException e) {
            throw new AppException(40006, String.format("this template %s already exists", req.getName()));
        }
    }

    @Override
    public List<TemplateInfoRespDto> queryAllTemplates() {
        List<Template> list = templateMapper.selectByPrimaryKeys(null);
        List<TemplateInfoRespDto> templates = ObjectCopyUtils.copyByList(list, TemplateInfoRespDto.class);
        templates.forEach(c -> c.setEnvList(Lists.newArrayList("PROD", "UAT", "SIT")));
        return templates;
    }

    @Override
    public List<String> queryAllProjectCode() {
        return Lists.newArrayList("Loreal", "MsStore");
    }
}
