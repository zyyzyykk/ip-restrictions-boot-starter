package com.kkbapps.iprestrictionsbootstarter.Service;


import com.kkbapps.iprestrictionsbootstarter.Annotation.EnableIPLimit;
import com.kkbapps.iprestrictionsbootstarter.Config.SettingsConfig;
import com.kkbapps.iprestrictionsbootstarter.Entity.IpRequestInfo;
import com.kkbapps.iprestrictionsbootstarter.Enum.IpRequestErrorEnum;
import com.kkbapps.iprestrictionsbootstarter.Exception.IpRequestErrorException;
import com.kkbapps.iprestrictionsbootstarter.Utils.NetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class IpHandleService {

    @Autowired
    private SettingsConfig settingsConfig;

    @Autowired
    private HttpServletRequest httpServletRequest;

    // 创建日期格式化对象，指定格式
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static ConcurrentHashMap<String,IpRequestInfo> IPRequestMap = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String,Date> ForbidIPMap = new ConcurrentHashMap<>();

    /**
     * 进行IP校验
     */
    public void ipVerification(Method method, EnableIPLimit enableIpLimit) {
        IpRequestErrorException ipRequestErrorException = null;

        // 获取当前访问的ip
        String ip = NetUtil.getIpAddress(httpServletRequest);

        // 记录访问日志
        printRequestLog(method, ip);

        // todo 查找黑白名单


        // 判断ip是否已被封禁
        Date forbidDate = ForbidIPMap.get(ip);
        if(forbidDate != null) {
            ipRequestErrorException = new IpRequestErrorException("ip：" + ip + "已被封禁");
            ipRequestErrorException.setIp(ip);
            ipRequestErrorException.setIpRequestErrorEnum(IpRequestErrorEnum.FORBID_IP);
            throw ipRequestErrorException;
        }

        // 获取当前ip在周期内已访问的次数
        IpRequestInfo ipRequestInfo = IPRequestMap.get(ip);

        // 周期内第一次访问
        if(ipRequestInfo == null) {
            IPRequestMap.put(ip,new IpRequestInfo(1,new Date()));
            return;
        }

        // 超出允许访问次数限制
        ipRequestInfo.setCount(ipRequestInfo.getCount() + 1);
        Long requestCountLimit = enableIpLimit.count() > 0 ?
                enableIpLimit.count() : settingsConfig.getRequestCountLimit();
        if(ipRequestInfo.getCount() > requestCountLimit) {
            if(settingsConfig.isForbidIp()) ForbidIPMap.put(ip,new Date());
            ipRequestErrorException = new IpRequestErrorException("ip：" + ip + "超出访问次数限制");
            ipRequestErrorException.setIp(ip);
            ipRequestErrorException.setIpRequestErrorEnum(IpRequestErrorEnum.REQUEST_COUNT_EXCEEDED);
            throw ipRequestErrorException;
        }

        // 超出允许访问频率限制
        Date nowTime = new Date();
        Date lastTime = ipRequestInfo.getLastDate();
        ipRequestInfo.setLastDate(nowTime);
        Long requestIntervalLimit = enableIpLimit.interval() >= 0 ?
                enableIpLimit.interval() : settingsConfig.getRequestIntervalLimit();
        if(nowTime.getTime() - lastTime.getTime() < requestIntervalLimit) {
            if(settingsConfig.isForbidIp()) ForbidIPMap.put(ip,new Date());
            ipRequestErrorException = new IpRequestErrorException("ip：" + ip + "超出访问频率限制");
            ipRequestErrorException.setIp(ip);
            ipRequestErrorException.setIpRequestErrorEnum(IpRequestErrorEnum.REQUEST_INTERVAL_EXCEEDED);
            throw ipRequestErrorException;
        }


    }

    /**
     * 定时清除与输出ip信息
     */
    @Scheduled(cron = "0 0 0 */#{kkbappsIPLimitSettingsConfig.cycle} *  ?")
    public void reset(){

        // 输出信息
        Date now_date = new Date();
        log.info("=================周期内访问ip信息=================");
        log.info("当前时间：" + dateFormat.format(now_date));
        IPRequestMap.forEach((ip, ipRequestInfo) -> {
            log.info("ip " + ip + "：" + ipRequestInfo.getCount() + "次 " + dateFormat.format(ipRequestInfo.getLastDate()));
        });
        log.info("===============================================");
        log.info("=================周期内封禁ip信息=================");
        log.info("当前时间：" + dateFormat.format(now_date));
        ForbidIPMap.forEach((ip, date) -> {
            log.info("ip " + ip + "：" + dateFormat.format(date));
        });
        log.info("===============================================");

        // 重置
        IPRequestMap.clear();
        if(!settingsConfig.isForbidIpForever()) ForbidIPMap.clear();

    }

    /**
     * 记录ip访问日志
     */
    public void printRequestLog(Method method, String ip) {
        log.info("=================ip访问记录=================");
        log.info("访问时间：" + dateFormat.format(new Date()));
        log.info("访问ip：" + ip);
        log.info("访问路径：" + httpServletRequest.getRequestURL().toString());
        log.info("访问方法：" + method.getDeclaringClass());
        log.info("==========================================");
    }

}
