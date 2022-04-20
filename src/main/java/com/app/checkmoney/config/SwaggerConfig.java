package com.app.checkmoney.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Bean
    public Docket productApi() {
        ApiInfo apiInfo = new ApiInfoBuilder().title("Api Rest CheckMoney - Gestión de Procesos para Casas de Cambio")
                .description(
                        "Api, donde se gestiona los procesos de la Plataforma Integrada")
                .version("1.0").license("Este api esta bajo la Apache license Version 2.0").build();
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo).select()
                .apis(RequestHandlerSelectors.basePackage("com.app.checkmoney.controller"))
                .paths(PathSelectors.ant("/api/**")).build().useDefaultResponseMessages(false)
                .securitySchemes(Arrays.asList(apiKey()));
    }

    private ApiKey apiKey() {
        return new ApiKey("apiKey", "Authorization", "header");
    }

}
