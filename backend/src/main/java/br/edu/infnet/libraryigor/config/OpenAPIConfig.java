package br.edu.infnet.libraryigor.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
           return new OpenAPI()
                   .info(new Info()
                           .title("Library API")
                           .description("This project is a management of library to register and loan a book for students and associates")
                           .version("v0.0.1")
                           .license(new License()
                                   .name("Apache 2.0")
                                   .url("https://github.com/igorcamposdeborba/library-backend/blob/main/Design%20Document%20(planejamento%20do%20software).pdf"))
                           );
    }
}
