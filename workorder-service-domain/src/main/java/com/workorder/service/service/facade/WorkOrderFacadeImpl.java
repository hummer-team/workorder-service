package com.workorder.service.service.facade;

import com.google.common.base.Splitter;
import com.hummer.common.exceptions.AppException;
import com.hummer.common.utils.DateUtil;
import com.hummer.common.utils.ObjectCopyUtils;
import com.hummer.rest.model.request.ResourcePageReqDto;
import com.hummer.rest.model.response.ResourcePageRespDto;
import com.hummer.rest.utils.ParameterAssertUtil;
import com.hummer.user.auth.plugin.context.UserContext;
import com.hummer.user.auth.plugin.holder.UserHolder;
import com.workorder.service.dao.TemplateMapper;
import com.workorder.service.dao.UsersMapper;
import com.workorder.service.dao.WorkOrderHandlerFlowMapper;
import com.workorder.service.dao.WorkOrderMapper;
import com.workorder.service.facade.WorkOrderFacade;
import com.workorder.service.facade.dto.request.QueryCurrentUserWorkOrderReqDto;
import com.workorder.service.facade.dto.request.WorkOrderCancelReqDto;
import com.workorder.service.facade.dto.request.WorkOrderCreatedReqDto;
import com.workorder.service.facade.dto.request.WorkOrderEditReqDto;
import com.workorder.service.facade.dto.response.WorkOrderHandlerFlowRespDto;
import com.workorder.service.facade.dto.response.WorkOrderRespDto;
import com.workorder.service.service.domain.RoleEnum;
import com.workorder.service.service.domain.WorkOrderStatusEnum;
import com.workorder.service.service.domain.service.WorkerOrderService;
import com.workorder.service.support.model.po.QueryCurrentUserWorkOrderPo;
import com.workorder.service.support.model.po.Template;
import com.workorder.service.support.model.po.Users;
import com.workorder.service.support.model.po.WorkOrder;
import com.workorder.service.support.model.po.WorkOrderHandlerFlow;
import com.workorder.service.support.model.po.WorkOrderPagePo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author guo.li
 */
@Service
@Slf4j
public class WorkOrderFacadeImpl extends BaseWorkOrderFacade implements WorkOrderFacade {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private TemplateMapper templateMapper;
    @Autowired
    private WorkOrderMapper workOrderMapper;
    @Autowired
    private WorkOrderHandlerFlowMapper handlerFlowMapper;
    @Autowired
    private WorkerOrderService workerOrderService;

    @Override
    public int createWorkOrder(WorkOrderCreatedReqDto req) {
        if (req.getExpectDatetime().before(DateUtil.now())) {
            throw new AppException(40005, "expect datetime must after now");
        }
        Template template = templateMapper.selectByPrimaryKey(req.getTemplateId());
        if (template == null || StringUtils.isEmpty(template.getApproveUserIds()) || template.getExecuteUserId() == null) {
            throw new AppException(40004, String.format("template %s not find or template config error,please check"
                    , req.getTemplateId()));
        }

        int approvalUserId = Integer.parseInt(Splitter.on(",").splitToList(template.getApproveUserIds()).get(0));
        Users approvalUsers = usersMapper.selectByPrimaryKey(approvalUserId);
        if (approvalUsers == null) {
            throw new AppException(40006, "approval user not config,submit order failed");
        }
        RoleEnum approvalRoleEnum = getUserRoleByRole(approvalUsers.getRoleCode());
        if (approvalRoleEnum != RoleEnum.APPROVE) {
            throw new AppException(40007, String.format("approval user %s No approval authority", approvalUsers.getUserName()));
        }

        UserContext userContext = UserHolder.getNotNull();

        WorkOrder workOrder = workerOrderService.submitWorkOrder(req, userContext, WorkOrderStatusEnum.WAIT_APPROVE, approvalUsers);
        log.info("user {} work order {} created success ", userContext.getUserName(), req.getTitle());
        return workOrder.getId();
    }

    @Override
    public void editWorkOrder(WorkOrderEditReqDto req) {
        UserContext userContext = UserHolder.getNotNull();
        //checkIsAllowOp(userContext, getCurrentUserRole(userContext), OpEnum.SUBMIT_WORK_ORDER);

        WorkOrder workOrder = new WorkOrder();
        workOrder.setId(req.getId());
        workOrder.setContent(req.getContent());
        workOrder.setTitle(req.getTitle());
        workOrder.setTemplateId(req.getTemplateId());
        workOrder.setSubmitUserId(Integer.parseInt(userContext.getUserId()));
        int result = workOrderMapper.updateWorkOrderContentById(workOrder);
        ParameterAssertUtil.assertConditionTrue(result > 0, () -> new AppException(40000
                , "edit work order failed"));
    }


