package com.example.reggie.common;

/**
 * 基于ThreadLocal工具类用来设置线程的用户id
 */
public class ThreadLocalConfig {
    //获取工具
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();
    //获取id
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    //设置id
    public static Long getCurrentId(){
        return  threadLocal.get();
    }
}
