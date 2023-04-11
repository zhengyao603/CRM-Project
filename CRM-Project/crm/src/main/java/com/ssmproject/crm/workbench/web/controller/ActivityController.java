package com.ssmproject.crm.workbench.web.controller;

import com.ssmproject.crm.commons.constant.Constant;
import com.ssmproject.crm.commons.pojo.ReturnObject;
import com.ssmproject.crm.commons.utils.DateUtils;
import com.ssmproject.crm.commons.utils.UUIDUtils;
import com.ssmproject.crm.settings.pojo.User;
import com.ssmproject.crm.settings.service.UserService;
import com.ssmproject.crm.workbench.pojo.Activity;
import com.ssmproject.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class ActivityController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request) {
        // 查询所有用户数据并保存到request请求域
        List<User> userList = userService.queryAllUsers();
        request.setAttribute("userList", userList);

        // 转发到市场活动页面
        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    public @ResponseBody Object saveCreateActivity(Activity activity, HttpSession session) {
        // 封装所需参数
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateBy(user.getId());
        activity.setCreateBy(DateUtils.formatDateTime(new Date()));

        // 尝试插入新的市场活动记录
        ReturnObject returnObject = new ReturnObject();
        try {
            int result = activityService.saveCreateActivity(activity);
            if (result > 0) {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setMessage("创建市场活动成功");
            } else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试...");
        }

        return returnObject;
    }
}
