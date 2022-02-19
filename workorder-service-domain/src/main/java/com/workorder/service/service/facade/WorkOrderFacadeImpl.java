package com.workorder.service.service.facade;

import com.google.common.base.Splitter;
import com.hummer.common.exceptions.AppException;
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
import com.workorder.service.facade.dto.request.WorkOrderCreatedReqDto;
import com.workorder.service.facade.dto.request.WorkOrderEditReqDto;
import com.workorder.service.facade.dto.response.WorkOrderHandlerFlowRespDto;
import com.workorder.service.facade.dto.response.WorkOrderRespDto;
import com.workorder.service.service.domain.OpEnum;
import com.workorder.service.service.domain.RoleEnum;
import com.workorder.service.service.domain.WorkOrderStatusEnum;
import com.workorder.service.service.domain.core.UserEntity;
import com.workorder.service.service.domain.core.UserRoleEntity;
import com.workorder.service.service.domain.service.WorkerOrderService;
import com.workorder.service.support.model.po.QueryCurrentUserWorkOrderPo;
import com.workorder.service.support.model.po.Template;
import com.workorder.service.support.model.po.Users;
import com.workorder.service.support.model.po.WorkOrder;
import com.workorder.service.support.model.po.WorkOrderHandlerFlow;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WorkOrderFacadeImpl implements WorkOrderFacade {
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
        UserContext userContext = UserHolder.getNotNull();

        RoleEnum submitRoleEnum = getCurrentUserRole(userContext);
        checkIsAllowOp(req.getTitle(), userContext, submitRoleEnum, OpEnum.SUBMIT_WORK_ORDER);

        Template template = templateMapper.selectByPrimaryKey(req.getTemplateId());
        if (template == null || StringUtils.isEmpty(template.getApproveUserIds()) || template.getExecuteUserId() == null) {
            throw new AppException(40004, String.format("template %s not find or template config error,please check"
                    , req.getTemplateId()));
        }

        int approvalUserId = Integer.parseInt(Splitter.on(",").splitToList(template.getApproveUserIds()).get(0));
        Users approvalUsers = usersMapper.selectByPrimaryKey(approvalUserId);
        if (approvalUsers == null) {
            throw new AppException(40005, "approval user not config,submit order failed");
        }
        RoleEnum approvalRoleEnum = getUserRoleByRole(approvalUsers.getRoleCode());
        checkIsAllowOp(req.getTitle(), userContext, approvalRoleEnum, OpEnum.APPROVE_WORK_ORDER);

        WorkOrder workOrder = workerOrderService.submitWorkOrder(req, userContext, WorkOrderStatusEnum.WAIT_APPROVE, approvalUsers);
        log.info("user {} work order {} created success ", userContext.getUserName(), req.getTitle());
        return workOrder.getId();
    }

    @Override
    public void editWorkOrder(WorkOrderEditReqDto req) {
        UserContext userContext = UserHolder.getNotNull();
        RoleEnum roleEnum = getCurrentUserRole(userContext);
        checkIsAllowOp("", userContext, roleEnum, OpEnum.SUBMIT_WORK_ORDER);

        WorkOrder workOrder = new WorkOrder();
        workOrder.setContent(req.getContent());
        workOrder.setTitle(req.getTitle());
        workOrder.setTemplateId(req.getTemplateId());
        workOrder.setSubmitUserId(Integer.parseInt(userContext.getUserId()));
        workOrderMapper.updateWorkOrderContentById(workOrder);
    }


    @Override
    public WorkOrderRespDto querySubmitWorkOrderById(int id) {
        UserContext userContext = UserHolder.getNotNull();
        RoleEnum roleEnum = getCurrentUserRole(userContext);
        checkIsAllowOp("", userContext, roleEnum, OpEnum.VIEW_WORK_ORDER);

        Integer submitUserId = roleEnum != RoleEnum.ADMINISTRATOR ? null : Integer.parseInt(userContext.getUserId());
        WorkOrder workOrder = workOrderMapper.selectByPrimaryKey(id, submitUserId);

        ParameterAssertUtil.assertConditionTrue(workOrder != null
                , () -> new AppException(40004, "work order not find"));
        List<WorkOrderHandlerFlow> flowList = handlerFlowMapper.selectByWorkOrderId(id);
        WorkOrderRespDto resp = ObjectCopyUtils.copy(workOrder, WorkOrderRespDto.class);
        assert resp != null;
        Template template = templateMapper.selectByPrimaryKey(workOrder.getTemplateId());
        resp.setTemplateName(template.getName());
        resp.setProjectCode(template.getProjectCode());
        resp.setHandlerFlows(ObjectCopyUtils.copyByList(flowList, WorkOrderHandlerFlowRespDto.class));
        resp.getHandlerFlows().forEach(f -> f.setStatusDes(WorkOrderStatusEnum.getByCode(f.getStatus()).getDesc()));

        return resp;
    }


    @Override
    public ResourcePageRespDto<WorkOrderRespDto> queryCurrentUserWorkOrder(
            ResourcePageReqDto<QueryCurrentUserWorkOrderReqDto> req) {
        ParameterAssertUtil.assertConditionTrue(req.getPageNumber() - 1 >= 0
                , () -> new AppException(40000, "page number invalid"));

        UserContext userContext = UserHolder.getNotNull();
        RoleEnum roleEnum = getCurrentUserRole(userContext);
        checkIsAllowOp("view work order", userContext, roleEnum, OpEnum.VIEW_WORK_ORDER);

        QueryCurrentUserWorkOrderPo queryPo = ObjectCopyUtils.copy(req.getQueryObject(), QueryCurrentUserWorkOrderPo.class);
        queryPo.setUserId(Integer.parseInt(userContext.getUserId()));
        queryPo.setType(roleEnum.ordinal());
        log.info("user {} query condition {}", userContext.getUserName(), queryPo);
        int count = workOrderMapper.countWorkOrder(queryPo);
        if (count <= 0) {
            return ResourcePageRespDto.emptyPage();
        }
        List<WorkOrder> workOrders = workOrderMapper.queryPageList(queryPo
                , (req.getPageNumber() - 1) * req.getPageSize(), req.getPageSize());

        List<WorkOrderRespDto> respDtos = ObjectCopyUtils.copyByList(workOrders, WorkOrderRespDto.class);
        for (WorkOrderRespDto dto : respDtos) {
            List<WorkOrderHandlerFlow> flowList = workOrders.stream()
                    .filter(f -> f.getId().equals(dto.getId()))
                    .flatMap(p -> p.getFlowList().stream())
                    .collect(Collectors.toUnmodifiableList());
            dto.setHandlerFlows(ObjectCopyUtils.copyByList(flowList, WorkOrderHandlerFlowRespDto.class));
        }
        respDtos.forEach(w -> Optional.ofNullable(w.getHandlerFlows()).orElse(Collections.emptyList())
                .forEach(f -> f.setStatusDes(WorkOrderStatusEnum.getByCode(f.getStatus()).getDesc())));
        return ResourcePageRespDto.builderPage(req.getPageNumber(), req.getPageSize(), count, respDtos);
    }

    private RoleEnum getCurrentUserRole(UserContext userContext) {
        UserEntity userEntity = new UserEntity();
        return userEntity.getRoleByContext(userContext);
    }

    private RoleEnum getUserRoleByRole(String roleCode) {
        UserEntity userEntity = new UserEntity();
        return userEntity.getRoleByCode(roleCode);
    }

    private void checkIsAllowOp(String description, UserContext userContext, RoleEnum roleEnum, OpEnum opEnum) {
        //todo AOP
        UserRoleEntity roleEntity = new UserRoleEntity(roleEnum);
        if (!roleEntity.checkAllowOp(opEnum)) {
            log.warn("user {} {} {}, no permission", userContext.getUserName(), opEnum, description);
            throw new AppException(opEnum.getCode(), String.format("user %s not allow operation work order"
                    , userContext.getUserName()));
        }
    }
}
