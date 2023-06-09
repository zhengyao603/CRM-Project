package com.ssmproject.crm.workbench.mapper;

import com.ssmproject.crm.workbench.pojo.ActivityRemark;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Apr 16 14:16:40 CST 2023
     */
    int insertSelective(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Apr 16 14:16:40 CST 2023
     */
    ActivityRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Apr 16 14:16:40 CST 2023
     */
    int updateByPrimaryKeySelective(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Apr 16 14:16:40 CST 2023
     */
    int updateByPrimaryKey(ActivityRemark record);

    /**
     * 根据市场活动记录id查询备注
     * @param activityId
     * @return
     */
    List<ActivityRemark> selectActivityRemarkForDetailByActivityId(@Param("activityId") String activityId);

    /**
     * 插入新的市场活动备注
     * @param remark
     * @return
     */
    int insertActivityRemark(ActivityRemark remark);

    /**
     * 删除指定市场活动记录
     * @param id
     * @return
     */
    int deleteActivityRemarkById(@Param("id") String id);

    /**
     * 更新市场活动记录备注
     * @param remark
     * @return
     */
    int updateActivityRemark(ActivityRemark remark);
}