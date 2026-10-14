package com.zhang.function;

import java.util.ArrayList;

public interface UploadClass {
    /**
     * 装配容器：加载类、建立「接口 -> 实现类」映射，再为 @DiObj 字段注入代理对象。
     *
     * @param classList 扫描得到的全限定类名列表
     */
    void upload(ArrayList<String> classList) throws Exception;
}
