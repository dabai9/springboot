package com.lianxi.springboot.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口统一返回包装类
 */

@Data
//无参构造
@NoArgsConstructor
//有参构造
@AllArgsConstructor
//统一返回结果集
public class Result {


    //判断
    private String code;
    //定义通用返回类
    private String msg;
    //后台携带的数据
    private Object data;

    public static Result success() {
        return new Result(Constants.CODE_200, "", null);
    }

    public static Result success(Object data) {
        return new Result(Constants.CODE_200, "", data);
    }

    public static Result error(String code, String msg) {
        return new Result(code, msg, null);
    }

    public static Result error(){
        return new Result(Constants.CODE_500,"系统错误",null);
    }

}
