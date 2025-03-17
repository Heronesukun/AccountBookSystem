package com.fm.sso.mapper;

import com.fm.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    User selectByUsername(String username);
    
    /**
     * 根据用户ID查询用户
     * @param userId 用户ID
     * @return 用户对象
     */
    User selectByUserId(Long userId);
    
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户对象
     */
    User findByUsername(String username);
    
    /**
     * 插入新用户
     * @param user 用户对象
     * @return 影响行数
     */
    int insert(User user);
}