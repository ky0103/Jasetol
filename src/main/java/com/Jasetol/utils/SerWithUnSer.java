package com.Jasetol.utils;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.io.*;

public class SerWithUnSer {
    public static void unSerializeObject(byte[] bytes) throws Exception{  // 反序列化对象
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        objectInputStream.readObject();
        byteArrayInputStream.close();
        objectInputStream.close();
    }

    public static void unSerializeObjectFromFile() throws Exception{
        FileInputStream fileInputStream = new FileInputStream(ParseArgs.file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        objectInputStream.readObject();
        fileInputStream.close();
        objectInputStream.close();
    }

    public static void unSerialize(byte[] bytes) throws Exception{
        if (ParseArgs.file != null){
            unSerializeObjectFromFile();
        }else {
            unSerializeObject(bytes);
        }
    }

    public static byte[] serialize(Object object) throws Exception{
    if (ParseArgs.file != null){
        serializeObjectToFile(object);
        return new byte[]{};
    }else {
        byte[] bytes = serializeObject(object);
        return bytes;
    }
}

    public static void serializeObjectToFile(Object object) throws Exception{
        FileOutputStream fileOutputStream = new FileOutputStream(ParseArgs.file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(object);
        fileOutputStream.flush();
        objectOutputStream.flush();
        fileOutputStream.close();
        objectOutputStream.close();
    }

    public static byte[] serializeObject(Object object) throws Exception{  // 序列化对象
        ByteOutputStream byteOutputStream = new ByteOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
        objectOutputStream.writeObject(object);
        byte[] bytes = byteOutputStream.toByteArray();
        byteOutputStream.close();
        objectOutputStream.close();
        return bytes;
    }
}
