package com.workorder.service.service.domain.core.status;

import com.google.common.base.Splitter;
import com.hummer.common.exceptions.AppException;
import com.hummer.common.utils.DateUtil;
import com.hummer.rest.utils.ParameterAssertUtil;
import com.workorder.service.service.domain.WorkOrderStatusEnum;
import com.workorder.service.support.model.po.Template;
import com.workorder.service.support.model.po.WorkOrder;
import com.workorder.service.support.model.po.WorkOrderHandlerFlow;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class CheckApproveStatus implements CheckStatus {

    @Override
    public NextHandlerFlow checkAndReturnNext(WorkOrderStatusEnum currentStatus, WorkOrder workOrderPo
            , List<Integer> handlerUserIds
            , List<Integer> historyStatus
            , Template template) {

        ParameterAssertUtil.assertConditionFalse(workOrderPo.getStatus() == WorkOrderStatusEnum.CANCELED.getCode()
                , () -> new AppException(40000, "已取消工单，不能审批"));

        ParameterAssertUtil.assertConditionFalse(currentStatus == WorkOrderStatusEnum.RETURNED
                , () -> new AppException(40010, "已驳回工单，不能审批"));


        ParameterAssertUtil.assertConditionFalse(currentStatus == WorkOrderStatusEnum.EXECUTED
                , () -> new AppException(40020, "已行完成，不能审批"));

        ParameterAssertUtil.assertConditionFalse(currentStatus == WorkOrderStatusEnum.APPROVE_OK
                , () -> new AppException(40030, "已通过工单，不能重复审批"));

        List<String> nextUsers = Splitter.on(",").splitToList(template.getApproveUserIds());
        Optional<String> nextUserId = nextUsers.stream().filter(n -> !handlerUserIds.contains(Integer.parseInt(n)))
                .skip(0).limit(1).findFirst();
        WorkOrderHandlerFlow flow = new WorkOrderHandlerFlow();
        flow.setHandlerBatchId(DateUtil.formatNowDate(DateUtil.DateTimeFormat.F7.getValue()));
        if (nextUserId.isPresent()) {
            flow.setStatus(WorkOrderStatusEnum.WAIT_EXECUTE.getCode());
            flow.setActionType(WorkOrderStatusEnum.WAIT_EXECUTE.getCode());
            flow.setTemplateId(workOrderPo.getTemplateId());
            flow.setWorkOrderId(workOrderPo.getId());
            flow.setHandlerUserId(Integer.parseInt(nextUserId.get()));

        } else {
            flow.setStatus(WorkOrderStatusEnum.WAIT_EXECUTE.getCode());
            flow.setActionType(WorkOrderStatusEnum.WAIT_EXECUTE.getCode());
            flow.setTemplateId(workOrderPo.getTemplateId());
            flow.setWorkOrderId(workOrderPo.getId());
            flow.setHandlerUserId(template.getExecuteUserId());

        }
        log.info("work order {} , next handler user id is {},handler status {}", workOrderPo.getId(), flow.getHandlerUserId()
                , flow.getStatus());
        return NextHandlerFlow.builder().isEnd(false).next(flow).build();
    }
}
