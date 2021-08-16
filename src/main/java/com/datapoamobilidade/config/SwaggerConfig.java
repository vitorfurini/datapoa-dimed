package com.datapoamobilidade.config;

import com.datapoamobilidade.constantes.Constantes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket votacaoApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(Constantes.PACKAGE))
                .paths(regex(Constantes.PATHS))
                .build()
                .apiInfo(metaInfo());
    }

    @SuppressWarnings("rawtypes")
    private ApiInfo metaInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "TransportePorto API REST",
                "API REST de Transporte de Linhas e Itinerários de Ônibus do município de Porto Alegre/RS.",
                "1.0.0",
                "Termos do serviço",
                new Contact(Constantes.DEVNAME, Constantes.DEVSITE, Constantes.DEVEMAIL),
                "Apache License Version 2.0",
                "https://www.apache.org/licesen.html", new ArrayList<VendorExtension>()
        );

        return apiInfo;
    }
}
