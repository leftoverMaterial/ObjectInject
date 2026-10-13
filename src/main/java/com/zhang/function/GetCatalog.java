package com.zhang.function;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public interface GetCatalog {
    URL getCatalog() throws Exception;

    File getFile() throws Exception;
}
