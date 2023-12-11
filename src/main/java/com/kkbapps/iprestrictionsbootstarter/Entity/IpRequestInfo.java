package com.kkbapps.iprestrictionsbootstarter.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IpRequestInfo {

    /**
     * ip
     */
    private String ip;

    /**
     * 周期内访问次数
     */
    private Integer count;

    /**
     * 最后一次的访问时间
     */
    private Date lastDate;

}
