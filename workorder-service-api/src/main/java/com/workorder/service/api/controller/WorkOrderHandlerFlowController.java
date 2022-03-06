package com.workorder.service.api.controller;

import com.hummer.rest.model.ResourceResponse;
import com.hummer.user.auth.plugin.annotation.AuthChannelEnum;
import com.hummer.user.auth.plugin.annotation.PermissionAuthorityConditionEnum;
import com.hummer.user.auth.plugin.annotation.UserAuthority;
import com.workorder.service.facade.WorkOrderHandlerFlowFacade;
import com.workorder.service.facade.dto.request.WorkOrderHandlerFlowReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/v1")
public class WorkOrderHandlerFlowController {
    @Autowired
    private WorkOrderHandlerFlowFacade flowFacade;

    @PostMapping("/work-order/flow-handler")
    @UserAuthority(channel = AuthChannelEnum.LOCAL
            , authorityCodes = {"APPROVE_WORK_ORDER", "RETURNED", "EXECUTE_WORK_ORDER"}
            , authorityCondition = PermissionAuthorityConditionEnum.ANY_OF)
    public ResourceResponse handler(@RequestBody @Valid WorkOrderHandlerFlowReqDto req, Errors errors) {
        flowFacade.handler(req);
        return ResourceResponse.ok();
    }
}
