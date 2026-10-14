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


    /**
     * 私有构造器：禁止外部 new，保证全局只有一个实例。
     */
    private GetClassImpl() {
    }

    /**
     * 获取单例实例。
     *
     * @return 全局唯一的 GetClassImpl
     */
    public static GetClassImpl getInstance() {
        return getCatalogImpl;
    }

    /**
     * 扫描包目录，返回其下所有类的全限定名。
     * 注意：classList 是实例字段且未清空，重复调用会累积结果。
     *
     * @param dir 包目录的 URL
     * @return 全限定类名列表
     */
    @Override
    public ArrayList<String> getClassList(URL dir) throws Exception {

        getStringFile();
        // 获取包名下的所有类
        File file = new File(dir.toURI());
        directoryRecursion(file);
        return classList;
    }

    /**
     * 递归遍历目录，把 .class 文件（排除 $ 内部类）收集成类名。
     *
     * @param file 当前遍历到的目录
     */
    private void directoryRecursion(File file) {
        File[] files = file.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) directoryRecursion(f);
            else if (f.getName().endsWith(".class") && !f.getName().contains("$"))
                classList.add(toClassName(f));
        }
    }

    /**
     * 把 .class 文件路径转成全限定类名。
     * 例：.../classes/com/zhang/App.class -> com.zhang.App
     *
     * @param f .class 文件
     * @return 全限定类名
     * @throws IllegalStateException 文件不在 classpath 根目录下时抛出
     */
    private String toClassName(File f) {
        String path = f.getAbsolutePath();
        if (path.startsWith(classPath))
            path = path.substring(classPath.length());
        else
            throw new IllegalStateException("类文件不在 classpath 下: " + path);

        path = path.replace(File.separatorChar, '.');
        return path.substring(0, path.length() - ".class".length());
    }

    /**
     * 计算并缓存 classpath 根路径（末尾带分隔符），作为剥离类名前缀的基准。
     */
    private void getStringFile() throws URISyntaxException {
        GetCatalogImpl getFile = new GetCatalogImpl();
        File file = getFile.getFile();
        classPath = file.getAbsolutePath() + File.separator;
    }
}
