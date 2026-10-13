package com.zhang.function.impl;

import com.zhang.function.GetCatalog;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class GetCatalogImpl implements GetCatalog {
    @Override
    public URL getCatalog() throws Exception {
        //获取包名
        String packageName = "com.zhang";
        //获取包名全路径
        String packagePath = packageName.replace(".", "/");
        URL dir = Thread.currentThread().getContextClassLoader().getResource(packagePath);
        return dir;
    }

    @Override
    public File getFile() throws URISyntaxException {
        URL root = Thread.currentThread().getContextClassLoader().getResource("");
        File rootDir = new File(root.toURI());
        return rootDir;
    }
}
