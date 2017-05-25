package com.ace.code.config;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * @author Tingpeng.Huang
 * @date 2017/3/3
 * @since 1.7
 */
@Configuration
@EnableTransactionManagement
public class PcmMybatisConfiguration implements EnvironmentAware {
    private RelaxedPropertyResolver propertyResolver;
    private String driveClassName;
    private String url;
    private String userName;
    private String password;
    private String xmlLocation;
    private String typeAliasesPackage;
    //新增TypeHandlers包扫描
    private String typeHandlersPackage;
    private String filters;
    private String maxActive;
    private String initialSize;
    private String maxWait;
    private String minIdle;
    private String timeBetweenEvictionRunsMillis;
    private String minEvictableIdleTimeMillis;
    private String validationQuery;
    private String testWhileIdle;
    private String testOnBorrow;
    private String testOnReturn;
    private String poolPreparedStatements;
    private String maxOpenPreparedStatements;
//    private String isPerformanceInterceptorActive;

    public PcmMybatisConfiguration() {
    }

    @Bean({"csbDataSource"})
    @Primary
    public DataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(this.url);
        druidDataSource.setUsername(this.userName);
        druidDataSource.setPassword(this.password);
        druidDataSource.setDriverClassName(StringUtils.isNotBlank(this.driveClassName)?this.driveClassName:"com.mysql.jdbc.Driver");
        druidDataSource.setMaxActive(StringUtils.isNotBlank(this.maxActive)?Integer.parseInt(this.maxActive):10);
        druidDataSource.setInitialSize(StringUtils.isNotBlank(this.initialSize)?Integer.parseInt(this.initialSize):1);
        druidDataSource.setMaxWait(StringUtils.isNotBlank(this.maxWait)?(long)Integer.parseInt(this.maxWait):60000L);
        druidDataSource.setMinIdle(StringUtils.isNotBlank(this.minIdle)?Integer.parseInt(this.minIdle):3);
        druidDataSource.setTimeBetweenEvictionRunsMillis(StringUtils.isNotBlank(this.timeBetweenEvictionRunsMillis)?(long)Integer.parseInt(this.timeBetweenEvictionRunsMillis):60000L);
        druidDataSource.setMinEvictableIdleTimeMillis(StringUtils.isNotBlank(this.minEvictableIdleTimeMillis)?(long)Integer.parseInt(this.minEvictableIdleTimeMillis):300000L);
        druidDataSource.setValidationQuery(StringUtils.isNotBlank(this.validationQuery)?this.validationQuery:"SELECT 1 FROM DUAL");
        druidDataSource.setTestWhileIdle(StringUtils.isNotBlank(this.testWhileIdle)?Boolean.parseBoolean(this.testWhileIdle):true);
        druidDataSource.setTestOnBorrow(StringUtils.isNotBlank(this.testOnBorrow)?Boolean.parseBoolean(this.testOnBorrow):false);
        druidDataSource.setTestOnReturn(StringUtils.isNotBlank(this.testOnReturn)?Boolean.parseBoolean(this.testOnReturn):false);
        druidDataSource.setPoolPreparedStatements(StringUtils.isNotBlank(this.poolPreparedStatements)?Boolean.parseBoolean(this.poolPreparedStatements):true);
        druidDataSource.setMaxOpenPreparedStatements(StringUtils.isNotBlank(this.maxOpenPreparedStatements)?Integer.parseInt(this.maxOpenPreparedStatements):20);

        try {
            druidDataSource.setFilters(StringUtils.isNotBlank(this.filters)?this.filters:"stat, wall");
        } catch (SQLException var3) {
            var3.printStackTrace();
        }

        return druidDataSource;
    }

    @Bean(name = {"csbSqlSessionFactory"})
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("csbDataSource") DataSource dataSource) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        if(StringUtils.isNotBlank(this.typeAliasesPackage)) {
            bean.setTypeAliasesPackage(this.typeAliasesPackage);
        }
        if(StringUtils.isNotBlank(this.typeHandlersPackage)) {
            bean.setTypeHandlersPackage(this.typeHandlersPackage);
        }
