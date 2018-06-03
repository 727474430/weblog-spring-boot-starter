### A custom spring boot starter project.

​    Primary provides the function of interface request log information printing,
which simplifies the need to write an interface request log function for each project.



### How to use.

1. git clone https://github.com/727474430/weblog-spring-boot-starter.git

2. cd weblog-spring-boot-starter

3. runing: mvn install

4. Install the project to your local repository

5. Introducing dependencies in SpringBoot project that nedd to be used

   ```xml
   <dependency>
       <groupId>com.raindrop</groupId>
       <artifactId>weblog-spring-boot-starter</artifactId>
       <version>1.0-SNAPSHOT</version>
   </dependency>
   
   ```

6. Add the following attributes in the application.properties/application.yml

   * web.log.enable=true 

     Whether to open the log printing function. default is false,not open.  select true is open

   * web.log.mapping-path=/*

     Need to intercept the path.  /* indicates all. default /*

   * web.log.exclude-mapping-path=/views/;/icon/

     Need to exclude the path，Use ”;“ split multiple paths

   * web.log.print-header

     Need to printing request header，Use ";" split multiple headers

### Example.

```yaml
web.log:
  mapping-path: /*
  printHeader: host
  enable: true
  exclude-mapping-path: /generator/;/index.ftl
```