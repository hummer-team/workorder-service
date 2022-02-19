package com.workorder.service.facade;

import com.hummer.rest.model.request.ResourcePageReqDto;
import com.hummer.rest.model.response.ResourcePageRespDto;
import com.workorder.service.facade.dto.request.QueryCurrentUserWorkOrderReqDto;
import com.workorder.service.facade.dto.request.WorkOrderCreatedReqDto;
import com.workorder.service.facade.dto.request.WorkOrderEditReqDto;
import com.workorder.service.facade.dto.response.WorkOrderRespDto;

public interface WorkOrderFacade {
    int createWorkOrder(WorkOrderCreatedReqDto req);

    void editWorkOrder(WorkOrderEditReqDto req);

    WorkOrderRespDto querySubmitWorkOrderById(int id);

    ResourcePageRespDto<WorkOrderRespDto> queryCurrentUserWorkOrder(ResourcePageReqDto<QueryCurrentUserWorkOrderReqDto> req);
}
