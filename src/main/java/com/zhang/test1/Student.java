package com.zhang.test1;

import com.zhang.pojo.IocClass;

@IocClass
public class Student implements StudentImpl{
    /**
     * 目标方法：真正被调用时，代理会先打印增强日志再执行这里。
     */
    @Override
    public void test(){
        System.out.println("笨蛋");
    }
}
