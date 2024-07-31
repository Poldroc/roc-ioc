package com.github.poldroc.ioc.util;

import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    public static String getFileContent(InputStream inputStream) {
        return getFileContent(inputStream, "UTF-8");
    }

    public static String getFileContent(InputStream inputStream, String charset) {
        try {
            int size = inputStream.available();
            byte[] bytes = new byte[size];
            inputStream.read(bytes);
            inputStream.close();
            String jsonText = new String(bytes, charset);
            return jsonText;
        } catch (IOException var6) {
            IOException e = var6;
            throw new RuntimeException(e);
        }
    }
}
