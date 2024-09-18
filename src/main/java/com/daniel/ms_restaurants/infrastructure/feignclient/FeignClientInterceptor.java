package com.daniel.ms_restaurants.infrastructure.feignclient;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String authHeader = "Basic " + Base64Util.encode("admin:12345");
        template.header(HttpHeaders.AUTHORIZATION, authHeader);
    }
}

