package com.kkbapps.iprestrictionsbootstarter.Config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component("settingsConfig")
@ConfigurationProperties(prefix = "kkbapps.ip")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettingsConfig {

    /**
     * ip获取方式（是否有nginx代理）
     */
    private boolean nginxProxy = true;

    /**
     * 监控周期（秒）
     */
    private Long cycle = 24L * 60 * 60;

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
