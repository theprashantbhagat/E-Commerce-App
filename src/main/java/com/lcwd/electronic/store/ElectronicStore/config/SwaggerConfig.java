package com.lcwd.electronic.store.ElectronicStore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayDeque;
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket docket(){
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(getApiInfo());
        return docket;
    }

    private ApiInfo getApiInfo() {
        ApiInfo apiInfo = new ApiInfo(

                "Electronic Store Backend:APIs",
                "This is backend project created by prashant",
                "1.0.0V",
                "https://www.mobicoolsoftwaresolutions.com/",
                new Contact("prashant","https://www.mobicoolsoftwaresolutions.com/","prashantbhagat1997@gmail.com"),
                "License of Apis",
                "https://www.mobicoolsoftwaresolutions.com/",
                new ArrayDeque<>()
        );

        return apiInfo;
    }
}
