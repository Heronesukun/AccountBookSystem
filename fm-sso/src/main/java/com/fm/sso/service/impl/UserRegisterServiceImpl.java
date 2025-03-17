package com.fm.sso.service.impl;

import com.fm.common.pojo.FMResult;
import com.fm.dto.UserRegisterDTO;
import com.fm.pojo.User;
import com.fm.sso.mapper.UserMapper;
import com.fm.sso.service.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;

/**
 * 用户注册服务实现类
 */
@Service
public class UserRegisterServiceImpl implements UserRegisterService {
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public FMResult register(UserRegisterDTO userRegisterDTO) {
        // 检查用户名是否已存在
        User existUser = userMapper.findByUsername(userRegisterDTO.getUsername());
        if (existUser != null) {
            return FMResult.error("用户名已存在");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        // 使用MD5加密密码
        user.setUserPass(DigestUtils.md5DigestAsHex(userRegisterDTO.getPassword().getBytes()));
        user.setRealName(userRegisterDTO.getRealName());
        user.setCreateTime(new Date());
        user.setModifyTime(new Date());
        user.setUserStatus(1);

        // 保存用户
        int result = userMapper.insert(user);
        if (result > 0) {
            return FMResult.success("注册成功");
        } else {
            return FMResult.error("注册失败，请稍后重试");
        }
    }
}