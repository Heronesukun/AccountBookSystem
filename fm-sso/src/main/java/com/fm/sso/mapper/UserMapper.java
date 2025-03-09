package com.fm.sso.mapper;

import com.fm.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    /**
     * 依照传入用户名查询用户信息
     *
     * @param username
     * @return
     */
    User selectByUsername(String username);
    User selectByUserId(Long userId);
}