# ObjectInject

> 手写的迷你 IoC/DI 容器 + AOP 增强器 —— 个人 Java 学习项目

**当前版本：`v0.1.1`** ｜ 最后更新：2026-10-14 ｜ 作者：[leftoverMaterial](https://github.com/leftoverMaterial)

---

## 这是什么

一个**从零手写**的迷你 IoC / DI 容器，零第三方依赖，全部基于 JDK 原生反射与动态代理实现。

写它的目的不是造一个能用的框架，而是把「Spring 到底帮我做了什么」这件事拆开看清楚 —— 注解是怎么被扫描到的、对象是怎么被创建出来的、代理是怎么在方法调用前后织入逻辑的。

**代码量约 220 行，14 个源文件（不含注释）。**

---

## 核心概念

容器只用两个注解：

| 注解 | 目标 | 保留策略 | 含义 |
|---|---|---|---|
| `@IocClass` | `TYPE` | `RUNTIME` | 标记这个实现类归容器管理（相当于 Spring 的 `@Component`） |
| `@DiObj` | `FIELD` | `RUNTIME` | 标记这个字段需要注入（相当于 Spring 的 `@Autowired`） |

用起来是这样：

```java
public interface StudentImpl {
    void test();
}

@IocClass                                   // 声明：这个类归容器管
public class Student implements StudentImpl {
    @Override
    public void test() {
        System.out.println("笨蛋");
    }
}

public class App {
    @DiObj                                  // 声明：这个字段请容器注入
    private static StudentImpl student;

    public static void main(String[] args) throws Exception {
        new StartClassImpl().start();       // 启动容器
        student.test();                     // 拿到的其实是代理对象
    }
}
```

运行输出：

```
我变强了      ← 代理织入的前置增强
笨蛋          ← 真实的目标方法
```

---

## 架构

四个接口各司其职，一种「定位 → 扫描 → 装配 → 编排」的职责切分：

```
App.main()
 │
 └─ StartClassImpl.start()                        编排整个流程
      │
      ├─ GetCatalogImpl.getCatalog()              ① 定位：找到 com.zhang 包所在目录
      │
      ├─ GetClassImpl.getClassList(url)           ② 扫描：递归遍历 .class 文件
      │      └─ directoryRecursion()                 转成全限定类名列表
      │
      └─ UploadClassImpl.upload(classList)        ③ 装配：容器核心
             │
             ├─ Class.forName()                     逐个加载类
             │
             ├─ getFaces()                          建立「接口名 → 实现类」映射表
             │
             └─ 遍历 @DiObj 字段
                  ├─ 反射 newInstance()             实例化实现类
                  ├─ getProxy()                     JDK 动态代理包装
                  └─ field.set()                    注入（仅支持 static 字段）
```

**AOP 织入点**在 `UploadClassImpl.getProxy()`：

```java
private Object getProxy(Class<?> aClass, final Object target) {
    return Proxy.newProxyInstance(
        ClassLoader.getSystemClassLoader(),
        new Class<?>[]{aClass},
        new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("我变强了");          // ← 前置增强
                return method.invoke(target, args);
            }
        });
}
```

---

## 快速开始

**环境要求**：JDK 21+ 、Maven 3.6+

```bash
# 1. 编译
mvn clean package

# 2. 运行
#    ⚠️ 必须在「目录 classpath」下运行，不能用 jar 直接跑，原因见「已知问题」第 1 条
java -Dstdout.encoding=UTF-8 -cp target/classes com.zhang.App
```

> `-Dstdout.encoding=UTF-8` 是让 JDK 18+ 的控制台正确输出中文，否则会看到乱码。

在 IDEA 里直接 Run `App` 也可以，效果一样。

---

## 已知问题

这部分是给自己留的账，也是这个仓库持续迭代的驱动力。

> `v0.1.1` 已修复其中三条：注入失败静默 NPE、类名提取全局替换、版本号不一致。

### 1. 打包成 JAR 后无法运行（待修复）

```
java.lang.NullPointerException: Cannot invoke "java.net.URL.toURI()" because "root" is null
    at com.zhang.function.impl.GetCatalogImpl.getFile(GetCatalogImpl.java:23)
```

**原因**：`getResource("")` 只在 classpath 条目是**目录**时有效；当 classpath 是 JAR 时，JAR 内部没有对应 `""` 的目录条目，直接返回 `null`。

**本质**：当前整套扫描策略建立在 `File.listFiles()` 之上，而它天然只能处理目录。只要还用 `File` 递归，JAR 形态就跑不起来 —— 加判空只能把异常换得更早，治标不治本。

**两条出路**：要么明确不支持 JAR 但给出清晰报错；要么按 `file` / `jar` 协议分流，JAR 走 `JarFile.entries()` 遍历。

### 2. 包名硬编码

`GetCatalogImpl` 把 `"com.zhang"` 写死在代码里，扫描范围不可配置。

### 3. 单例状态累积

`GetClassImpl` 是单例，但 `classList` 是实例字段。重复调用 `start()` 会反复追加同一批类名。

---

## 迭代计划

- [ ] 修复 JAR 形态下的 NPE，让容器支持打包运行
- [x] 注入失败时抛出带明确提示的异常
- [ ] 包扫描范围改为可配置
- [ ] 补齐单元测试
- [x] 统一版本号（pom 与 tag 对齐）

---

## 版本记录

| 版本 | 日期 | 内容 |
|---|---|---|
| `v0.1.1` | 2026-10-14 | 修复：扫描过滤非 .class 文件、类名生成跨平台、注入失败改为明确报错；补齐方法注释 |
| `v0.1` | 2026-10-13 | 初版：注解扫描 + 反射装配 + 动态代理跑通；纳入版本管理 |

---

## 说明

这是个**个人学习项目**，代码以「把原理跑通」为目标，尚未考虑健壮性与工程化。欢迎指出问题，但请勿用于生产环境。

## 许可

[MIT](https://choosealicense.com/licenses/mit/)
