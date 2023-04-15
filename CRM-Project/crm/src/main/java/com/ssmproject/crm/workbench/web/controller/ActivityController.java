package com.ssmproject.crm.workbench.web.controller;

import com.ssmproject.crm.commons.constant.Constant;
import com.ssmproject.crm.commons.pojo.ReturnObject;
import com.ssmproject.crm.commons.utils.DateUtils;
import com.ssmproject.crm.commons.utils.HSSFUtils;
import com.ssmproject.crm.commons.utils.UUIDUtils;
import com.ssmproject.crm.settings.pojo.User;
import com.ssmproject.crm.settings.service.UserService;
import com.ssmproject.crm.workbench.pojo.Activity;
import com.ssmproject.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

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
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));

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

    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    public @ResponseBody Object queryActivityByConditionForPage(String name, String owner, String startDate, String endDate, int pageNo ,int pageSize) {

        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);

        System.out.println(map.get("name"));
        System.out.println(map);

        //调用service层方法，查询数据
        List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
        int totalRows = activityService.queryCountOfActivityByCondition(map);

        //根据查询结果结果，生成响应信息
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("activityList", activityList);
        retMap.put("totalRows", totalRows);

        return retMap;
    }

    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    public @ResponseBody Object deleteActivityByIds(String[] id) {
        ReturnObject returnObject = new ReturnObject();

        // 尝试删除指定市场活动数据
        try {
            int result = activityService.deleteActivityByIds(id);
            if (result > 0) {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setMessage("删除成功");
            } else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后再试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后再试...");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityById.do")
    public @ResponseBody Object queryActivityById(String id) {
        // 根据id查询市场活动记录
        return activityService.queryActivityById(id);
    }

    @RequestMapping("/workbench/activity/saveEditActivity.do")
    public @ResponseBody Object saveEditActivity(Activity activity, HttpSession session) {
        // 获取当前用户
        User user = (User) session.getAttribute(Constant.SESSION_USER);

        // 补全市场活动记录信息
        activity.setEditTime(DateUtils.formatDateTime(new Date()));
        activity.setEditBy(user.getId());

        // 尝试更改市场活动记录
        ReturnObject returnObject = new ReturnObject();
        try {
            int result = activityService.saveEditActivity(activity);
            if (result > 0) {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setMessage("更新成功");
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

    @RequestMapping("/workbench/activity/exportAllActivities.do")
    public void exportAllActivities(HttpServletResponse response) throws IOException {
        // 查询所有市场活动记录
        List<Activity> activityList = activityService.queryAllActivities();

        // 新建Excel文件, 新建Excel表，新建Excel第一行
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("市场活动记录表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建者");
        cell = row.createCell(9);
        cell.setCellValue("修改时间");
        cell = row.createCell(10);
        cell.setCellValue("修改者");

        //遍历activityList，创建HSSFRow对象，生成所有的数据行
        if (activityList != null && activityList.size() > 0){
            Activity activity = null;
            for (int i = 0;i < activityList.size(); i++){
                activity = activityList.get(i);
                //每遍历出一个activity，生成一行
                row = sheet.createRow(i+1);
                //每一行创建11列，每一列的数据从activity中获取
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }

        // 设置响应内容格式和响应头
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=activityList.xls");

        // 拿到响应文件输出流并写入
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.flush();
    }

    @RequestMapping("/workbench/activity/importActivities.do")
    public @ResponseBody Object importActivities(MultipartFile activityFile, HttpSession session, HttpServletResponse response) {
        // 获取当前用户
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        ReturnObject returnObject = new ReturnObject();

        // 尝试读取Excel文件并将数据插入至数据库
        try {
            // 获取Excel表
            InputStream inputStream = activityFile.getInputStream();
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            HSSFSheet sheet = workbook.getSheetAt(0);

            HSSFRow row = null;
            HSSFCell cell = null;
            Activity activity = null;
            List<Activity> activityList = new ArrayList<>();

            // 循环读取信息,封装到activity中
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                activity = new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateTime(DateUtils.formatDateTime(new Date()));
                activity.setCreateBy(user.getId());

                for(int j = 0;j < row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    String cellValue = HSSFUtils.getCellValueForStr(cell);
                    if(j == 0){
                        activity.setName(cellValue);
                    }else if(j == 1){
                        activity.setStartDate(cellValue);
                    }else if(j == 2){
                        activity.setEndDate(cellValue);
                    }else if(j == 3){
                        activity.setCost(cellValue);
                    }else if(j == 4){
                        activity.setDescription(cellValue);
                    }
                }
                //每一行中所有列都封装完成之后，把activity保存到list中
                activityList.add(activity);
            }

            int result = activityService.saveCreateActivities(activityList);
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setMessage("插入成功");
            returnObject.setRetData(result);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后再试...");
        }

        return returnObject;
    }
}
