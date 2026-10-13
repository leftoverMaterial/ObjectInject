package com.zhang.function.impl;

import com.zhang.function.UploadClass;
import com.zhang.pojo.DiObj;
import com.zhang.pojo.IocClass;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;

public class UploadClassImpl implements UploadClass {
    private final HashMap<String, Class<?>> classMap = new HashMap<>();
    private final ArrayList<Class<?>> list = new ArrayList<>();

    @Override
    public void upload(ArrayList<String> classList) throws Exception {
        for (String s : classList) {
            list.add(Class.forName(s));
        }
        getFaces();
        for (Class<?> aClass : list) {
            for (Field field : aClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(DiObj.class)) {
                    Class<?> typeClass = list.get(list.indexOf(field.getType()));
                    Object o = classMap.get(typeClass.getName()).getDeclaredConstructor().newInstance();
                    Object proxy = getProxy(field.getType(), o);
                    field.setAccessible(true);
                    if (Modifier.isStatic(field.getModifiers())) field.set(null, proxy);
                    else {
                        field.set(aClass.getDeclaredConstructor().newInstance(), proxy);
                    }
                }
            }
        }
    }

    private void getFaces() throws ClassNotFoundException {
        for (Class<?> aClass : list) {
            if (aClass.getInterfaces().length > 0 && aClass.isAnnotationPresent(IocClass.class)) {
                for (Class<?> anInterface : aClass.getInterfaces()) {
                    classMap.put(anInterface.getName(), aClass);
                }
            }
        }
    }

    private Object getProxy(Class<?> aClass, Object target) {
        return Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class<?>[]{aClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("我变强了");
                Object result = method.invoke(target, args);
                return result;
            }
        });
    }
}
