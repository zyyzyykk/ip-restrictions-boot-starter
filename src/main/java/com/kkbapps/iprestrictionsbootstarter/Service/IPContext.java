package com.kkbapps.iprestrictionsbootstarter.Service;

import com.kkbapps.iprestrictionsbootstarter.Entity.IpRequestInfo;

public class IPContext {

    private static final ThreadLocal<IpRequestInfo> requestInfo = new ThreadLocal<>();


    public static void set(IpRequestInfo ipRequestInfo) {
        requestInfo.set(ipRequestInfo);
    }


    public static IpRequestInfo get() {return requestInfo.get();}


    public static void remove(){
        requestInfo.remove();
    }

}
