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
     * 监控周期（天）
     */
    private Long cycle = 1L;

    /**
     * 周期内访问次数限制
     */
    private Long requestCountLimit = 240L;

    /**
     * 周期内访问间隔限制（毫秒）
     */
    private Long requestIntervalLimit = 300000L;


    /**
     * 超出限制后是否封禁IP（默认不封禁）
     */
    private boolean forbidIp = false;


    /**
     * 是否永久封禁超出限制的IP（默认周期内封禁）
     */
    private boolean forbidIpForever = false;

}
