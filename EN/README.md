# ip-restrictions-boot-starter
> Ip-restrictions-boot-starter is a Maven dependency package based on SpringBoot, which can restrict  and intercept requests for specific methods and IPs.
>
> Author: [zyyzyykk](https://github.com/zyyzyykk)
>
> Source Code: https://github.com/zyyzyykk/ip-restrictions-boot-starter
>
> Maven Repository Address: https://mvnrepository.com/artifact/com.kkbapps/ip-restrictions-boot-starter
>
> Update Time: 2024-02-27

<p align="center"><a href="https://mvnrepository.com/artifact/com.kkbapps/ip-restrictions-boot-starter" target="_blank" rel="noopener noreferrer"><img width="100" src="https://img.kkbapps.com/logo/ip-restrictions-boot-starter.png" alt="ip-restrictions-boot-starter logo"></a></p>

<p align="center">
  <a href="https://mvnrepository.com/artifact/com.kkbapps/ip-restrictions-boot-starter"><img src="https://img.shields.io/maven-central/v/com.kkbapps/ip-restrictions-boot-starter"></a>
  <a href="https://www.oracle.com/cn/java/technologies/downloads/#java8-windows"><img src="https://img.shields.io/badge/jdk-1.8-orange" alt="JDK Version"></a>
  <a href="https://spring.io/projects/spring-boot"><img src="https://img.shields.io/badge/springboot-2.7.16-green?color=6db33f" alt="SpringBoot Version"></a>
  <a href="https://opensource.org/license/MIT"><img src="https://img.shields.io/badge/licence-MIT-red" alt="MIT Licence"></a>
  <a href="https://github.com/zyyzyykk/ip-restrictions-boot-starter"><img src="https://img.shields.io/github/stars/zyyzyykk/ip-restrictions-boot-starter" alt="GitHub"></a>
</p>
<p align="center"><a href="../README.md" >简体中文</a> ｜ English</p>


### 💪 Use

1.Importing dependencies in the Springboot project:

```xml
<!-- https://mvnrepository.com/artifact/com.kkbapps/ip-restrictions-boot-starter -->
<dependency>
    <groupId>com.kkbapps</groupId>
    <artifactId>ip-restrictions-boot-starter</artifactId>
    <!-- suggest importing the latest version of RELEASE -->
    <version>1.1.4-RELEASE</version>
</dependency>
```

2.Add  **@EnableIPLimit** Annotation on methods that require request restriction

```java
@EnableIPLimit(count = 100, interval = 1000)
public String needIPLimit(String param) {
```

- Annotation parameter description:
  - count: Maximum number of visits to the same IP during the monitoring period
  - interval: The minimum interval time (in milliseconds) between two visits to the same IP during the monitoring period
  - If no annotation parameters are added, the configuration depends on the custom configuration and default values in `application.properties`

3.Custom configuration properties can be made in `application.properties`:

```properties
# related configurations start with kkbapps.ip, see prompts for other configurations
kkbapps.ip.forbid-ip=true	# whether to ban IPs that exceed the limit, valid within the default monitoring period
```

4.Configure IP blacklist and whitelist: **version 1.1.2 and above**

- Configure in `.properties` file:

  ```properties
  # Configure IP blacklist
  kkbapps.ip.ip-black-list[0]=192.168.42.1
  kkbapps.ip.ip-black-list[1]=192.168.42.2
  ```

- Configure in `.yml` file:

  ```yml
  # Configure IP blacklist
  kkbapps:
    ip:
      ip-black-list:
        - 192.168.42.1
        - 192.168.42.2
  ```

5.Obtain IP request information: **version 1.1.3 and above**

```java
@EnableIPLimit(count = 100, interval = 1000)
public String needIPLimit() {
    // obtain IP request information
    IpRequestInfo ipRequestInfo = IPContext.get();
    // request IP: 192.168.42.1
    System.out.println(ipRequestInfo.getIp());
    // number of visits within IP cycle: 5
    System.out.println(ipRequestInfo.getCount());
    // latest IP access time: Mon Dec 11 19:27:39 CST 2023
    System.out.println(ipRequestInfo.getLastDate());
}
```

### 💡 Description

1.`ip-restrictions-boot-starter` is a Maven dependency package based on the SpringBoot framework, which can restrict and intercept requests for specific methods and IP addresses

2.Intercepting is implemented using AOP, and it is recommended to add the **@EnableIPLimit** Annotation to the methods in the `Controller` layer

3.When the request exceeds the set limit, an **IpRequestErrorException** will be thrown, which can be custom captured

### 👨‍💻 Update Records

##### 1.1.4-RELEASE:latest

Add the **@Order(1)** Annotation in the AOP class to ensure interception is at the outermost layer

##### 1.1.3-RELEASE:

Add a method with **@EnableIPLimit** Annotation to retrieve the IP request information for this time

##### 1.1.2-RELEASE:

New IP blacklist and whitelist configuration: Blacklist IPs reject requests directly, while whitelist IPs are released directly

##### [History Update Records](./UPDATE.md)

### 🏘️ About this project

Author: [zyyzyykk](https://github.com/zyyzyykk/)

Welcome to provide valuable opinions or suggestions on this project, and you can also join us in maintaining and developing this project together
