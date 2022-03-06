package com.workorder.service.api.controller;

import com.hummer.rest.model.ResourceResponse;
import com.hummer.user.auth.plugin.annotation.AuthChannelEnum;
import com.hummer.user.auth.plugin.annotation.UserAuthority;
import com.workorder.service.facade.TemplateFacade;
import com.workorder.service.facade.dto.request.TemplateCreatedReqDto;
import com.workorder.service.facade.dto.response.TemplateInfoRespDto;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TemplateController {
    @Autowired
    private TemplateFacade templateFacade;

    @GetMapping("/template/list")
    @UserAuthority(channel = AuthChannelEnum.LOCAL)
    public ResourceResponse<List<TemplateInfoRespDto>> queryTemplateList() {
        return ResourceResponse.ok(templateFacade.queryAllTemplates());
    }


    @GetMapping("/template/project-code/list")
    @UserAuthority(channel = AuthChannelEnum.LOCAL)
    public ResourceResponse<List<String>> queryAllProjects() {
        return ResourceResponse.ok(templateFacade.queryAllProjectCode());
    }

    @PostMapping("/template/new")
    @UserAuthority(channel = AuthChannelEnum.LOCAL, authorityCodes = {"ADD_TEMPLATE"})
    public ResourceResponse createTemplate(@RequestBody @Valid TemplateCreatedReqDto req) {
        templateFacade.createTemplate(req);
        return ResourceResponse.ok();
    }


}
