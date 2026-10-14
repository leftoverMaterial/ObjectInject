package com.zhang.function;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public interface GetClass {
    /**
     * 递归扫描指定包目录，收集其下所有类的全限定名。
     *
     * @param dir 包目录的 URL
     * @return 全限定类名列表，例如 com.zhang.test1.Student
     */
    ArrayList<String> getClassList(URL dir) throws Exception;
}
