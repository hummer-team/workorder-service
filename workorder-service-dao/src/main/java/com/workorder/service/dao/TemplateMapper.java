package com.workorder.service.dao;

import com.hummer.dao.annotation.DaoAnnotation;
import com.workorder.service.support.model.po.Template;

@DaoAnnotation
public interface TemplateMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table template
     *
     * @mbg.generated
     */
    int insert(Template record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table template
     *
     * @mbg.generated
     */
    int insertSelective(Template record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table template
     *
     * @mbg.generated
     */
    Template selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table template
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Template record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table template
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Template record);
}