package com.ssmproject.crm.workbench.web.controller;

import com.ssmproject.crm.commons.constant.Constant;
import com.ssmproject.crm.commons.pojo.ReturnObject;
import com.ssmproject.crm.commons.utils.DateUtils;
import com.ssmproject.crm.commons.utils.UUIDUtils;
import com.ssmproject.crm.settings.pojo.User;
import com.ssmproject.crm.workbench.pojo.ActivityRemark;
import com.ssmproject.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class ActivityRemarkController {

    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/saveCreateActivityRemark.do")
    public @ResponseBody Object saveCreateActivityRemark(ActivityRemark remark, HttpSession session) {
        User user = (User) session.getAttribute(Constant.SESSION_USER);

        // 封装缺失参数
        remark.setId(UUIDUtils.getUUID());
        remark.setCreateTime(DateUtils.formatDateTime(new Date()));
        remark.setCreateBy(user.getId());
        remark.setEditFlag(Constant.REMARK_EDIT_FLAG_NO_EDITED);

        ReturnObject returnObject = new ReturnObject();
        try {
            int result = activityRemarkService.saveCreateActivityRemark(remark);
            if (result > 0) {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(remark);
            } else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后再试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后再试");
        }

        return returnObject;
    }

    @RequestMapping("/workbench/activity/deleteActivityRemark.do")
    public @ResponseBody Object deleteActivityRemark(String id) {
        ReturnObject returnObject = new ReturnObject();
        // 尝试删除当前市场活动记录备注
        try {
            int result = activityRemarkService.deleteActivityRemarkById(id);
            if (result > 0) {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后再试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后再试");
        }

        return returnObject;
    }

    @RequestMapping("/workbench/activity/saveEditActivityRemark.do")
    public @ResponseBody Object saveEditActivityRemark(ActivityRemark remark, HttpSession session) {
        // 获取当前User
        User use = (User) session.getAttribute(Constant.SESSION_USER);

        // 封装缺失参数
        remark.setEditBy(use.getId());
        remark.setEditTime(DateUtils.formatDateTime(new Date()));
        remark.setEditFlag(Constant.REMARK_EDIT_FLAG_YES_EDITED);

        ReturnObject returnObject = new ReturnObject();
        // 尝试修改当前市场活动记录备注
        try {
            int result = activityRemarkService.saveEditActivityRemark(remark);
            if (result > 0) {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(remark);
            } else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后再试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后再试...");
        }

        return returnObject;
    }
}
