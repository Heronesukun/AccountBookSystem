package com.fm.sso.service;

import com.fm.common.pojo.FMResult;
import com.fm.dto.UserRegisterDTO;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description
 * @Time 2025/3/11 19:59
 */

public interface UserRegisterService {
    FMResult register(UserRegisterDTO userRegisterDTO);

}
