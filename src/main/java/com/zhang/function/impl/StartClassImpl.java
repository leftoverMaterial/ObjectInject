package com.zhang.function.impl;

import com.zhang.function.GetCatalog;
import com.zhang.function.StartClass;
import com.zhang.function.UploadClass;

import java.net.URL;
import java.util.ArrayList;

public class StartClassImpl implements StartClass {
    @Override
    public void start() throws Exception{
        GetCatalog getCatalogImpl = new GetCatalogImpl();
        URL dir = getCatalogImpl.getCatalog();

        ArrayList<String> classList = GetClassImpl.getInstance().getClassList(dir);

        UploadClass uploadClassImpl = new UploadClassImpl();
        uploadClassImpl.upload(classList);
    }
}
