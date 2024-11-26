package com.farmacia_web.farmacia_web.configuration;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger para la documentación de la API.
 * Define los detalles básicos de la API como el título, versión, descripción, contacto y licencia.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configura los detalles básicos de la documentación del API.
     *
     * @return Un objeto {@link OpenAPI} con la configuración personalizada para Swagger.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión") // Título de la API
                        .version("1.0.0") // Versión de la API
                        .description("Documentación interactiva de la API de gestión") // Breve descripción de la API
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("Michel Cardona") // Nombre del contacto
                                .email("u20231211735@usco.edu.co")) // Correo electrónico del contacto
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("Licencia MIT") // Nombre de la licencia
                                .url("http://opensource.org/licenses/MIT"))); // URL de la licencia
    }
}
