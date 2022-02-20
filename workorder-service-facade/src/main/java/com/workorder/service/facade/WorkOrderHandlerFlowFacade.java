package com.workorder.service.facade;

import com.workorder.service.facade.dto.request.WorkOrderHandlerFlowReqDto;

/**
 * @author guo.li
 */
public interface WorkOrderHandlerFlowFacade {
    void handler(WorkOrderHandlerFlowReqDto req);
}
