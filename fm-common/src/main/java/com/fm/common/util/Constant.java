package com.fm.common.util;

public interface Constant {
    // 响应状态码
    int RESPONSE_STATUS_OK = 200; // 正常
    int RESPONSE_STATUS_ERROR = 500; // 异常
    int RESPONSE_STATUS_REPEAT = 300; // 正常
    int RESPONSE_STATUS_NOT_FOUND = 400; //未找到
    int RESPONSE_STATUS_OVER_SIZE = 600;//越界
    // 数据状态
    int DATA_STATUS_NORMAL = 1;// 数据正常
    int DATA_STATUS_DISABLE = 2;// 数据禁用
    int DATA_STATUS_ARCHIVE = 3;// 归档操作
    int DATA_STATUS_SUSPEND = 4;// 暂停状态
    int DATA_STATUS_ACCIDENT = 5;// 意外情况
    int DATA_PROJECT_STATUS_RELEASE = 2;// 项目发布状态
    int DATA_STATUS_DELETE = 99;// 删除状态


    int AUTHENTICATION_NO = 506;// 未登录状态
    int AUTHENTICATION_YES = 505;// 登录状态
    int AUTHENTICATION_RENEW = 507;// token续签状态
}