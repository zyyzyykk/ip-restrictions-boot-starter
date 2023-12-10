package com.kkbapps.iprestrictionsbootstarter.Service;


import com.kkbapps.iprestrictionsbootstarter.Annotation.EnableIPLimit;
import com.kkbapps.iprestrictionsbootstarter.Config.SettingsConfig;
import com.kkbapps.iprestrictionsbootstarter.Entity.IpRequestInfo;
import com.kkbapps.iprestrictionsbootstarter.Enum.IpRequestErrorEnum;
import com.kkbapps.iprestrictionsbootstarter.Exception.IpRequestErrorException;
import com.kkbapps.iprestrictionsbootstarter.Utils.NetUtil;
import com.kkbapps.iprestrictionsbootstarter.Utils.StringUtil;
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


    public static Integer tag = 0;

    public static ConcurrentHashMap<String,IpRequestInfo> IPRequestMap = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String,Date> ForbidIPMap = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String,String> BlackIPMap = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String,String> WhiteIPMap = new ConcurrentHashMap<>();


    /**
     * 初始化
     */
    private void init()
    {
        // 初始化黑白名单
        if(settingsConfig.getIpBlackList() != null) {
            log.info("初始化ip黑名单");
            for(int i=0;i<settingsConfig.getIpBlackList().length;i++) {
                String ip = settingsConfig.getIpBlackList()[i];
                BlackIPMap.put(ip,ip);
            }
            log.info("ip黑名单初始化完毕");
        }
        if(settingsConfig.getIpWhiteList() != null) {
            log.info("初始化ip白名单");
            for(int i=0;i<settingsConfig.getIpWhiteList().length;i++) {
                String ip = settingsConfig.getIpWhiteList()[i];
                WhiteIPMap.put(ip,ip);
            }
            log.info("ip白名单初始化完毕");
        }

        // 修改标志
        tag = 1;
    }


    /**
     * 进行IP校验
     */
    public void ipVerification(Method method, EnableIPLimit enableIpLimit) {

        // 初始化
        if(tag.equals(0))
            init();

        IpRequestErrorException ipRequestErrorException = null;

        // 获取当前访问的ip
        String ip = NetUtil.getIpAddress(httpServletRequest);

        // 记录访问日志
        printRequestLog(method, ip);

        // 访问方法全路径
        String path = method.getDeclaringClass() + "." + method.getName() + "^";

        // 查找黑白名单
        if(WhiteIPMap.get(ip) != null) return;
        if(BlackIPMap.get(ip) != null) {
            ipRequestErrorException = new IpRequestErrorException("ip：" + ip + " 已被加入黑名单");
            ipRequestErrorException.setIp(ip);
            ipRequestErrorException.setIpRequestErrorEnum(IpRequestErrorEnum.BLACK_LIST_IP);
            throw ipRequestErrorException;
        }

        // 判断ip是否已被封禁
        Date forbidDate = ForbidIPMap.get(path + ip);
        if(forbidDate != null) {
            ipRequestErrorException = new IpRequestErrorException("ip：" + ip + " 已被封禁");
            ipRequestErrorException.setIp(ip);
            ipRequestErrorException.setIpRequestErrorEnum(IpRequestErrorEnum.FORBID_IP);
            throw ipRequestErrorException;
        }

        // 获取当前ip在周期内已访问信息
        IpRequestInfo ipRequestInfo = IPRequestMap.get(path + ip);

        // 周期内第一次访问
        if(ipRequestInfo == null) {
            ipRequestInfo = new IpRequestInfo();
            ipRequestInfo.setIp(ip);
            ipRequestInfo.setCount(1);
            ipRequestInfo.setLastDate(new Date());
            IPRequestMap.put(path + ip, ipRequestInfo);
            IPContext.set(ipRequestInfo);
            return;
        }

        // 超出允许访问次数限制
        ipRequestInfo.setCount(ipRequestInfo.getCount() + 1);
        Long requestCountLimit = enableIpLimit.count() > 0 ?
                enableIpLimit.count() : settingsConfig.getRequestCountLimit();
        if(ipRequestInfo.getCount() > requestCountLimit) {
            if(settingsConfig.isForbidIp()) ForbidIPMap.put(path + ip, new Date());
            ipRequestErrorException = new IpRequestErrorException("ip：" + ip + " 超出访问次数限制");
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
            if(settingsConfig.isForbidIp()) ForbidIPMap.put(path + ip, new Date());
            ipRequestErrorException = new IpRequestErrorException("ip：" + ip + " 超出访问频率限制");
            ipRequestErrorException.setIp(ip);
            ipRequestErrorException.setIpRequestErrorEnum(IpRequestErrorEnum.REQUEST_INTERVAL_EXCEEDED);
            throw ipRequestErrorException;
        }

        IPContext.set(ipRequestInfo);
    }

    /**
     * 定时清除与输出ip信息
     */
    @Scheduled(cron = "0 0 0 */#{kkbappsIPLimitSettingsConfig.cycle} *  ?")
    private void reset(){

        // 输出信息
        Date now_date = new Date();
        log.info("=================周期内访问ip信息=================");
        log.info("当前时间：" + dateFormat.format(now_date));
        IPRequestMap.forEach((key, ipRequestInfo) -> {
            String path = "";
            String ip = "";
            StringUtil.parse(key,path,ip);
            log.info("ip：" + ip);
            log.info("访问方法：" + path);
            log.info("访问次数：" + ipRequestInfo.getCount() + "次");
            log.info("最后访问时间：" + dateFormat.format(ipRequestInfo.getLastDate()));
            log.info("-----------------------------------------------");
        });
        log.info("===============================================");
        log.info("=================周期内封禁ip信息=================");
        log.info("当前时间：" + dateFormat.format(now_date));
        ForbidIPMap.forEach((key, date) -> {
            String path = "";
            String ip = "";
            StringUtil.parse(key,path,ip);
            log.info("封禁ip：" + ip);
            log.info("封禁方法：" + path);
            log.info("-----------------------------------------------");
        });
        log.info("===============================================");

        // 重置
        IPRequestMap.clear();
        if(!settingsConfig.isForbidIpForever()) ForbidIPMap.clear();

    }

    /**
     * 记录ip访问日志
     */
    private void printRequestLog(Method method, String ip) {
        log.info("=================ip访问记录=================");
        log.info("访问时间：" + dateFormat.format(new Date()));
        log.info("访问ip：" + ip);
        log.info("访问路径：" + httpServletRequest.getRequestURL().toString());
        log.info("访问方法：" + method.getDeclaringClass() + "." + method.getName());
        log.info("==========================================");
    }

}
