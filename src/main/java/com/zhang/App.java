package com.zhang;

import com.zhang.function.StartClass;
import com.zhang.function.impl.StartClassImpl;
import com.zhang.pojo.DiObj;
import com.zhang.test1.StudentImpl;

public class App {
    @DiObj
    private static StudentImpl student;
    public static void main(String[] args) throws Exception {
        StartClass startClass = new StartClassImpl();
        startClass.start();
        student.test();


    }
}
