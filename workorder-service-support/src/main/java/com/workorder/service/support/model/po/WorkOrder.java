package com.workorder.service.support.model.po;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * this is %,auto generator
 *
 * @author guo.li
 */
public class WorkOrder implements Serializable {
    private Integer id;
    private String title;
    private Integer submitUserId;
    private String submitUser;
    private Integer status;
    private Integer templateId;
    private String templateName;
    private String projectCode;
    private Date createdDatetime;
    private Boolean isDelete;
    private String affixList;
    private String content;
    private String reason;
    private String reasonUserId;
    private Date reasonDatetime;
    private Boolean isCancel;


    public String getReason() {
        return reason;
    }

    public Boolean getIsCancel(){
        return isCancel;
    }

    public void setIsCancel(Boolean reason) {
        isCancel = reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReasonUserId() {
        return reasonUserId;
    }

    public void setReasonUserId(String reasonUserId) {
        this.reasonUserId = reasonUserId;
    }

    public Date getReasonDatetime() {
        return reasonDatetime;
    }

    public void setReasonDatetime(Date reasonDatetime) {
        this.reasonDatetime = reasonDatetime;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    private List<WorkOrderHandlerFlow> flowList;

    public List<WorkOrderHandlerFlow> getFlowList() {
        return flowList;
    }

    public void setFlowList(List<WorkOrderHandlerFlow> flowList) {
        this.flowList = flowList;
    }

    public String getSubmitUser() {
        return submitUser;
    }

    public void setSubmitUser(String submitUser) {
        this.submitUser = submitUser;
    }


    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table work_order
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column work_order.id
     *
     * @return the value of work_order.id
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column work_order.id
     *
     * @param id the value for work_order.id
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column work_order.title
     *
     * @return the value of work_order.title
     * @mbg.generated
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column work_order.title
     *
     * @param title the value for work_order.title
     * @mbg.generated
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column work_order.submit_user_id
     *
     * @return the value of work_order.submit_user_id
     * @mbg.generated
     */
    public Integer getSubmitUserId() {
        return submitUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column work_order.submit_user_id
     *
     * @param submitUserId the value for work_order.submit_user_id
     * @mbg.generated
     */
    public void setSubmitUserId(Integer submitUserId) {
        this.submitUserId = submitUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column work_order.status
     *
     * @return the value of work_order.status
     * @mbg.generated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column work_order.status
     *
     * @param status the value for work_order.status
     * @mbg.generated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column work_order.template_id
     *
     * @return the value of work_order.template_id
     * @mbg.generated
     */
    public Integer getTemplateId() {
        return templateId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column work_order.template_id
     *
     * @param templateId the value for work_order.template_id
     * @mbg.generated
     */
    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column work_order.created_datetime
     *
     * @return the value of work_order.created_datetime
     * @mbg.generated
     */
    public Date getCreatedDatetime() {
        return createdDatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column work_order.created_datetime
     *
     * @param createdDatetime the value for work_order.created_datetime
     * @mbg.generated
     */
    public void setCreatedDatetime(Date createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column work_order.is_delete
     *
     * @return the value of work_order.is_delete
     * @mbg.generated
     */
    public Boolean getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column work_order.is_delete
     *
     * @param isDelete the value for work_order.is_delete
     * @mbg.generated
     */
    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column work_order.affix_list
     *
     * @return the value of work_order.affix_list
     * @mbg.generated
     */
    public String getAffixList() {
        return affixList;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column work_order.affix_list
     *
     * @param affixList the value for work_order.affix_list
     * @mbg.generated
     */
    public void setAffixList(String affixList) {
        this.affixList = affixList;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column work_order.content
     *
     * @return the value of work_order.content
     * @mbg.generated
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column work_order.content
     *
     * @param content the value for work_order.content
     * @mbg.generated
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table work_order
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", title=").append(title);
        sb.append(", submitUserId=").append(submitUserId);
        sb.append(", status=").append(status);
        sb.append(", templateId=").append(templateId);
        sb.append(", createdDatetime=").append(createdDatetime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", affixList=").append(affixList);
        sb.append(", content=").append(content);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}