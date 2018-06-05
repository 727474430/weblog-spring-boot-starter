### 一个自定义spring boot starter项目

​    主要提供了接口请求日志信息打印的功能，简化了为每个项目编写接口请求日志功能的需要。



### 如何使用.

1. **git clone <https://github.com/727474430/weblog-spring-boot-starter.git>** 

2. **cd weblog-spring-boot-starter** 

3. 运行: **mvn install**

4. 安装项目到本地仓库

5. 在需要使用的SpringBoot项目中引入依赖关系 

   ```xml
   <dependency>
       <groupId>com.raindrop</groupId>
       <artifactId>weblog-spring-boot-starter</artifactId>
       <version>1.0-SNAPSHOT</version>
   </dependency>
   ```

   

6. 在**application.properties/application.yml**文件中添加下列属性

   * web.log.enable=true 

     是否开启日志打印功能，默认为**"false"**不开启，选择true开启。

   * web.log.mapping-path=/* 

     需要拦截的路径，**/*** 表示全部路径，默认为 **/***

   * web.log.exclude-mapping-path=/views/;/icon/ 

     需要排除的路径，多个使用 **";"** 分割

   * web.log.print-header 

     需要打印的请求头，多个使用 **";"** 分割

   

### 示例.

```yaml
web.log:
  mapping-path: /*
  printHeader: host
  enable: true
  exclude-mapping-path: /generator/;/index.ftl
```





