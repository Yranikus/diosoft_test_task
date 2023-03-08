package com.example.diosoft_test_task.configuration;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class StringPropertyConfiguration {

    @Bean
    public PropertiesFactoryBean propertiesfilemapping() {
        PropertiesFactoryBean factoryBean = new PropertiesFactoryBean();
        factoryBean.setFileEncoding("UTF-8");
        factoryBean.setLocations(new ClassPathResource("messages.properties"),
                new ClassPathResource("url.properties"));
        return factoryBean;
    }


}
