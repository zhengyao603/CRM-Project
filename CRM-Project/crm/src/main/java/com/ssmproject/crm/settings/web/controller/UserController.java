package com.ssmproject.crm.settings.web.controller;

import com.ssmproject.crm.commons.constant.Constant;
import com.ssmproject.crm.commons.pojo.ReturnObject;
import com.ssmproject.crm.commons.utils.DateUtils;
import com.ssmproject.crm.settings.pojo.User;
import com.ssmproject.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin() {
        // 转发到登陆界面
        return "settings/qx/user/login";
    }

    @RequestMapping("/settings/qx/user/doLogin.do")
    public @ResponseBody Object doLogin(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        // 封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        // 获取查询结果
        User user = userService.queryUserByLoginActAndPwd(map);

        // 根据查询结果生成相应信息
        ReturnObject returnObject = new ReturnObject();
        if (user == null) {
            // 登录失败，用户名或密码错误
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名或密码错误");
        } else {
            // 进一步判断账号是否合法
            String curTime = DateUtils.formatDateTime(new Date());
            if (curTime.compareTo(user.getExpireTime()) > 0) {
                // 登录失败，账号已过期
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号已过期");
            } else if ("0".equals(user.getExpireTime())) {
                // 登录失败，账号被锁定
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号被锁定");
            } else if (!user.getAllowIps().contains(request.getRemoteAddr())) {
                // 登录失败，非常用ip地址
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("非常用ip地址");
            } else {
                // 登录成功
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setMessage("登录成功");

                // 把当前user信息保存到session中
                session.setAttribute(Constant.SESSION_USER, user);

                if ("true".equals(isRemPwd)) {
                    // 如果记住密码，设置cookie
                    Cookie c1 = new Cookie("loginAct", user.getLoginAct());
                    c1.setMaxAge(10*24*60*60);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd", user.getLoginPwd());
                    c2.setMaxAge(10*24*60*60);
                    response.addCookie(c2);
                } else {
                    // 如果不记住密码，则保证无相关cookie
                    Cookie c1 = new Cookie("loginAct", "1");
                    c1.setMaxAge(0);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd", "1");
                    c2.setMaxAge(0);
                    response.addCookie(c2);
                }
            }
        }
        return returnObject;
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response, HttpSession session) {
        // 清空cookie
        Cookie c1 = new Cookie("loginAct", "1");
        c1.setMaxAge(0);
        response.addCookie(c1);
        Cookie c2 = new Cookie("loginPwd", "1");
        c2.setMaxAge(0);
        response.addCookie(c2);

        // 销毁session
        session.invalidate();

        // 重定向到首页
        return "redirect:/";
    }
}
