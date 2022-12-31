package com.dxw.wx_course.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxw.wx_course.mapper.CoursewareMapper;
import com.dxw.wx_course.pojo.Courseware;
import com.dxw.wx_course.vo.CommonPage;
import com.dxw.wx_course.vo.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoursewareService {
    @Resource
    CoursewareMapper coursewareMapper;

    public Result list(Integer start) {
        IPage<Courseware> page = new Page<>(start, 10);
        IPage<Courseware> coursewareIPage = coursewareMapper.selectPage(page, null);
        CommonPage<Courseware> commonPage= CommonPage.restPage(coursewareIPage);
        List<Courseware> coursewareList=commonPage.getList().stream().peek(x -> x.setUrl(null)).collect(Collectors.toList());
        commonPage.setList(coursewareList);
        return Result.SUCCESS(commonPage);
    }

    public Result getCarousel() {
        LambdaQueryWrapper<Courseware> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.select(Courseware::getId,Courseware::getCarouselUrl);
        queryWrapper.gt(Courseware::getIsCarousel,0);
        queryWrapper.orderByDesc(Courseware::getIsCarousel);
        List<Courseware> coursewareList=coursewareMapper.selectList(queryWrapper);
        return Result.SUCCESS(coursewareList);
    }
}
