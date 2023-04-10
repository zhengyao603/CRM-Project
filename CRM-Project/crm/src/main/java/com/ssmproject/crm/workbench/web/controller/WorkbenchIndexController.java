package com.ssmproject.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WorkbenchIndexController {

    @RequestMapping("/workbench/index.do")
    public String index() {
        // 转发到工作台整体页面
        return ("workbench/index");
    }
}
