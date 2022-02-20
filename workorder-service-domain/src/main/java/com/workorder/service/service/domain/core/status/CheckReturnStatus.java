package com.workorder.service.service.domain.core.status;

import com.hummer.common.exceptions.AppException;
import com.hummer.rest.utils.ParameterAssertUtil;
import com.workorder.service.service.domain.WorkOrderStatusEnum;
import com.workorder.service.support.model.po.Template;
import com.workorder.service.support.model.po.WorkOrder;

import java.util.List;

public class CheckReturnStatus implements CheckStatus {

    @Override
    public NextHandlerFlow checkAndReturnNext(WorkOrderStatusEnum currentStatus, WorkOrder workOrderPo
            , List<Integer> handlerUserIds, List<Integer> historyStatus, Template template) {
        ParameterAssertUtil.assertConditionTrue(!Boolean.TRUE.equals(workOrderPo.getIsCancel())
                , () -> new AppException(40050, "已取消工单，不能驳回"));

        ParameterAssertUtil.assertConditionFalse(currentStatus == WorkOrderStatusEnum.APPROVE_OK
                , () -> new AppException(40060, "已审批通过，不能驳回"));


        ParameterAssertUtil.assertConditionFalse(currentStatus == WorkOrderStatusEnum.EXECUTED
                , () -> new AppException(40070, "已行完成，不能驳回"));

        boolean end = historyStatus.stream().anyMatch(f -> WorkOrderStatusEnum.getByCode(f) == WorkOrderStatusEnum.EXECUTED);
        ParameterAssertUtil.assertConditionFalse(end
                , () -> new AppException(40070, "已行完成，不能驳回"));

        return NextHandlerFlow.builder().isEnd(true).build();
    }
}