//        StringArrayTypeHandler stringArrayTypeHandler = new StringArrayTypeHandler();
//        StringListTypeHandler stringListTypeHandler = new StringListTypeHandler();
//        bean.setTypeHandlers(new TypeHandler[]{stringArrayTypeHandler, stringListTypeHandler});

        //参见配置：http://git.oschina.net/free/Mybatis_PageHelper/wikis/HowToUse
//        PageHelper pageHelper = new PageHelper();
//        Properties properties = new Properties();
//        properties.setProperty("dialect", "oracle");
//        properties.setProperty("offsetAsPageNum", "true");
//        properties.setProperty("rowBoundsWithCount", "true");
//        properties.setProperty("pageSizeZero", "true");
//        properties.setProperty("reasonable", "true");
//        properties.setProperty("supportMethodsArguments", "true");
//        properties.setProperty("returnPageInfo", "check");
//        properties.setProperty("params", "pageNum=page;pageSizeZero=zero;reasonable=heli;count=contsql");
//        pageHelper.setProperties(properties);
        //一定要把PerformanceInterceptor拦截器配置在第一个，否则其他需要修改Sql的拦截器会对该拦截器获取Sql部分产生影响
        //默认正式环境下不开启此SQL拦截
//        List<Interceptor> interceptors = new ArrayList<Interceptor>();
//        interceptors.add(pageHelper);
//        if(StringUtils.isNotBlank(this.isPerformanceInterceptorActive)?Boolean.parseBoolean(this.isPerformanceInterceptorActive):false)
//            interceptors.add(new PerformanceInterceptor());
//        bean.setPlugins(interceptors.toArray(new Interceptor[interceptors.size()]));

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources(this.xmlLocation));
            return bean.getObject();
        } catch (Exception var7) {
            var7.printStackTrace();
            throw new RuntimeException(var7);
        }
    }

    @Bean({"csbSqlSessionTemplate"})
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("csbSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, (String)null);
        this.url = this.propertyResolver.getProperty("spring.datasource.url");
        this.userName = this.propertyResolver.getProperty("spring.datasource.username");
        this.password = this.propertyResolver.getProperty("spring.datasource.password");
        this.driveClassName = this.propertyResolver.getProperty("spring.datasource.driver-class-name");
        this.filters = this.propertyResolver.getProperty("spring.datasource.filters");
        this.maxActive = this.propertyResolver.getProperty("spring.datasource.maxActive");
        this.initialSize = this.propertyResolver.getProperty("spring.datasource.initialSize");
        this.maxWait = this.propertyResolver.getProperty("spring.datasource.maxWait");
        this.minIdle = this.propertyResolver.getProperty("spring.datasource.minIdle");
        this.timeBetweenEvictionRunsMillis = this.propertyResolver.getProperty("spring.datasource.timeBetweenEvictionRunsMillis");
        this.minEvictableIdleTimeMillis = this.propertyResolver.getProperty("spring.datasource.minEvictableIdleTimeMillis");
        this.validationQuery = this.propertyResolver.getProperty("spring.datasource.validationQuery");
        this.testWhileIdle = this.propertyResolver.getProperty("spring.datasource.testWhileIdle");
        this.testOnBorrow = this.propertyResolver.getProperty("spring.datasource.testOnBorrow");
        this.testOnReturn = this.propertyResolver.getProperty("spring.datasource.testOnReturn");
        this.poolPreparedStatements = this.propertyResolver.getProperty("spring.datasource.poolPreparedStatements");
        this.maxOpenPreparedStatements = this.propertyResolver.getProperty("spring.datasource.maxOpenPreparedStatements");
        this.typeAliasesPackage = this.propertyResolver.getProperty("mybatis.typeAliasesPackage");
        this.typeHandlersPackage = this.propertyResolver.getProperty("mybatis.typeHandlersPackage");
        this.xmlLocation = this.propertyResolver.getProperty("mybatis.xmlLocation");
//        this.isPerformanceInterceptorActive = this.propertyResolver.getProperty("mybatis.isPerformanceInterceptorActive");
    }

    @Bean({"csbTransactionManager"})
    public DataSourceTransactionManager transactionManager(@Qualifier("csbDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
