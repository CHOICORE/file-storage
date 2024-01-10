package me.choicore.demo.filestorage.presentation;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@TestConfiguration(proxyBeanMethods = false)
public class TestMultipartConfigurer {

    @Autowired
    MultipartProperties multipartProperties;

    @Bean
    public MultipartConfigElement multipartConfigElement(MultipartProperties multipartProperties) {
        MultipartConfigFactory multipartConfigFactory = new MultipartConfigFactory();
        multipartConfigFactory.setMaxFileSize(multipartProperties.getMaxFileSize());
        multipartConfigFactory.setMaxRequestSize(multipartProperties.getMaxRequestSize());
        multipartConfigFactory.setFileSizeThreshold(multipartProperties.getFileSizeThreshold());
        multipartConfigFactory.setLocation(multipartProperties.getLocation());
        return multipartConfigFactory.createMultipartConfig();
    }

    @Bean
    public MultipartResolver multipartResolver() {
        StandardServletMultipartResolver standardServletMultipartResolver = new StandardServletMultipartResolver();
        standardServletMultipartResolver.setResolveLazily(multipartProperties.isResolveLazily());
        standardServletMultipartResolver.setStrictServletCompliance(multipartProperties.isStrictServletCompliance());
        return standardServletMultipartResolver;
    }
}
