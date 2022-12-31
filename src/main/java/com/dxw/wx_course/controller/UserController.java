package com.dxw.wx_course.controller;

import com.dxw.wx_course.annation.NoAuth;
import com.dxw.wx_course.params.WXAuth;
import com.dxw.wx_course.service.UserService;
import com.dxw.wx_course.vo.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;

@RestController
@RequestMapping("user")
public class UserController {
    @Resource
    private UserService userService;
    @GetMapping("getSessionId")
    @NoAuth
    public Result getSessionId(@RequestParam String code){
        String sessionId=userService.getSessionId(code);
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("sessionId",sessionId);
        return Result.SUCCESS(hashMap);
    }
    @PostMapping("authLogin")
    @NoAuth
    public Result authLog(@RequestBody WXAuth auth){
        Result result=userService.authLogin(auth);
        return result;
    }
    @GetMapping("userinfo")
    public Result getUserInfo(@RequestHeader("Authorization")String token,Boolean refresh){
        return userService.getUserInfo(token,refresh);
    }
}
