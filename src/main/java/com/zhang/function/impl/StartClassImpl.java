package com.zhang.function.impl;

import com.zhang.function.GetCatalog;
import com.zhang.function.StartClass;
import com.zhang.function.UploadClass;

import java.net.URL;
import java.util.ArrayList;

public class StartClassImpl implements StartClass {
    /**
     * 启动容器：按「定位包目录 -> 扫描类 -> 装配注入」三步编排。
     */
    @Override
    public void start() throws Exception{
        GetCatalog getCatalogImpl = new GetCatalogImpl();
        URL dir = getCatalogImpl.getCatalog();

        ArrayList<String> classList = GetClassImpl.getInstance().getClassList(dir);

        UploadClass uploadClassImpl = new UploadClassImpl();
        uploadClassImpl.upload(classList);
    }
}
