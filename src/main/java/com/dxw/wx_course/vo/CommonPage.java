package com.dxw.wx_course.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

@Data
public class CommonPage<T> {
    private Integer pageNum;
    private Integer pageSize;
    private Integer totalPage;
    private Long total;
    private List<T> list;
    /*重载的方法*/
    public static <T> CommonPage<T> restPage(IPage<T> list){
        CommonPage<T> result = new CommonPage<>();
        result.setList(list.getRecords());
        result.setTotal(list.getTotal());
        result.setPageSize((int) list.getSize());
        result.setTotalPage((int) list.getPages());
        result.setPageNum((int) list.getCurrent());
        return result;
    }
    /**
     * 将SpringData分页后的list转为分页信息
     */
   /* public static <T> CommonPage<T> restPage(Page<T> pageInfo) {
        CommonPage<T> result = new CommonPage<T>();
        result.setTotalPage(pageInfo.getTotalPages());
        result.setPageNum(pageInfo.getNumber());
        result.setPageSize((int)pageInfo.getSize());
        result.setTotal(pageInfo.getTotalElements());
        result.setList(pageInfo.getContent());
        return result;
    }*/
}