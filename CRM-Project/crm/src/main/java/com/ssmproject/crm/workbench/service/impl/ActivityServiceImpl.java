package com.ssmproject.crm.workbench.service.impl;

import com.ssmproject.crm.workbench.mapper.ActivityMapper;
import com.ssmproject.crm.workbench.pojo.Activity;
import com.ssmproject.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public int saveCreateActivity(Activity activity) {
        return activityMapper.insertActivity(activity);
    }
}
