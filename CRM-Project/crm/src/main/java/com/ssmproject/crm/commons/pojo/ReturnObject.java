package com.ssmproject.crm.commons.pojo;

public class ReturnObject {

    // 返回码 成功1 失败0
    private String code;
    // 返回信息
    private String message;
    // 其他返回数据
    private Object retData;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRetData() {
        return retData;
    }

    public void setRetData(Object retData) {
        this.retData = retData;
    }
}
