package com.dxw.wx_course.controller;

import com.alibaba.fastjson.JSONObject;
import com.dxw.wx_course.annation.NoAuth;
import com.dxw.wx_course.pojo.Order;
import com.dxw.wx_course.service.OrderService;
import com.dxw.wx_course.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("order")
public class OrderController {
    /*@Autowired
    private OrderService orderService;
    @PostMapping("create")
    public Result createOrder(@RequestBody Order order){
        return orderService.createOrder(order);
    }*/
    /*@NoAuth
    @PostMapping("/callback")
    public String asyncCallBack(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        JSONObject jsonObject = new JSONObject();
        for (String key : parameterMap.keySet()) {
            String[] strings = parameterMap.get(key);
            jsonObject.put(key, strings[0]);
        }
        boolean b = orderService.callBack(jsonObject, request);
        return b ? "SUCCESS" : null;
    }*/
}
