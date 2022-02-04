package com.Jasetol.utils;

public class ParseArgs {
    public static String cmd;
    public static String file;
    public static String url;
    public static String cmdArgFormat = "-cmd";
    public static String fileArgFormat = "-file";
    public static String urlArgFormat = "-url";
    public static void parseArgs(String[] args) throws Exception{
        if (args != null){
            if (args.length >= 2){
                if (args[0].equals(cmdArgFormat)){
                    cmd = args[1];
                    System.out.println(cmd);
                }else if (args[0].equals(urlArgFormat)){
                    url = args[1];
                    System.out.println(url);
                }else {
                    System.out.println("The first parameter is wrong");
                }
            }if (args.length >= 4){
                if (args[2].equals(fileArgFormat)){
                    file = args[3];
                    System.out.println("2. 序列化数据保存在 "+file+" 文件");
                }else {
                    System.out.println("Error in second argument");
                }
            }
        }else {
            System.out.println("Please enter parameters");
        }
    }
}
