package com.dxw.wx_course.controller;

import com.dxw.wx_course.annation.NoAuth;
import com.dxw.wx_course.service.CoursewareService;
import com.dxw.wx_course.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("courseware")
public class CoursewareController {
    @Resource
    private CoursewareService coursewareService;
    //获取主界面信息
    @GetMapping("list")
    @NoAuth
    public Result listCourseware(@RequestParam Integer start){
        return coursewareService.list(start);
    }
    @GetMapping("getCarousel")
    //获取首页轮播图
    @NoAuth
    public Result getCarousel(){
        return coursewareService.getCarousel();
    }
}
