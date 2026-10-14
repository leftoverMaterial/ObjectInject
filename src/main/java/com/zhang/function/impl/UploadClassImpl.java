package com.zhang.function.impl;

import com.zhang.exception.BeanInjectException;
import com.zhang.function.UploadClass;
import com.zhang.pojo.DiObj;
import com.zhang.pojo.IocClass;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;

public class UploadClassImpl implements UploadClass {
    private final HashMap<String, Class<?>> classMap = new HashMap<>();
    private final ArrayList<Class<?>> list = new ArrayList<>();

    /**
     * 装配容器：加载扫描到的类，建立「接口 -> 实现类」映射，
     * 再为每个 @DiObj 字段创建实现类实例、包装成代理后注入。
     *
     * @param classList 扫描得到的全限定类名列表
     */
    @Override
    public void upload(ArrayList<String> classList) throws Exception {
        for (String s : classList) {
            list.add(Class.forName(s));
        }
        getFaces();
        for (Class<?> aClass : list) {
            for (Field field : aClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(DiObj.class)) {
                    Class<?> typeClass = field.getType();
                    Class<?> implClass = classMap.get(typeClass.getName());
                    if (implClass == null) {
                        throw new BeanInjectException("@DiObj 字段 " + aClass.getName() + "." + field.getName()
                                + " 的类型 " + typeClass.getName() + " 找不到实现类（实现类是否漏标 @IocClass?）");
                    }
                    Object obj = implClass.getDeclaredConstructor().newInstance();
                    Object proxy = getProxy(typeClass, obj);
                    field.setAccessible(true);
                    if (Modifier.isStatic(field.getModifiers())) field.set(null, proxy);
                    else {
                        throw new BeanInjectException("暂不支持非静态字段注入: " + aClass.getName() + "." + field.getName()
                                + "，当前容器不托管 Bean 实例");
                    }
                }
            }
        }
    }

    /**
     * 建立「接口全限定名 -> 实现类」映射表，
     * 只收录实现了接口、且标注了 @IocClass 的类。
     */
    private void getFaces() throws ClassNotFoundException {
        for (Class<?> aClass : list) {
            if (aClass.getInterfaces().length > 0 && aClass.isAnnotationPresent(IocClass.class)) {
                for (Class<?> anInterface : aClass.getInterfaces()) {
                    classMap.put(anInterface.getName(), aClass);
                }
            }
        }
    }

    /**
     * 用 JDK 动态代理把实现类实例包装成接口代理，在方法调用前织入增强逻辑。
     *
     * @param aClass 被代理的接口
     * @param target 真实的实现类实例
     * @return 代理对象
     */
    private Object getProxy(Class<?> aClass, Object target) {
        return Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class<?>[]{aClass}, new InvocationHandler() {
            /**
             * 代理回调：打印增强日志后，调用真实对象的目标方法。
             */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("我变强了");
                Object result = method.invoke(target, args);
                return result;
            }
        });
    }
}
