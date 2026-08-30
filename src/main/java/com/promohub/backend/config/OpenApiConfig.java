package com.promohub.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customAPI(){
        return new OpenAPI()
                .info(new Info().title("Punto Promo SGO - API REST")
                .description("API REST para la gestión, " +
                        "filtrado y centralización de promociones y beneficios bancarios en Santiago del Estero.")
                .version("1.0.0")
                .contact(new Contact().name("Agustin Figueroa").url("https://github.com/AFigueroaAgustin")
                        .email("agustinfigueroa390@gmail.com"))
                );
    }
}
