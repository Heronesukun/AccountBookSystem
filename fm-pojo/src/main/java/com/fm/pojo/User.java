package com.fm.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.xml.crypto.Data;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description
 * @Time 2024/12/5 10:27
 */

public class User {
    private Long userId;
    private String username;
    private String userPass;
    private String realName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Data createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Data modifyTime;
    private Integer userStatus;
    private Integer deptId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Data getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Data createTime) {
        this.createTime = createTime;
    }

    public Data getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Data modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", userPass='" + userPass + '\'' +
                ", realName='" + realName + '\'' +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", userStatus=" + userStatus +
                ", deptId=" + deptId +
                '}';
    }
}