package com.kkbapps.iprestrictionsbootstarter.Exception;

import com.kkbapps.iprestrictionsbootstarter.Enum.IpRequestErrorEnum;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class IpRequestErrorException extends RuntimeException{

    private String ip;

    private IpRequestErrorEnum ipRequestErrorEnum;

    public IpRequestErrorException(String message){
        super(message);
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public IpRequestErrorEnum getIpRequestErrorEnum() {
        return ipRequestErrorEnum;
    }

    public void setIpRequestErrorEnum(IpRequestErrorEnum ipRequestErrorEnum) {
        this.ipRequestErrorEnum = ipRequestErrorEnum;
    }
}
