package com.ssmproject.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/workbench/main/index.do")
    public String index() {
        // 转发到工作台主页面
        return "workbench/main/index";
    }
}
