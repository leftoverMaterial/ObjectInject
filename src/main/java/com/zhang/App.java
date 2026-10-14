package com.zhang;

import com.zhang.function.StartClass;
import com.zhang.function.impl.StartClassImpl;
import com.zhang.pojo.DiObj;
import com.zhang.test1.StudentImpl;

public class App {
    @DiObj
    private static StudentImpl student;
    /**
     * 程序入口：先启动 IoC 容器完成依赖注入，再调用被注入的代理对象验证 AOP 增强。
     *
     * @param args 命令行参数（本示例未使用）
     */
    public static void main(String[] args) throws Exception {
        StartClass startClass = new StartClassImpl();
        startClass.start();
        student.test();


    }
}
