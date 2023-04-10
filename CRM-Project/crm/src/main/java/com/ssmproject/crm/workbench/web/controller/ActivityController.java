package com.ssmproject.crm.workbench.web.controller;

import com.ssmproject.crm.settings.pojo.User;
import com.ssmproject.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ActivityController {

    @Autowired
    private UserService userService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request) {
        // 查询所有用户数据并保存到request请求域
        List<User> userList = userService.queryAllUsers();
        request.setAttribute("userList", userList);

        // 转发到市场活动页面
        return "workbench/activity/index";
    }
}
