package com.zhang.function.impl;

import com.zhang.function.GetCatalog;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class GetCatalogImpl implements GetCatalog {
    /**
     * 定位被扫描包（com.zhang）在 classpath 上的目录。
     *
     * @return 包目录的 URL，例如 .../target/classes/com/zhang
     */
    @Override
    public URL getCatalog() throws Exception {
        //获取包名
        String packageName = "com.zhang";
        //获取包名全路径
        String packagePath = packageName.replace(".", "/");
        URL dir = Thread.currentThread().getContextClassLoader().getResource(packagePath);
        return dir;
    }

    /**
     * 定位 classpath 根目录，作为后续剥离路径前缀得到类名的基准。
     *
     * @return classpath 根目录
     */
    @Override
    public File getFile() throws URISyntaxException {
        URL root = Thread.currentThread().getContextClassLoader().getResource("");
        File rootDir = new File(root.toURI());
        return rootDir;
    }
}
