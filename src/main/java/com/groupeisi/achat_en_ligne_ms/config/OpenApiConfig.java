package com.groupeisi.achat_en_ligne_ms.config;


import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {
    /*
     * Cette méthode permet de configurer Swagger (OpenAPI)
     * pour documenter les API de l'application.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        // Création d'un objet OpenAPI avec des informations générales sur l'API
        return new OpenAPI()
                .info(new Info()
                        // Titre affiché dans Swagger UI
                        .title("Achat En Ligne API")
                        // Version de l'API
                        .version("1.0")
                        // Description de l'API (explication du projet)
                        .description("Documentation API du microservice achat-en-ligne-ms"));
    }
}
