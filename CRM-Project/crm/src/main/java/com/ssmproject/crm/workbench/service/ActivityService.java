package com.ssmproject.crm.workbench.service;

import com.ssmproject.crm.workbench.pojo.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    int saveCreateActivity(Activity activity);

    List<Activity> queryActivityByConditionForPage(Map<String, Object> map);

    int queryCountOfActivityByCondition(Map<String, Object> map);

    int deleteActivityByIds(String[] ids);

    Activity queryActivityById(String id);

    int saveEditActivity(Activity activity);

    List<Activity> queryAllActivities();

    int saveCreateActivities(List<Activity> activityList);

    Activity queryActivityForDetailById(String id);
}
