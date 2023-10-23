# ip-restrictions-boot-starter
> springboot项目中实现请求IP自动限制与拦截
>
> 作者：zyyzyykk
>
> 网站：http://git.kkbapps.com/kk/ip-restrictions-boot-starter
>
> 更新时间：2023-10-23

### 使用

1.在Springboot项目中引入依赖：

```xml
<dependency>
    <groupId>com.kkbapps</groupId>
    <artifactId>ip-restrictions-boot-starter</artifactId>
    <!-- 建议引入最新RELEASE版本 -->
    <version>1.0.5-RELEASE</version>
</dependency>
```

2.在需要限制的方法上添加 **@EnableIPLimit** 注解

```java
@EnableIPLimit(count = 100, interval = 1000)
public String demo(String param) {
```

- 注解参数说明：
  - count：监控周期内运行同一IP最大访问次数
  - interval：监控周期内同一IP两次访问最小间隔时间（毫秒）
  - 不添加注解参数配置则取决于application.properties中的配置、默认值

3.在 application.properties 中配置：

```properties
# kkbapps.ip开头，其他配置见提示
kkbapps.ip.forbid-ip=true	# 是否封禁超出限制的ip
```

### 注意

1.拦截采用AOP实现，**@EnableIPLimit** 注解建议添加在 Controller层、Service层的方法上

### 更新记录

##### 1.0.5-RELEASE：修复监控时间bug

更新了监控时间最小单位为天，默认监控周期为一天，取值范围（1-30）

##### 1.0.4-RELEASE：

更新了监控时间最小单位为天，默认监控周期为一天，取值范围（1-30）**（监控时间bug）**

##### 1.0.3-RELEASE：

更新了监控时间最小单位为分钟，默认监控周期为一天**（监控时间bug）**

##### 1.0.2-RELEASE：

第一个基础版本

##### 1.0.0-RELEASE：

发布中央仓库测试版
