package com.workorder.service.service.domain.service;

import com.hummer.common.utils.DateUtil;
import com.hummer.common.utils.ObjectCopyUtils;
import com.hummer.dao.annotation.TargetDataSourceTM;
import com.hummer.user.auth.plugin.context.UserContext;
import com.workorder.service.dao.WorkOrderHandlerFlowMapper;
import com.workorder.service.dao.WorkOrderMapper;
import com.workorder.service.facade.dto.request.WorkOrderCreatedReqDto;
import com.workorder.service.service.domain.WorkOrderStatusEnum;
import com.workorder.service.support.model.po.Users;
import com.workorder.service.support.model.po.WorkOrder;
import com.workorder.service.support.model.po.WorkOrderHandlerFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkerOrderService {
    @Autowired
    private WorkOrderMapper workOrderMapper;
    @Autowired
    private WorkOrderHandlerFlowMapper handlerFlowMapper;

    @TargetDataSourceTM(timeout = 30, transactionManager = "workflow_TM", rollbackFor = Throwable.class
            , dbName = "workflow")
    public WorkOrder submitWorkOrder(WorkOrderCreatedReqDto req
            , UserContext userContext
            , WorkOrderStatusEnum status
            , Users approvedUsers) {
        //re factory
        WorkOrder workOrder = ObjectCopyUtils.copy(req, WorkOrder.class);
        workOrder.setSubmitUserId(Integer.parseInt(userContext.getUserId()));
        workOrder.setSubmitUser(userContext.getUserName());
        workOrder.setStatus(status.getCode());
        workOrder.setExpectDatetime(req.getExpectDatetime());
        workOrder.setEnvironment(req.getEnvironment());
        workOrderMapper.insert(workOrder);

        WorkOrderHandlerFlow flow = new WorkOrderHandlerFlow();
        flow.setWorkOrderId(workOrder.getId());

        flow.setStatus(status.getCode());
        flow.setHandlerUserId(approvedUsers.getId());
        flow.setActionType(status.getCode());
        flow.setHandlerUser(approvedUsers.getUserName());
        flow.setTemplateId(workOrder.getTemplateId());
        flow.setHandlerBatchId(DateUtil.formatNowDate(DateUtil.DateTimeFormat.F7.getValue()));
        handlerFlowMapper.insert(flow);
        return workOrder;
    }

    @TargetDataSourceTM(timeout = 30, transactionManager = "workflow_TM", rollbackFor = Throwable.class
            , dbName = "workflow")
    public void handlerStatus(WorkOrderHandlerFlow updateStatus, WorkOrderHandlerFlow nextStatus) {
        handlerFlowMapper.updateStatusByPrimaryKey(updateStatus);
        workOrderMapper.updateWorkOrderStatus(updateStatus.getWorkOrderId(), updateStatus.getStatus());
        if (nextStatus != null) {
            handlerFlowMapper.insert(nextStatus);
        }
    }
}
