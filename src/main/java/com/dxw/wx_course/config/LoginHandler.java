package com.dxw.wx_course.config;

import com.alibaba.fastjson.JSON;
import com.dxw.wx_course.annation.NoAuth;
import com.dxw.wx_course.utils.JWTUtils;
import com.dxw.wx_course.utils.RedisKey;
import com.dxw.wx_course.utils.UserThreadLocal;
import com.dxw.wx_course.vo.Result;
import com.dxw.wx_course.vo.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginHandler implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if(handlerMethod.hasMethodAnnotation(NoAuth.class)){
            return true;
        }

        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)){
            return noLoginResponse(response);
        }
        token = token.replace("Bearer ", "");
        boolean verify = JWTUtils.verify(token);
        if (!verify){
            return noLoginResponse(response);
        }
        String userJson = stringRedisTemplate.opsForValue().get(RedisKey.TOKEN_KEY + token);
        if (StringUtils.isBlank(userJson)){
            return noLoginResponse(response);
        }
        UserDto userDto = JSON.parseObject(userJson, UserDto.class);
        UserThreadLocal.put(userDto);
        return true;
    }
    private boolean noLoginResponse(HttpServletResponse response) throws  IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(Result.FAIL("未登录")));
        return false;
}}
