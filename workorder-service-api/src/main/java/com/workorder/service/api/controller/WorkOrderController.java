package com.workorder.service.api.controller;

import com.hummer.rest.model.ResourceResponse;
import com.hummer.rest.model.request.ResourcePageReqDto;
import com.hummer.rest.model.response.ResourcePageRespDto;
import com.hummer.user.auth.plugin.annotation.AuthChannelEnum;
import com.hummer.user.auth.plugin.annotation.UserAuthority;
import com.workorder.service.facade.WorkOrderFacade;
import com.workorder.service.facade.dto.request.QueryCurrentUserWorkOrderReqDto;
import com.workorder.service.facade.dto.request.WorkOrderCreatedReqDto;
import com.workorder.service.facade.dto.response.WorkOrderRespDto;
import org.springframework.beans.factory.annotation.Autowired;
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
    @UserAuthority(channel = AuthChannelEnum.LOCAL)
    public ResourceResponse<Integer> createWorkOrder(@RequestBody @Valid WorkOrderCreatedReqDto req) {
        return ResourceResponse.ok(workOrderFacade.createWorkOrder(req));
    }

    @GetMapping("/work-order/details/{workOrderId}")
    @UserAuthority(channel = AuthChannelEnum.LOCAL)
    public ResourceResponse<WorkOrderRespDto> queryWorkOrderDetails(@PathVariable("workOrderId") Integer workOrderId) {
        return ResourceResponse.ok(workOrderFacade.querySubmitWorkOrderById(workOrderId));
    }

    @PostMapping("/work-order/page-list")
    @UserAuthority(channel = AuthChannelEnum.LOCAL)
    public ResourceResponse<ResourcePageRespDto<WorkOrderRespDto>> queryWorkOrderPageList(
            @RequestBody @Valid ResourcePageReqDto<QueryCurrentUserWorkOrderReqDto> req) {
        return ResourceResponse.ok(workOrderFacade.queryCurrentUserWorkOrder(req));
    }
}
