package com.groupeisi.achat_en_ligne_ms.config;


import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Achat En Ligne API")
                        .version("1.0")
                        .description("Documentation API du microservice achat-en-ligne-ms"));
    }
}
