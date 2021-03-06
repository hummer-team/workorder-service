package com.workorder.service.api.controller;

import com.hummer.rest.model.ResourceResponse;
import com.hummer.rest.model.request.ResourcePageReqDto;
import com.hummer.rest.model.response.ResourcePageRespDto;
import com.hummer.user.auth.plugin.annotation.AuthChannelEnum;
import com.hummer.user.auth.plugin.annotation.UserAuthority;
import com.workorder.service.facade.WorkOrderFacade;
import com.workorder.service.facade.dto.request.QueryCurrentUserWorkOrderReqDto;
import com.workorder.service.facade.dto.request.WorkOrderCancelReqDto;
import com.workorder.service.facade.dto.request.WorkOrderCreatedReqDto;
import com.workorder.service.facade.dto.request.WorkOrderEditReqDto;
import com.workorder.service.facade.dto.response.WorkOrderRespDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/v1")
public class WorkOrderController {
    @Autowired
    private WorkOrderFacade workOrderFacade;

    @PostMapping("/work-order/new")
    @UserAuthority(channel = AuthChannelEnum.LOCAL, authorityCodes = {"SUBMIT_WORK_ORDER"})
    public ResourceResponse<Integer> createWorkOrder(@RequestBody @Valid WorkOrderCreatedReqDto req, Errors errors) {
        return ResourceResponse.ok(workOrderFacade.createWorkOrder(req));
    }

    @GetMapping("/work-order/details/{workOrderId}")
    @UserAuthority(channel = AuthChannelEnum.LOCAL, authorityCodes = {"VIEW_WORK_ORDER"})
    public ResourceResponse<WorkOrderRespDto> queryWorkOrderDetails(@PathVariable("workOrderId") Integer workOrderId) {
        return ResourceResponse.ok(workOrderFacade.queryWorkOrderDetailsById(workOrderId));
    }

    @PostMapping("/work-order/page-list")
    @UserAuthority(channel = AuthChannelEnum.LOCAL, authorityCodes = {"VIEW_WORK_ORDER"})
    public ResourceResponse<ResourcePageRespDto<WorkOrderRespDto>> queryWorkOrderPageList(
            @RequestBody @Valid ResourcePageReqDto<QueryCurrentUserWorkOrderReqDto> req, Errors errors) {
        return ResourceResponse.ok(workOrderFacade.queryCurrentUserWorkOrder(req));
    }

    @PostMapping("/work-order/edit")
    @UserAuthority(channel = AuthChannelEnum.LOCAL, authorityCodes = {"SUBMIT_WORK_ORDER"})
    public ResourceResponse editWorkOrder(@RequestBody @Valid WorkOrderEditReqDto req, Errors errors) {
        workOrderFacade.editWorkOrder(req);
        return ResourceResponse.ok();
    }

    @PostMapping("/work-order/cancel")
    @UserAuthority(channel = AuthChannelEnum.LOCAL, authorityCodes = {"CANCEL_WORK_ORDER"})
    public ResourceResponse cancel(@RequestBody @Valid WorkOrderCancelReqDto req, Errors errors) {
        workOrderFacade.cancel(req);
        return ResourceResponse.ok();
    }
}
