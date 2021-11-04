package com.poly;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class ValidateConfig {
    @Bean("messageSource")
    public MessageSource getMessageSource() {
        ReloadableResourceBundleMessageSource ms
                = new ReloadableResourceBundleMessageSource();
        ms.setBasenames("classpath:validate/user_validate");
        ms.setDefaultEncoding("utf-8");
        return ms;
    }

    @Bean("messageSource1")
    public MessageSource getMessageSource1() {
        ReloadableResourceBundleMessageSource ms1
                = new ReloadableResourceBundleMessageSource();
        ms1.setBasenames("classpath:validate/product_validate");
        ms1.setDefaultEncoding("utf-8");
        return ms1;
    }
}
