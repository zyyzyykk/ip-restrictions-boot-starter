package com.kkbapps.iprestrictionsbootstarter.Enum;

public enum IpRequestErrorEnum {

    REQUEST_COUNT_EXCEEDED(1,"访问次数超出限制"),

    REQUEST_INTERVAL_EXCEEDED(2,"访问频率超出限制"),

    FORBID_IP(3,"当前ip已被封禁"),

    BLACK_LIST_IP(4,"当前ip已被加入黑名单");
    private Integer state;
    private String desc;

    IpRequestErrorEnum(Integer state, String desc) {
        this.state = state;
        this.desc = desc;
    }

    public static IpRequestErrorEnum getByState(Integer state) {
        for (IpRequestErrorEnum item : IpRequestErrorEnum.values()) {
            if (item.getState().equals(state)) {
                return item;
            }
        }
        return null;
    }

    public Integer getState() {
        return state;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
