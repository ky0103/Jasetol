package com.Jasetol.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class Utils {

    public static byte[] classFileChangeToBytes(String classFileName) throws Exception{
        File file = new File(classFileName);
        FileInputStream fileInputStream = new FileInputStream(file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = fileInputStream.read(bytes)) != -1){
            byteArrayOutputStream.write(bytes,0,len);
        }
        byte[] bytesData = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        fileInputStream.close();
        return bytesData;
    }

}
