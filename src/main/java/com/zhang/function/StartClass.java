package com.zhang.function;

public interface StartClass {
    /**
     * 启动容器，按「定位包 -> 扫描类 -> 装配注入」的顺序编排整个流程。
     */
    void start() throws Exception;
}
