package com.zhang.function;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public interface GetCatalog {
    /**
     * 定位被扫描包（com.zhang）在 classpath 上的目录。
     *
     * @return 包目录的 URL，例如 .../target/classes/com/zhang
     */
    URL getCatalog() throws Exception;

    /**
     * 定位 classpath 根目录，作为后续剥离路径前缀得到类名的基准。
     *
     * @return classpath 根目录
     */
    File getFile() throws Exception;
}
