package com.wqa.cems.config;

import com.github.pagehelper.PageInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class MyBatisConfig {

    @Bean
    public PageInterceptor pageInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        // 设置合理的值，默认值为 true
        properties.setProperty("reasonable", "true");
        // 支持通过 Mapper 接口参数来传递分页参数，默认值为 false
        properties.setProperty("supportMethodsArguments", "true");
        // 默认值为 SELECT count(0)
//        properties.setProperty("countColumn", "count(*)");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }
}

