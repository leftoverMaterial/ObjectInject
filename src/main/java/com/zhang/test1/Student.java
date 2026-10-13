package com.zhang.test1;

import com.zhang.pojo.IocClass;

@IocClass
public class Student implements StudentImpl{
    @Override
    public void test(){
        System.out.println("笨蛋");
    }
}
