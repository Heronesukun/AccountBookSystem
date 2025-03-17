package com.fm.books.interceptors;

import com.fm.common.pojo.FMResult;
import com.fm.common.util.Constant;
import com.fm.common.util.JSONTools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author 张喆
 * @Company
 * @Description 登录拦截器配置
 * @Time 2024/10/15 9:14
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private RestTemplate restTemplate;// 远程请求的工具类
    // SSO服务的地址
    @Value("${sso-url:http://localhost:8088/auth/check}")
    private String ssoURL;
    // 进入方法之前进行拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 处理跨域的问题
        response.setHeader("Access-Control-Allow-Headers", "*");
        //允许所有的方法访问  如post , get方法
        response.setHeader("Access-Control-Allow-Methods", "*");
        //允许所有的域
        response.setHeader("Access-Control-Allow-Origin","*");

        FMResult result = null;
        // 从请求头中获取token，给sso系统发送请求验证token是否有效
        String token = request.getHeader("token");
        if(StringUtils.isBlank(token)){
            // token不存在，直接返回
            result = FMResult.error(Constant.AUTHENTICATION_NO,"未登录,请登录后再操作");
            // 响应客户端，使用response响应
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSONTools.object2json(result));
            return false;
        }
        // token存在就要校验token有效性。
        // 远程请求 校验
        // getForObject参数： 参数1 请求的url，参数2 预期返回的结果类型
        result = restTemplate.getForObject(ssoURL + "?token=" + token, FMResult.class);
        Integer code = result.getCode();
        if(code.equals(new Integer(200))){
            // 校验通过，将用户信息存入request作用域中
            request.setAttribute("longUser",result.getData());
            return true;// 放行
        }else if (code.equals(new Integer(507))){
            //不放行  虽然续签成功了，但是这里暂时不放行，因为我们要通知客户端最新的token。
            // 将最新的toekn响应到客户端去
            // 响应客户端，使用response响应
            response.setContentType("application/json;charset=utf-8");
            // 直接将请求校验token返回的result继续返回即可
            response.getWriter().write(JSONTools.object2json(result));
            return false;
        }
        // token不存在，直接返回
        result = FMResult.error(Constant.AUTHENTICATION_NO,"未登录,请登录后再操作");
        // 响应客户端，使用response响应
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSONTools.object2json(result));
        return false;
    }
}
