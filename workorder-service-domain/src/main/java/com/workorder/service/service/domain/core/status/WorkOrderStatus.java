package com.workorder.service.service.domain.core.status;

import com.workorder.service.service.domain.WorkOrderStatusEnum;
import com.workorder.service.support.model.po.Template;
import com.workorder.service.support.model.po.WorkOrder;
import com.workorder.service.support.model.po.WorkOrderHandlerFlow;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author guo.li
 */
public class WorkOrderStatus {
    private final WorkOrder workOrderPo;
    private final List<WorkOrderHandlerFlow> handlerFlow;
    private final Map<WorkOrderStatusEnum, CheckStatus> predicateMap;
    private final Template template;

    public WorkOrderStatus(WorkOrder workOrderPo, List<WorkOrderHandlerFlow> handlerFlow, Template template) {
        this.workOrderPo = workOrderPo;
        this.handlerFlow = handlerFlow;
        this.predicateMap = new ConcurrentHashMap<>();
        this.template = template;
        this.initNextHandler();
    }

    public NextHandlerFlow handler(WorkOrderStatusEnum opStatus, Integer currentFlowId) {
        WorkOrderHandlerFlow lastHandlerFlow = getLastHandlerFlow(currentFlowId);
        int currStatusId = lastHandlerFlow == null ? WorkOrderStatusEnum.WAIT_APPROVE.getCode() : lastHandlerFlow.getStatus();
        WorkOrderStatusEnum currentStatus = WorkOrderStatusEnum.getByCode(currStatusId);
        return predicateMap.get(opStatus).checkAndReturnNext(currentStatus, workOrderPo
                , getAlreadyHandlerUserIds(), getHistoryHandlerStatus(), template);
    }

    private WorkOrderHandlerFlow getLastHandlerFlow(Integer currentFlowId) {
        return handlerFlow
                .stream()
                .filter(f -> f.getId().equals(currentFlowId))
                .sorted(Comparator.comparing(WorkOrderHandlerFlow::getStatus).reversed())
                .limit(1)
                .findFirst()
                .orElseThrow();
    }

    private List<Integer> getAlreadyHandlerUserIds() {
        return handlerFlow.stream().map(WorkOrderHandlerFlow::getHandlerUserId).collect(Collectors.toList());
    }

    private List<Integer> getHistoryHandlerStatus() {
        return handlerFlow.stream().map(WorkOrderHandlerFlow::getStatus).collect(Collectors.toList());
    }

    private void initNextHandler() {
        predicateMap.put(WorkOrderStatusEnum.APPROVE_OK, new CheckApproveStatus());
        predicateMap.put(WorkOrderStatusEnum.RETURNED, new CheckReturnStatus());
        predicateMap.put(WorkOrderStatusEnum.EXECUTED, new CheckExecuteStatus());
    }
}
