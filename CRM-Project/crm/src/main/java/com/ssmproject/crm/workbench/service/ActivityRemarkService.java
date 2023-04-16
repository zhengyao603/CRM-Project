package com.ssmproject.crm.workbench.service;

import com.ssmproject.crm.workbench.pojo.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {
    List<ActivityRemark> queryActivityRemarkForDetailByActivityId(String activityId);

    int saveEditActivityRemark(ActivityRemark remark);
}
