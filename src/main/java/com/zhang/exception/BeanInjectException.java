package com.zhang.exception;

/**
 * 容器注入异常：@DiObj 字段注入失败，或使用了当前容器不支持的注入方式时抛出。
 */
public class BeanInjectException extends RuntimeException {
    /**
     * @param message 失败的具体原因（哪个类的哪个字段、为什么失败）
     */
    public BeanInjectException(String message) {
        super(message);
    }
}
