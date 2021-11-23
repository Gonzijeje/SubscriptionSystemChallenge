package com.challenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;


/**
 * Configuration to provide API documentation using Swagger
 *
 * @author Gonzalo Collada
 * @version 1.0.0
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.challenge.rest"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Subscription System API",
                "API description for the Subscription System required by Adidas challenge",
                "1.0.0",
                "ToS",
                new Contact("Gonzalo", "https://www.linkedin.com/in/gonzalo-collada-v%C3%A1zquez-427783181/",
                        "gcolladavazquez@gmail.com"),
                "License of API",
                "API license URL",
                Collections.emptyList()
        );
    }
}

