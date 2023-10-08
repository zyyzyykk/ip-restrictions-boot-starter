package com.kkbapps.iprestrictionsbootstarter.Config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component(value = "kkbappsIPLimitSettingsConfig")
@ConfigurationProperties(prefix = "kkbapps.ip")
public class SettingsConfig {

    /**
     * ip获取方式（是否有nginx代理）
     */
    private boolean nginxProxy = true;

    /**
     * 监控周期（分钟）
     */
    private Long cycle = 1440L;

    /**
     * 周期内访问次数限制
     */
    private Long requestCountLimit = 5L;

    /**
     * 周期内访问间隔限制（毫秒）
     */
    private Long requestIntervalLimit = 6000L;


    /**
     * 是否封禁IP
     */
    private boolean forbidIp = false;


    /**
     * 封禁IP是否为永久
     */
    private boolean forbidIpForever = false;

}
