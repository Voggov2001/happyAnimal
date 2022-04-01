package com.coderiders.happyanimal.config;

import com.coderiders.happyanimal.controller.handler.RestTemplateResponseErrorHandler;
import com.coderiders.happyanimal.service.AnimalKindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
@EnableScheduling
public class WebConfiguration {
    AnimalKindService service;

    @Autowired
    public WebConfiguration(AnimalKindService service) {
        this.service = service;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) throws IOException {
        service.createAll();
        return restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }
}
