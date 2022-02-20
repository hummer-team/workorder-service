package com.workorder.service.service.domain.core.status;

import com.workorder.service.service.domain.WorkOrderStatusEnum;
import com.workorder.service.support.model.po.Template;
import com.workorder.service.support.model.po.WorkOrder;

import java.util.List;

public interface CheckStatus {
    NextHandlerFlow checkAndReturnNext(WorkOrderStatusEnum currentStatus, WorkOrder workOrderPo
            , List<Integer> handlerUserIds
            , List<Integer> historyStatus
            , Template template);
}
