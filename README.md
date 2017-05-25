# ace-code

基于SpringBoot的简单业务编码生成器，如选配单号：CPC201705250000001 <br/>
编码规则存储在关系型数据库中，通过mybatis的访问，最新编码及其规则缓存到redis中统一管理。

# 使用手册
## maven依赖
```
<dependency>
    <groupId>com.github.lidiliang</groupId>
    <artifactId>ace-code</artifactId>
    <version>0.0.1</version>
</dependency>
```
## 启用编码生成器
Application类上的注解@ComponentScan中加入"com.ace.code.service"

# ace-cache相关
为什么使用ace-cache? 因为ace-cache是基于redis轻量实现的，而且拥有简单管理界面。

1、配置redis数据源，application.yml文件 <br/>
```
redis:
    pool:
         maxActive: 300
         maxIdle: 100
         maxWait: 1000
    host: 127.0.0.1
    port: 6379
    password:
    timeout: 2000
    # 服务或应用名
    sysname: ace
    enable: true
    database: 0
```
2、关于ace-cache的更多内容，请移步到 https://github.com/wxiaoqi/ace-cache

# 项目中引用
1、初始化编码规则
```
to be finished
```

 
2、依赖注入：
```
@Autowired 
private ICodeManagerService codeManagerService;
```
3、使用方法：
```
业务编码划分为两部分：编码前缀（包括固定标识、日期等内容）、流水号。
流水号的递增由redis管理。
 
方法1：
String code = codeManagerService.getCode("PCM_CHOICE_LIST_HW", "CHOICE_CODE");
  
  
方法2：
String code = codeManagerService.getCode("PCM_CHOICE_LIST_HW", "CONFIG_CODE", new CodeHandler() {

    @Override
    public String getPrefix() {
        return "TEST.CODE."; // 自定义生成编码前缀，适用于编码依赖于其它业务结果的复杂情况
    }
});
```
