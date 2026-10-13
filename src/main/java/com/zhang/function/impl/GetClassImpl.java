package com.zhang.function.impl;

import com.zhang.function.GetClass;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class GetClassImpl implements GetClass {
    private static final GetClassImpl getCatalogImpl = new GetClassImpl();
    private final ArrayList<String> classList = new ArrayList<>();
    private String classPath;


    private GetClassImpl() {
    }

    public static GetClassImpl getInstance() {
        return getCatalogImpl;
    }

    @Override
    public ArrayList<String> getClassList(URL dir) throws Exception {

        getStringFile();
        // 获取包名下的所有类
        File file = new File(dir.toURI());
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                directoryRecursion(f);
            } else {
                classList.add(f.getPath().replace(classPath, "").replace("\\", ".").replace(".class", ""));
            }
        }
        return classList;
    }

    private void directoryRecursion(File file) {
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                directoryRecursion(f);
            } else {
                classList.add(f.getPath().replace(classPath, "").replace("\\", ".").replace(".class", ""));
            }
        }
    }

    private void getStringFile() throws URISyntaxException {
        GetCatalogImpl getFile = new GetCatalogImpl();
        File file = getFile.getFile();
        classPath = file.getAbsolutePath() + File.separator;
    }
}
