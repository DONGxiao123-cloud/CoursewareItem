package com.dxw.wx_course.vo;

import com.dxw.wx_course.enums.ResultCode;
import lombok.Data;

@Data
public class Result<T> {
    Integer code;
    String message;
    T data;
    /*生成泛型对象，泛型对象在使用之前类型是确定的*/
    /*隐式推导:根据构造函数传递参数的类型就是泛型的类型*/
    /*显示指定:在new对象时显示指定泛型对象的类型*/

    /*泛型静态方法：不同于泛型类，泛型方法可以视为一种泛类型的方法接受任何类型的参数*/
    /**/
    public static Result SUCCESS(){
        return new Result(ResultCode.SUCCESS);
    }
    public static <T> Result SUCCESS(T data) {
        return new Result(ResultCode.SUCCESS, data);
    }
    public static Result FAIL() {
        return new Result(ResultCode.FAIL);
    }

    public static Result FAIL(String message) {
        return new Result(message);
    }

    public Result (ResultCode resultCode){
        this.code=resultCode.getCode();
        this.message=resultCode.getMessage();
    }
    public Result (ResultCode resultCode,T data){
        this.code=resultCode.getCode();
        this.message=resultCode.getMessage();
        this.data=data;
    }
    public Result (String message){
        this.message=message;
    }

}
