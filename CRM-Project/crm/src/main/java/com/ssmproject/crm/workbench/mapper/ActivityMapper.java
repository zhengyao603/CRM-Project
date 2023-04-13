package com.ssmproject.crm.workbench.mapper;

import com.ssmproject.crm.workbench.pojo.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ActivityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Tue Apr 11 16:09:59 CST 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Tue Apr 11 16:09:59 CST 2023
     */
    int insertSelective(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Tue Apr 11 16:09:59 CST 2023
     */
    int updateByPrimaryKeySelective(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Tue Apr 11 16:09:59 CST 2023
     */
    int updateByPrimaryKey(Activity record);

    /**
     * 插入新的市场活动记录
     * @param activity 需要插入的市场活动记录
     * @return 插入操作影响的行数
     */
    int insertActivity(Activity activity);

    /**
     * 根据条件分页查询市场活动数据
     * @param map
     * @return
     */
    List<Activity> selectActivityByConditionForPage(Map<String, Object> map);

    /**
     * 根据条件查询市场活动记录总条数
     * @param map
     * @return
     */
    int selectCountOfActivityByCondition(Map<String, Object> map);

    /**
     * 删除指定id数组内的市场活动记录
     * @param ids
     * @return
     */
    int deleteActivityByIds(@Param("ids") String[] ids);

    /**
     * 根据指定id查询市场活动记录
     * @param id
     * @return
     */
    Activity selectActivityById(@Param("id") String id);
}