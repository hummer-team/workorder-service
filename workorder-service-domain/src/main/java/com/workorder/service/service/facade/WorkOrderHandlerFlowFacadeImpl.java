package com.workorder.service.service.facade;

import com.hummer.common.exceptions.AppException;
import com.hummer.rest.utils.ParameterAssertUtil;
import com.hummer.user.auth.plugin.context.UserContext;
import com.hummer.user.auth.plugin.holder.UserHolder;
import com.workorder.service.dao.TemplateMapper;
import com.workorder.service.dao.UsersMapper;
import com.workorder.service.dao.WorkOrderHandlerFlowMapper;
import com.workorder.service.dao.WorkOrderMapper;
import com.workorder.service.facade.WorkOrderHandlerFlowFacade;
import com.workorder.service.facade.dto.request.WorkOrderHandlerFlowReqDto;
import com.workorder.service.service.domain.OpEnum;
import com.workorder.service.service.domain.RoleEnum;
import com.workorder.service.service.domain.WorkOrderStatusEnum;
import com.workorder.service.service.domain.core.status.NextHandlerFlow;
import com.workorder.service.service.domain.core.status.WorkOrderStatus;
import com.workorder.service.service.domain.service.WorkerOrderService;
import com.workorder.service.support.model.po.Template;
import com.workorder.service.support.model.po.Users;
import com.workorder.service.support.model.po.WorkOrder;
import com.workorder.service.support.model.po.WorkOrderHandlerFlow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author guo.li
 */
@Service
@Slf4j
public class WorkOrderHandlerFlowFacadeImpl extends BaseWorkOrderFacade implements WorkOrderHandlerFlowFacade {
    @Autowired
    private WorkOrderHandlerFlowMapper flowMapper;
    @Autowired
    private WorkOrderMapper workOrderMapper;
    @Autowired
    private TemplateMapper templateMapper;
    @Autowired
    private WorkerOrderService workerOrderService;
    @Autowired
    private UsersMapper usersMapper;

    @Override
    public void handler(WorkOrderHandlerFlowReqDto req) {
        UserContext userContext = UserHolder.getNotNull();
        RoleEnum roleEnum = getCurrentUserRole(userContext);
        OpEnum opEnum = getOpEnum(req);
        checkIsAllowOp(userContext, roleEnum, opEnum);

        WorkOrder workOrderPo = workOrderMapper.selectByPrimaryKey(req.getWorkOrderId(), null);
        ParameterAssertUtil.assertConditionTrue(workOrderPo != null
                , () -> new AppException(40004, String.format("work order not find,can't %s", opEnum.name())));

        assert workOrderPo != null;
        List<WorkOrderHandlerFlow> flowList = flowMapper.selectByWorkOrderId(workOrderPo.getId());
        WorkOrderHandlerFlow currentUserHandlerWorkOrder = flowList.stream()
                .filter(f -> userMatch(req, userContext, f))
                .findFirst()
                .orElseThrow(() -> new AppException(40000, String.format("user %s there are no work orders to be processed"
                        , userContext.getUserName())));

        Template template = templateMapper.selectByPrimaryKey(workOrderPo.getTemplateId());
        WorkOrderStatusEnum statusEnum = WorkOrderStatusEnum.getByCode(req.getStatus());
        WorkOrderStatus status = new WorkOrderStatus(workOrderPo, flowList, template);
        NextHandlerFlow nextHandlerFlow = status.handler(statusEnum, req.getFlowId());

        log.info("work order current operation {} ,next handler {}", statusEnum.getDesc()
                , nextHandlerFlow.getNext());


        currentUserHandlerWorkOrder.setStatus(statusEnum.getCode());
        currentUserHandlerWorkOrder.setMemo(req.getMemo());
        currentUserHandlerWorkOrder.setActionType(statusEnum.getCode());

        if (!nextHandlerFlow.isEnd()) {
            Users usersPo = usersMapper.selectByPrimaryKey(nextHandlerFlow.getNext().getHandlerUserId());
            nextHandlerFlow.getNext().setHandlerUser(usersPo.getUserName());
            workerOrderService.handlerStatus(currentUserHandlerWorkOrder, nextHandlerFlow.getNext());
        } else {
            workerOrderService.handlerStatus(currentUserHandlerWorkOrder, null);
        }
        log.info("user {} update work order success, status {}", userContext.getUserName(), statusEnum);
    }

    private boolean userMatch(WorkOrderHandlerFlowReqDto req, UserContext userContext, WorkOrderHandlerFlow f) {
        return f.getHandlerUserId().equals(Integer.parseInt(userContext.getUserId()))
                && f.getId().equals(req.getFlowId());
    }

    private OpEnum getOpEnum(WorkOrderHandlerFlowReqDto req) {
        WorkOrderStatusEnum statusEnum = WorkOrderStatusEnum.getByCode(req.getStatus());
        switch (statusEnum) {
            case APPROVE_OK:
            case RETURNED:
                return OpEnum.APPROVE_WORK_ORDER;
            case EXECUTED:
                return OpEnum.EXECUTE_WORK_ORDER;
            default:
                throw new AppException(40000, "operation work order status invald");
        }
    }
}