    @Override
    public WorkOrderRespDto queryWorkOrderDetailsById(int id) {
        UserContext userContext = UserHolder.getNotNull();
        RoleEnum roleEnum = getCurrentUserRole(userContext);
        //checkIsAllowOp(userContext, roleEnum, OpEnum.VIEW_WORK_ORDER);

        Integer submitUserId = roleEnum == RoleEnum.DEVELOP ? Integer.parseInt(userContext.getUserId()) : null;
        WorkOrder workOrder = workOrderMapper.selectByPrimaryKey(id, submitUserId);

        ParameterAssertUtil.assertConditionTrue(workOrder != null
                , () -> new AppException(40004, "work order not find"));

        List<WorkOrderHandlerFlow> flowList = handlerFlowMapper.selectByWorkOrderId(id);
        WorkOrderRespDto resp = ObjectCopyUtils.copy(workOrder, WorkOrderRespDto.class);
        assert resp != null;

        Template template = templateMapper.selectByPrimaryKey(workOrder.getTemplateId());
        resp.setTemplateName(template.getName());
        resp.setProjectCode(template.getProjectCode());
        resp.setDescribe(template.getDescribe());
        resp.setHandlerFlows(ObjectCopyUtils.copyByList(flowList, WorkOrderHandlerFlowRespDto.class));
        resp.getHandlerFlows().forEach(f -> f.setStatusDes(WorkOrderStatusEnum.getByCode(f.getStatus()).getDesc()));

        return resp;
    }

    @Override
    public ResourcePageRespDto<WorkOrderRespDto> queryCurrentUserWorkOrder2(ResourcePageReqDto<QueryCurrentUserWorkOrderReqDto> req) {
        return null;
    }

    @Override
    public ResourcePageRespDto<WorkOrderRespDto> queryCurrentUserWorkOrder(
            ResourcePageReqDto<QueryCurrentUserWorkOrderReqDto> req) {
        ParameterAssertUtil.assertConditionTrue(req.getPageNumber() - 1 >= 0
                , () -> new AppException(40000, "page number invalid"));

        UserContext userContext = UserHolder.getNotNull();
        RoleEnum roleEnum = getCurrentUserRole(userContext);

        QueryCurrentUserWorkOrderPo queryPo = ObjectCopyUtils.copy(req.getQueryObject(), QueryCurrentUserWorkOrderPo.class);
        queryPo.setUserId(Integer.parseInt(userContext.getUserId()));
        queryPo.setType(roleEnum.ordinal());
        log.info("user {} query condition {}", userContext.getUserName(), queryPo);

        int count;
        List<WorkOrderPagePo> workOrders;
        if (roleEnum == RoleEnum.DEVELOP) {
            count = workOrderMapper.countWorkOrderByDevelop(queryPo);
            if (count <= 0) {
                return ResourcePageRespDto.emptyPage();
            }
            workOrders = workOrderMapper.queryPageListByDevelop(queryPo
                    , (req.getPageNumber() - 1) * req.getPageSize(), req.getPageSize());
        } else {
            count = workOrderMapper.countWorkOrderByOther(queryPo);
            if (count <= 0) {
                return ResourcePageRespDto.emptyPage();
            }
            workOrders = workOrderMapper.queryPageListByOther(queryPo
                    , (req.getPageNumber() - 1) * req.getPageSize(), req.getPageSize());
        }


        List<WorkOrderRespDto> respDtos = ObjectCopyUtils.copyByList(workOrders, WorkOrderRespDto.class);
        for (WorkOrderRespDto dto : respDtos) {
            dto.setHandlerFlows(Collections.emptyList());
        }
        respDtos.forEach(r -> r.setStatusDes(WorkOrderStatusEnum.getByCode(r.getStatus()).getDesc()));

        return ResourcePageRespDto.builderPage(req.getPageNumber(), req.getPageSize(), count, respDtos);
    }

    @Override
    public void cancel(WorkOrderCancelReqDto req) {
        UserContext userContext = UserHolder.getNotNull();
        RoleEnum roleEnum = getCurrentUserRole(userContext);

        Integer submitUserId = roleEnum == RoleEnum.ADMINISTRATOR ? null : Integer.parseInt(userContext.getUserId());
        WorkOrder workOrderPo = workOrderMapper.selectByPrimaryKey(req.getWorkOrderId(), submitUserId);
        ParameterAssertUtil.assertConditionTrue(workOrderPo != null
                , () -> new AppException(40004, String.format("work order %s not find", req.getWorkOrderId())));

        assert workOrderPo != null;
        if (workOrderPo.getStatus() == WorkOrderStatusEnum.CANCELED.getCode()) {
            throw new AppException(40005, "this work order already cancel.");
        }

        List<WorkOrderHandlerFlow> flowList = handlerFlowMapper.selectByWorkOrderId(req.getWorkOrderId());
        if (CollectionUtils.isEmpty(flowList)
                || flowList.stream().anyMatch(f ->
                WorkOrderStatusEnum.getByCode(f.getStatus()) == WorkOrderStatusEnum.WAIT_APPROVE
                        || WorkOrderStatusEnum.getByCode(f.getStatus()) == WorkOrderStatusEnum.WAIT_EXECUTE)
        ) {
            WorkOrder workOrder = new WorkOrder();
            workOrder.setId(req.getWorkOrderId());
            workOrder.setReason(req.getReason());
            workOrder.setStatus(WorkOrderStatusEnum.CANCELED.getCode());
            workOrder.setCancelUserId(Integer.parseInt(userContext.getUserId()));
            workOrderMapper.cancelWorkOrder(workOrder);
            log.info("user {} cancel work order {} reason {}", userContext.getUserName(), req.getWorkOrderId(), req.getReason());
            return;
        }


        throw new AppException(40006, "Only in the status of pending approval or rejected can it be cancelled");
    }
}
