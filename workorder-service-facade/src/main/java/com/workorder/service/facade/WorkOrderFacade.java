package com.workorder.service.facade;

import com.hummer.rest.model.request.ResourcePageReqDto;
import com.hummer.rest.model.response.ResourcePageRespDto;
import com.workorder.service.facade.dto.request.QueryCurrentUserWorkOrderReqDto;
import com.workorder.service.facade.dto.request.WorkOrderCancelReqDto;
import com.workorder.service.facade.dto.request.WorkOrderCreatedReqDto;
import com.workorder.service.facade.dto.request.WorkOrderEditReqDto;
import com.workorder.service.facade.dto.response.WorkOrderRespDto;

public interface WorkOrderFacade {
    int createWorkOrder(WorkOrderCreatedReqDto req);

    void editWorkOrder(WorkOrderEditReqDto req);

    WorkOrderRespDto queryWorkOrderDetailsById(int id);

    ResourcePageRespDto<WorkOrderRespDto> queryCurrentUserWorkOrder(ResourcePageReqDto<QueryCurrentUserWorkOrderReqDto> req);

    ResourcePageRespDto<WorkOrderRespDto> queryCurrentUserWorkOrder2(ResourcePageReqDto<QueryCurrentUserWorkOrderReqDto> req);


    void cancel(WorkOrderCancelReqDto req);
}
