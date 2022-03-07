package com.workorder.service.dao;

import com.hummer.dao.annotation.DaoAnnotation;
import com.workorder.service.support.model.po.QueryCurrentUserWorkOrderPo;
import com.workorder.service.support.model.po.WorkOrder;
import com.workorder.service.support.model.po.WorkOrderPagePo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@DaoAnnotation
public interface WorkOrderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table work_order
     *
     * @mbg.generated
     */
    int insert(WorkOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table work_order
     *
     * @mbg.generated
     */
    int insertSelective(WorkOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table work_order
     *
     * @mbg.generated
     */
    WorkOrder selectByPrimaryKey(Integer id, Integer submitUserId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table work_order
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(WorkOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table work_order
     *
     * @mbg.generated
     */
    int updateByPrimaryKeyWithWorkOrder(WorkOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table work_order
     *
     * @mbg.generated
     */
    int updateWorkOrderContentById(WorkOrder record);

    int countWorkOrderByOther(@Param("queryPo") QueryCurrentUserWorkOrderPo queryPo);

    int countWorkOrderByDevelop(@Param("queryPo") QueryCurrentUserWorkOrderPo queryPo);

    List<WorkOrderPagePo> queryPageListByDevelop(@Param("queryPo") QueryCurrentUserWorkOrderPo queryPo
            , @Param("index") int index, @Param("pageSize") int pageSize);

    List<WorkOrderPagePo> queryPageListByOther(@Param("queryPo") QueryCurrentUserWorkOrderPo queryPo
            , @Param("index") int index, @Param("pageSize") int pageSize);
    /**
     * cancel this worker order
     *
     * @param record worker entity
     * @return
     */
    int cancelWorkOrder(WorkOrder record);

    /**
     * update this worker order status
     *
     * @param id              Primary key
     * @param status          status
     * @param lastHandlerUser last processing work order user nam
     * @return
     */
    int updateWorkOrderStatus(int id, int status, String lastHandlerUser);
}