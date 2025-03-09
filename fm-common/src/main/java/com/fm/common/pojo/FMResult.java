package com.fm.common.pojo;

import com.fm.common.util.Constant;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description
 * @Time 2024/12/5 15:59
 */

public class FMResult {
    private Integer code;
    private String msg;
    private Object data;
    public FMResult() {
    }
    // 为了方便获取这个类对象，我们添加一些获取这类对象的方法
    public FMResult(Integer code, String msg, Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    // 静态方法获取
    public static FMResult success(){
        return new FMResult(Constant.RESPONSE_STATUS_OK,"OK",null);
    }
    public static FMResult success(Integer code){
        return new FMResult(code,"OK",null);
    }
    public static FMResult success(Integer code, String msg){
        return new FMResult(code,msg,null);
    }
    public static FMResult success(Object data){
        return new FMResult(Constant.RESPONSE_STATUS_OK,"OK",data);
    }
    public static FMResult success(Integer code, String msg, Object data){
        return new FMResult(code,msg,data);
    }

    public static FMResult error(){
        return new FMResult(Constant.RESPONSE_STATUS_ERROR,"ERROR",null);
    }
    public static FMResult error(Integer code){
        return new FMResult(code,"ERROR",null);
    }
    public static FMResult error(Integer code, String msg){
        return new FMResult(code,msg,null);
    }
    public static FMResult error(Object data){
        return new FMResult(Constant.RESPONSE_STATUS_ERROR,"OK",data);
    }

    public static FMResult isSuccess(int i) {
        return i == 1?success():error();
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
