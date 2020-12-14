# 小白视角：整合 SpringBoot + MyBatis + Druid + log4j



> **写在前面：**
>
> ​		这篇文章是本人初次接触SpringBoot，同时也并未学习过Spring。因为项目原因要使用SpringBoot技术点，因此需要从零开始学习SpringBoot。写这篇文章的初衷一是为了自我总结，二则是后来要学习SpringBoot的新人填坑。身为一小白，我能明白一开始几乎没有接触过这些东西而又不得不上手而不知从何开始的那种困惑和迷茫。
>
> ​	那么这篇文章就是从一个小白的视角出发，希望对你有所帮助；同时因为是新手有些表述可能不会明白、有些知识点讲解不深入或者理解存在问题，还望海涵，也请不吝赐教！



## 环境信息

使用的环境如下：

+ 操作系统信息：

  ![system.png](http://ww1.sinaimg.cn/large/007eYQjmgy1glgh857a1ej30n40ixwju.jpg)

+ 开发工具信息：

  ![idea.png](http://ww1.sinaimg.cn/large/007eYQjmgy1glgh8k8ntgj30hs0b4whm.jpg)

+ JDK版本：

  corretto - JDK1.8.0_275

+ MySQL版本：

  MySQL 5.5.29

  ![database.png](http://ww1.sinaimg.cn/large/007eYQjmgy1glgi1s6ve2j30tw0jngn7.jpg)

+ Spring initializr

  ![springinit.png](http://ww1.sinaimg.cn/large/007eYQjmgy1glgh9vmq7zj30ma0kzmxk.jpg)

  ![springinit-2.png](http://ww1.sinaimg.cn/large/007eYQjmgy1glgha77rizj30ma0kygmy.jpg)

  

## 配置相关文件

+ **/pom.xml **

  添加相关依赖： mysql连接驱动、Druid连接池、Spring-mybatis、log4j

  + 视情况而定添加相关数据库类型的连接驱动，连接池如使用其它请添加对应的依赖，但不要忘记修改application.yml中 spring.datasource.type ；

  + Spring-mybatis可替换其它框架；

  + log4j 视情况而定。

  ```xml
  <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
          <dependency>
              <groupId>mysql</groupId>
              <artifactId>mysql-connector-java</artifactId>
              <version>6.0.6</version>
          </dependency>
  
  <!-- https://mvnrepository.com/artifact/com.alibaba/druid -->
          <dependency>
              <groupId>com.alibaba</groupId>
              <artifactId>druid</artifactId>
              <version>1.2.3</version>
          </dependency>
  
  <!-- https://mvnrepository.com/artifact/org.mybatis.spring.boot/mybatis-spring-boot-starter -->
          <dependency>
              <groupId>org.mybatis.spring.boot</groupId>
              <artifactId>mybatis-spring-boot-starter</artifactId>
              <version>2.1.4</version>
          </dependency>
  
  <!-- https://mvnrepository.com/artifact/log4j/log4j -->
          <dependency>
              <groupId>log4j</groupId>
              <artifactId>log4j</artifactId>
              <version>1.2.17</version>
          </dependency>
  ```

  

+ **/src/main/resources/application.yml**

  鉴于yaml格式配置文件众多的好处，为大众所推荐，本文就使用application.yml配置文件。

  ```yaml
  spring:
    datasource:
      url: jdbc:mysql://localhost:3306/database?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false&serverTimezone=UTC
      username: root
      password: root
      driver-class-name: com.mysql.cj.jdbc.Driver
      # 使用阿里 Druid 连接池
      type: com.alibaba.druid.pool.DruidDataSource
      
      # Druid 配置
      filters: stat,wall,log4j
      initialSize: 5
      minIdle: 5
      maxActive: 100
      timeBetweenEvictionRunsMillis: 60000
      minEvictableIdleTimeMillis: 300000
      validationQuery: SELECT 1 FROM DUAL
      testWhileIdle: true
      testOnBorrow: false
      testOnReturn: false
      poolPreparedStatements: true
      maxPoolPreparedStatementPerConnectionSize: 20
      useGlobalDataSourceStat: true
      connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500
  
  mybatis:
    mapper-locations: classpath:mapper/*.xml
    # 对应实体类的路径
    type-aliases-package: cn.notfound945.demo.pojo.User
    configLocation: classpath:mybatis-config.xml
  ```

  本配置文件已初步整合了 Druid 、MyBatis 的一些配置

  + 连接URL、用户名、密码等填上自己的数据；

  + 连接驱动 driver-class-name 笔者填写 com.mysql.cj.jdbc.Driver ，如果报错请填 com.mysql.jdbc.Driver ，笔者也是因为启动有WARN提示就修改成了 com.mysql.cj.jdbc.Driver ；

  + 类型 type 填写阿里 Druid 连接池 com.alibaba.druid.pool.DruidDataSource，以下就是Druid相关配置，如果使用其它连接池，请修改成其对应配置；
  + 注意 mybatis 是与 spring 同级的！ 因为 mybatis 是 MyBatis.org团队自适配 SpringBoot 开发的，不属于 Spring 下；
  + MyBatis 配置文件 configLocation 对应好 resources 目录下的 mybatis-config.xml 文件；
  + mapper-locations 填写 mapper/*.xml ，确保
  + type-aliases-package 此项值填写对应的实体类路径

+ **/src/main/resources/log4j.properties **

  这是 log4j 的配置，请根据你的情况来修改。

  ```
  # LOG4J配置
  log4j.rootCategory=INFO, stdout, file
  
  # 控制台输出
  log4j.appender.stdout=org.apache.log4j.ConsoleAppender
  log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
  log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss,SSS} %5p %c{1}:%L - %m%n
  
  # root日志输出到文件
  log4j.appender.file=org.apache.log4j.DailyRollingFileAppender
  log4j.appender.file.file=/data/logs/springboot-log4j-all.log
  log4j.appender.file.DatePattern='.'yyyy-MM-dd
  log4j.appender.file.layout=org.apache.log4j.PatternLayout
  log4j.appender.file.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss,SSS} %5p %c{1}:%L - %m%n
  
  # ERROR级别输出到特定的日志文件中
  log4j.logger.error=errorfile
  # error日志输出
  log4j.appender.errorfile=org.apache.log4j.DailyRollingFileAppender
  log4j.appender.errorfile.file=/data/logs/springboot-log4j-error.log
  log4j.appender.errorfile.DatePattern='.'yyyy-MM-dd
  log4j.appender.errorfile.Threshold = ERROR
  log4j.appender.errorfile.layout=org.apache.log4j.PatternLayout
  log4j.appender.errorfile.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss,SSS} %5p %c{1}:%L - %m%n
  ```

+ **/src/main/resources/mybatis-config.xml**

  MyBatis 的一些配置

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE configuration
          PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-config.dtd">
  <configuration>
      <settings>
          <!--开启驼峰命名规则-->
          <setting name="mapUnderscoreToCamelCase" value="true"/>
          <!--设置主键自增-->
          <setting name="useGeneratedKeys" value="true"/>
      </settings>
  </configuration>
  ```

+ **/src/main/java/cn.xxx.xxx/config/DruidConfig.java（新建 config 目录，类名可自定义）**

  Druid 登录、管理等配置。

  ```java
  import com.alibaba.druid.pool.DruidDataSource;
  import com.alibaba.druid.support.http.StatViewServlet;
  import com.alibaba.druid.support.http.WebStatFilter;
  import org.springframework.boot.context.properties.ConfigurationProperties;
  import org.springframework.boot.web.servlet.FilterRegistrationBean;
  import org.springframework.boot.web.servlet.ServletRegistrationBean;
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  
  import javax.sql.DataSource;
  import java.util.Arrays;
  import java.util.HashMap;
  import java.util.Map;
  
  @Configuration
  public class DruidConfig {
  
      @ConfigurationProperties(prefix = "spring.datasource")
      @Bean
      public DataSource DruidDataSource() {
          return new DruidDataSource();
      }
  
      @Bean
      public ServletRegistrationBean statViewServlet() {
          ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
  
          Map<String, String> initParams = new HashMap<>();
          initParams.put("loginUsername", "admin"); //后台管理界面的登录账号
          initParams.put("loginPassword", "123456"); //后台管理界面的登录密码
  
          //后台允许谁可以访问
          //initParams.put("allow", "localhost"); 表示只有本机可以访问
          //initParams.put("allow", "");为空或者为null时，表示允许所有访问
          initParams.put("allow", "");
          //deny：Druid 后台拒绝谁访问
          //initParams.put("admin", "192.168.1.20");表示禁止此ip访问
  
          //设置初始化参数
          bean.setInitParameters(initParams);
          return bean;
          //这些参数可以在 com.alibaba.druid.support.http.StatViewServlet 的父类 com.alibaba.druid.support.http.ResourceServlet 中找到
      }
  
      @Bean
      public FilterRegistrationBean webStatFilter() {
          FilterRegistrationBean bean = new FilterRegistrationBean();
          bean.setFilter(new WebStatFilter());
  
          //exclusions：设置哪些请求进行过滤排除掉，从而不进行统计
          Map<String, String> initParams = new HashMap<>();
          initParams.put("exclusions", "*.js,*.css,/druid/*");
          bean.setInitParameters(initParams);
  
          //"/*" 表示过滤所有请求
          bean.setUrlPatterns(Arrays.asList("/*"));
          return bean;
      }
  }
  
  ```

  配置完后，启动项目就可访问 localhost:8080/druid/ 输入配置文件中设置的用户名 admin 和密码 123456 即可进行 Druid 监控管理。

  ![druid.png](http://ww1.sinaimg.cn/large/007eYQjmgy1glgkou5309j311y0k3myz.jpg)

  至此，整合 SpringBoot + Druid + log4j + MyBatis 相关文件配置基本完成。



## 数据库

这是两份样例数据库文件，一份 bean_user 用户表 ，一份 bean_admin_logs 日志表，仅供参考。

+ **bean_user 数据：**

```sql
# Host: localhost  (Version: 5.5.29)
# Date: 2020-12-08 17:03:41
# Generator: MySQL-Front 5.3  (Build 4.234)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "bean_user"
#

DROP TABLE IF EXISTS `bean_user`;
CREATE TABLE `bean_user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(200) NOT NULL,
  `avatar` varchar(120) DEFAULT NULL,
  `realname` varchar(50) DEFAULT NULL,
  `authority` tinyint(2) NOT NULL COMMENT '0保留账号1普通权限2管理员3超级管理员',
  `groups` tinyint(2) DEFAULT NULL,
  `registration_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

#
# Data for table "bean_user"
#

INSERT INTO `bean_user` VALUES (1,'wdnmd','hJzz3XIWf95Io0w6H9+oYpLVUT49Tva+dwB5S9SKh/fN7DYGvzZzsfIK3UcbXA==','images/user/avatar/upload/Eriman.png','卡卡',3,3,'2018-10-14'),(2,'demo','MPbp5C4bEZ78dO+j4wmK4k+fBx3JYp3hbAb9tvVhk55eIFT6vWYeDg==','images/user/avatar/upload/2AB7C3612A0F1199A322617A2033.jpg','王者',3,3,'2018-10-14'),(3,'gg','InREeIWKZ0EZXW4601kipoCnXXGVCry2pynNMuI1eYqbsAafg/LcEQb5',NULL,'打野',2,2,'2018-10-14'),(4,'dearlord','WI3hY5Bm4eXJi/SYCifjZtjf9bRA3fNsxsgl9XTKUjj+AwI4alFGAQ==',NULL,'完了',1,1,'2018-10-14'),(5,'jankie','+c4TCX1HlMvD/TF9ul0Hf520WxkLSt3af6uGykGBQfA+epi4C5EiKA==','images/user/avatar/upload/46639afac606.jpg','喜庆',1,1,'2018-10-14'),(6,'hello','ig4ktjkrF9o2ZWoCYsASfGDV26HtK6I1YH8iPwIhgqzr5uOaei0f4YRn','images/user/avatar/upload/Weat.jpg','上单',1,1,'2018-10-14'),(7,'verd','A9sB5TCLGdfd0Xe19TjCxt/GJNOCBEMkR/hHgBUu2C+ZK+S1abulH3fR',NULL,'五佰',1,1,'2018-10-15'),(8,'admintest','4lHrcssx759crpYMcWUXkC3hdH6g/tElvD/Nqixzu+nvmrqVuwvgYCsY','images/user/avatar/upload/01(jj32).jpg','admin',2,2,'2018-11-20'),(9,'test','djsofjsdofjsdoivsdsdjHIODJODIHFsdf','image/user/avatar/dsfdjng.jpg','测试',0,1,'2020-12-08'),(10,'admin','dssdfjHODFIJSDfdsfsFDDDF98s0dfds','image/user/avatar/dsfJODF0j.png','管理员',3,2,'2020-12-08');

```

+ **bean_user 结构：**

  ![mysql.png](http://ww1.sinaimg.cn/large/007eYQjmgy1glgk61umjoj30se0irjry.jpg)

![mysql-data.png](http://ww1.sinaimg.cn/large/007eYQjmgy1glgk6p4zkrj30rx0iq3zb.jpg)

+ **bean_admin_logs 数据**

  ```sql
  # Host: localhost  (Version: 5.5.29)
  # Date: 2020-12-08 19:05:32
  # Generator: MySQL-Front 5.3  (Build 4.234)
  
  /*!40101 SET NAMES utf8 */;
  
  #
  # Structure for table "bean_admin_logs"
  #
  
  DROP TABLE IF EXISTS `bean_admin_logs`;
  CREATE TABLE `bean_admin_logs` (
    `id` bigint(10) unsigned NOT NULL AUTO_INCREMENT,
    `username` varchar(50) NOT NULL,
    `action` varchar(30) DEFAULT NULL,
    `ip_adr` varchar(30) DEFAULT NULL,
    `dotime` datetime DEFAULT NULL,
    `remarks` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=140 DEFAULT CHARSET=utf8;
  
  #
  # Data for table "bean_admin_logs"
  #
  
  INSERT INTO `bean_admin_logs` VALUES (1,'beiian','login','10.19.177.68','2018-11-20 10:21:30',NULL),(2,'jankjieo','login','16.19.4.193','2018-11-20 10:38:49',NULL),(3,'jankiej','login','3.250.201.55','2018-11-20 11:12:57',NULL),(4,'beiian','login','18.76.65.99','2018-11-20 12:09:11',NULL),(5,'beiian','login','28.76.65.99','2018-11-20 12:12:18',NULL),(6,'dearlord','login','19.39.248.1','2018-11-20 12:27:19',NULL),(7,'2g0','login','15.5.50.44','2018-11-20 12:44:39',NULL),(8,'beiian','login','223.4.130.20','2018-11-20 14:52:42',NULL),(9,'jki','login','18.6.65.9','2018-11-20 16:41:51',NULL),(10,'beiian','login','18.6.65.99','2018-11-20 16:49:12',NULL),(11,'beiian','login','18.6.65.99','2018-11-20 20:26:03',NULL),(12,'2g0','login','28.16.65.99','2018-11-20 20:26:33',NULL),(13,'janjjie','login','18.76.65.1','2018-11-20 20:29:50',NULL);
  
  ```

+ **bean_admin_logs 结构**

![log.png](http://ww1.sinaimg.cn/large/007eYQjmgy1glgnogt44gj30se0iqwew.jpg)

![log2.png](http://ww1.sinaimg.cn/large/007eYQjmgy1glgnrk381cj30rx0inmy1.jpg)

其实使用一个数据表就可以了 ，整两个数据表，主要是为了对照（下有说明）。以下就 bean_admin_logs 表为例，bean_user 表是一样的操作。



## 编写业务代码

文件配置完成、数据库也就位了，现开始编写业务代码进行测试。

> **说明：**
>
> 两表不同之如下，根据你的需求来选择：
>
> + bean_admin_log 表是通过 **Controller层 ->  Dao层** 进行数据库操作；
> + bean_user 表是经过 **Controller 层 -> Service 层 -> Dao 层** 再进行数据库操作。



### 新建相应的文件夹

+ pojo ：存放实体类
+ config：放置 Druid 等配置文件类
+ dao：数据库访问层
+ controller：控制层
+ service：服务层
+ /resources/mapper：MyBatis 映射文件



### pojo 下创建 Log 实体类（User 同）

一个简单的实体类，再简单不过了。

```java
package cn.notfound945.demo.pojo;

public class Log {
    private int id;
    private String userName;
    private String action;
    private String ipAddr;
    private String doTime;
    private String remarks;

    public Log() {
    }

    public Log(int id, String userName, String action, String ipAddr, String doTime, String remarks) {
        this.id = id;
        this.userName = userName;
        this.action = action;
        this.ipAddr = ipAddr;
        this.doTime = doTime;
        this.remarks = remarks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getDoTime() {
        return doTime;
    }

    public void setDoTime(String doTime) {
        this.doTime = doTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", action='" + action + '\'' +
                ", ipAddr='" + ipAddr + '\'' +
                ", doTime='" + doTime + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}

```

### /resources/mapper 下创建 LogMapper.xml 映射文件（User 同）

+ namespace 对应操作实体类的 Dao
+ type 对应操作实体类
+ column 对应数据库对应的列名 property 对应实体类属性名 jdbcType 对应数据库对应列的类型
+ select、delete、insert 标签中是对应的操作的 SQL 语句
+ sql 标签可视为变量
+ id 名可理解为映射的方法名，在实体类的 Dao 接口中选择性添加使用
+  parameterType 传入参数类型 #{id, jdbcType=INTEGER} 使用传入指定类型的参数

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
# 对应操作实体类的 Dao
<mapper namespace="cn.notfound945.demo.dao.LogDao">
    # 对应操作实体类
    <resultMap id="BaseResultMap" type="cn.notfound945.demo.pojo.Log">
        # column 对应数据库对应的列名 property 对应实体类属性名 jdbcType 对应数据库对应列的类型
        <result column="id" property="id" jdbcType="INTEGER"/>
        <result column="username" property="userName" jdbcType="VARCHAR" />
        <result column="action" property="action" jdbcType="VARCHAR" />
        <result column="ip_adr" property="ipAddr" jdbcType="VARCHAR" />
        <result column="dotime" property="doTime" jdbcType="VARCHAR" />
        <result column="remarks" property="remarks" jdbcType="VARCHAR" />
    </resultMap>

    
    # 类似于变量
    <sql id="Base_Column_List">
        id, username, action, ip_adr, dotime, remarks
    </sql>

    <select id="getAllLog" resultMap="BaseResultMap" parameterType="java.lang.String">
        select <include refid="Base_Column_List"/>
        from bean_admin_logs
    </select>
    # id 名可理解为映射的方法名 在实体类的 Dao 接口中选择性添加 parameterType 传入参数类型 #{id, jdbcType=INTEGER} 使用传入指定类型的参数
    <select id="getLogById" resultMap="BaseResultMap" parameterType="java.lang.Integer">
        select <include refid="Base_Column_List" />
        from bean_admin_logs
        where id = #{id, jdbcType=INTEGER}
    </select>

    <select id="getLogByUserName" resultMap="BaseResultMap" parameterType="java.lang.String">
        select
        <include refid="Base_Column_List" />
        from bean_admin_logs
        where username = #{username, jdbcType=VARCHAR}
    </select>

    <delete id="deleteLogById" parameterType="java.lang.Integer" >
        delete from bean_admin_logs
        where id = #{id,jdbcType=INTEGER}
    </delete>
</mapper>
```

### dao 下创建 Log 实体类对应 Dao 接口类（User 同）

添加对应 /resources/mapper/logMapper.xml 映射文件中的 SQL 操作（ id 名）。

```java
package cn.notfound945.demo.dao;

import cn.notfound945.demo.pojo.Log;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LogDao {
    List<Log> getAllLog();
    List<Log> getLogByUserName(String userName);
    Log getLogById(int id);
    int deleteLogById(int id);
}

```

### controller 下创建 LogController 控制类 

至此，Log 类已可取出对应数据库数据来装配对象，也可对 bean_admin_logs 数据库进行操作了。

```java
package cn.notfound945.demo.controller;

import cn.notfound945.demo.dao.LogDao;
import cn.notfound945.demo.pojo.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class LogController {

    // 自动装配注解不要落下 否则报 NullPointerException 异常
    @Autowired
    private LogDao logDao;

    @RequestMapping(value = "/log")
    public List<Log> getAllLog() {
        return logDao.getAllLog();
    }

    @RequestMapping(value = "/getlogbyusername")
    public List<Log> getLogByUserName(HttpServletRequest request) {
        String userName  = request.getParameter("username");
        return logDao.getLogByUserName(userName);
    }
    @RequestMapping(value = "/getlogbyid/{id}")
    public Log getLogById(@PathVariable("id") int id) {
        return logDao.getLogById(id);
    }
    @RequestMapping(value = "/deletelogbyid/{id}")
    public String deleteLogById(@PathVariable("id") int id) {
        int affectedNum = logDao.deleteLogById(id);
        return affectedNum > 0 ? "Delete Successful. " + affectedNum + " row(s) affected." :  "Delete Fail. " + affectedNum + " row affected.";
    }
}

```

启动后，访问以下地址：

+ localhost:8080/log  获取所有日志

+ localhost:8080/getlogbyusername?username={username}  通过用户名username获取对应日志

+ localhost:8080/getlogbyid/{id}  通过id获取对应日志

+ localhost:8080//deletelogbyid/{id} 通过id删除对应日志

  

  ![log-web.png](http://ww1.sinaimg.cn/large/007eYQjmgy1glgsxlz4uqj30pc0bit94.jpg)

现在对 bean_admin_logs 表的操作流程是 **controll 层  -> dao 层 -> mysql 数据库**。

好了，对数据库层增删查改是一样，就不一一写出。

那么，这就整合成功了，希望对你有所帮助！

如果你满足于此，现在你可以结束阅读了。





如果没有，那么继续。

-----------------------------------------------------------

### 添加 service 层

+ serice 目录下创建 UserService 接口

  添加对应 /resources/mapper/UserMapper.xml 映射文件中的 SQL 操作（ id 名）

  ```java
  package cn.notfound945.demo.service;
  
  import cn.notfound945.demo.pojo.User;
  
  import java.util.List;
  
  public interface UserService {
      User getUserById(int id);
      List<User> getAllUser();
      List<User> getUserByUserName(String userName);
      int deleteUserById(int id);
  }
  
  ```

+ 同级目录下创建 serviceImpl 目录并在 serviceImpl 目录创建 UserServiceImpl 类

+ UserServiceImpl 类实现 UserService 接口

  ```java
  package cn.notfound945.demo.service.serviceImpl;
  
  import cn.notfound945.demo.dao.UserDao;
  import cn.notfound945.demo.pojo.User;
  import cn.notfound945.demo.service.UserService;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Service;
  
  import java.util.List;
  
  @Service
  public class UserServiceImpl implements UserService {
  
      @Autowired
      private UserDao userDao;
  
      @Override
      public List<User> getUserByUserName(String userName) {
          return userDao.getUserByUserName(userName);
      }
  
      @Override
      public int deleteUserById(int id) {
          return userDao.deleteUserById(id);
      }
  
      @Override
      public User getUserById(int id) {
          return userDao.getUserById(id);
      }
  
      @Override
      public List<User> getAllUser() {
          return  userDao.getAllUser();
      }
  }
  
  ```

  

### controller 层调用

controller 下创建 UserController 类，进行 service 层调用。

```java
package cn.notfound945.demo.controller;

import cn.notfound945.demo.pojo.Log;
import cn.notfound945.demo.pojo.User;
import cn.notfound945.demo.service.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class UserController {
    // 自动装配注解不能丢
    @Autowired
    private UserServiceImpl userService;

    @RequestMapping(value = "/user")
    public List<User> getAllUser() {
        return userService.getAllUser();
    }

    @RequestMapping(value = "/getuserbyusername")
    public List<User> getLogByUserName(HttpServletRequest request) {
        String userName  = request.getParameter("username");
        return userService.getUserByUserName(userName);
    }
    @RequestMapping(value = "/getuserbyid/{id}")
    public User getUserById(@PathVariable("id") int id) {
        return userService.getUserById(id);
    }
    @RequestMapping(value = "/deleteuserbyid/{id}")
    public String deleteUserById(@PathVariable("id") int id) {
        int affectedNum = userService.deleteUserById(id);
        return affectedNum > 0 ? "Delete Successful. " + affectedNum + " row(s) affected." :  "Delete Fail. " + affectedNum + " row affected.";
    }

}
```

也不是很多，完毕！

启动后，访问以下地址：

+ localhost:8080/user  获取所有用户

+ localhost:8080/getuserbyusername?username={username}  通过用户名username获取对应用户

+ localhost:8080/getuserbyid/{id}  通过id获取对应用户

+ localhost:8080//deleteuserbyid/{id} 通过id删除对应用户

  

  ![user.png](http://ww1.sinaimg.cn/large/007eYQjmgy1glgtbacx5cj311q0csaax.jpg)

好了，现在对 bean_user 表的操作流程是 **controll 层 -> service 层 -> dao 层 -> mysql 数据库**。

此项目文件目录结构如下：

![index-1.png](http://ww1.sinaimg.cn/large/007eYQjmgy1glgth1sl1dj311y0l9adr.jpg)

![index-2.png](http://ww1.sinaimg.cn/large/007eYQjmgy1glgtha2lhxj311y0l6n10.jpg)
