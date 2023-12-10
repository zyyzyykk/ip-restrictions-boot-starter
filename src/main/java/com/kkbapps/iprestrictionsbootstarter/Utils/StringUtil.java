package com.kkbapps.iprestrictionsbootstarter.Utils;

public class StringUtil {
    /**
     * 解析ip与方法
     */
    public static void parse(String key, String path, String ip)
    {
        int index = key.lastIndexOf("^");
        if(index != -1)
        {
            path = key.substring(0,index);
            ip = key.substring(index + 1);
        }
    }

}
