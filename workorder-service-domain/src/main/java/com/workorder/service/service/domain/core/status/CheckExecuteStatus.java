package com.workorder.service.service.domain.core.status;

import com.hummer.common.exceptions.AppException;
import com.hummer.rest.utils.ParameterAssertUtil;
import com.workorder.service.service.domain.WorkOrderStatusEnum;
import com.workorder.service.support.model.po.Template;
import com.workorder.service.support.model.po.WorkOrder;

import java.util.List;

public class CheckExecuteStatus implements CheckStatus {
    @Override
    public NextHandlerFlow checkAndReturnNext(WorkOrderStatusEnum currentStatus, WorkOrder workOrderPo
            , List<Integer> handlerUserIds, List<Integer> historyStatus, Template template) {
        ParameterAssertUtil.assertConditionFalse(workOrderPo.getStatus() == WorkOrderStatusEnum.CANCELED.getCode()
                , () -> new AppException(40030, "已取消工单，不能执行"));

        ParameterAssertUtil.assertConditionFalse(currentStatus != WorkOrderStatusEnum.WAIT_EXECUTE
                , () -> new AppException(40040, "未审核通过，不能执行"));

        return NextHandlerFlow.builder().isEnd(true).build();
    }
}
