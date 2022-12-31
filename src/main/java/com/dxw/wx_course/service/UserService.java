package com.dxw.wx_course.service;

import cn.hutool.core.lang.UUID;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dxw.wx_course.mapper.UserMapStruct;
import com.dxw.wx_course.mapper.UserMapper;
import com.dxw.wx_course.model.WXUserInfo;
import com.dxw.wx_course.params.WXAuth;
import com.dxw.wx_course.pojo.User;
import com.dxw.wx_course.utils.JWTUtils;
import com.dxw.wx_course.utils.RedisKey;
import com.dxw.wx_course.vo.Result;
import com.dxw.wx_course.vo.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


@Service
public class UserService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private WxService wxService;
    @Resource//默认按照名称注入
    private UserMapStruct userMapStruct;
    @Resource
    private UserMapper userMapper;
    @Value("${wxmini.secret}")
    private String secret;
    @Value("${wxmini.appid}")
    private String appId;
    //两步请求，第一步将三者联系起来，服务端利用code从微信服务端获取到开放数据并缓存起来
    //第二部，利用客户端的密钥信息对服务端存储的信息进行解密，
    public String getSessionId(String code) {
        //code--->后端---->uid+sessionKey----->利用开放接口请求其他信息或者给小程序返回一些信息
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid={0}&secret={1}&js_code={2}&grant_type=authorization_code";
        url = url.replace("{0}", appId).replace("{1}", secret).replace("{2}", code);
        String res = HttpUtil.get(url);
        String uidStr = UUID.randomUUID().toString();
        System.out.println(res);
        stringRedisTemplate.opsForValue().set(RedisKey.WX_SESSION_ID + uidStr, res, 30, TimeUnit.MINUTES);
        System.out.println(stringRedisTemplate.opsForValue().get((RedisKey.WX_SESSION_ID + uidStr)));
        return uidStr;
    }

    public Result authLogin(WXAuth auth) {
        try {
            String wxRes = wxService.wxDecrypt(auth.getEncryptedData(), auth.getSessionId(), auth.getIv());
            WXUserInfo wxUserInfo = JSON.parseObject(wxRes, WXUserInfo.class);
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper();
            wrapper.eq(User::getOpenId, wxUserInfo.getOpenId());
            User user = userMapper.selectOne(wrapper);
            UserDto userDto = new UserDto();
            userDto.from(wxUserInfo);
            if (user == null) {
                return register(userDto);
            } else {
                userDto.setId(user.getId());
                return login(userDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAIL();
        }
    }

    public Result register(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        userMapper.insert(user);
        userDto.setId(user.getId());//ID一致性
        return login(userDto);
    }

    public Result login(UserDto userDto) {
        userDto.setPassword(null);
        userDto.setUsername(null);
        userDto.setOpenId(null);
        userDto.setWxUnionId(null);
        String token = JWTUtils.sign(userDto.getId());
        userDto.setToken(token);
        stringRedisTemplate.opsForValue().set(RedisKey.TOKEN_KEY + token, JSON.toJSONString(userDto), 7, TimeUnit.DAYS);
        return Result.SUCCESS(userDto);
    }

    public Result getUserInfo(String token, Boolean refresh) {
        token=token.replace("Bearer ","");
        boolean verifyToken=JWTUtils.verify(token);
        if(!verifyToken){
            return Result.FAIL("未登录");
        }
        String userJson=stringRedisTemplate.opsForValue().get(RedisKey.TOKEN_KEY+token);
        if(StringUtils.isBlank(userJson)){
            return Result.FAIL("未登录");
        }
        UserDto userDto=JSON.parseObject(userJson,UserDto.class);
        if(refresh){
            token=JWTUtils.sign(userDto.getId());
            userDto.setToken(token);
            stringRedisTemplate.opsForValue().set(RedisKey.TOKEN_KEY+token,JSON.toJSONString(userDto),7,TimeUnit.DAYS);
        }
        return Result.SUCCESS(userDto);
    }
}
