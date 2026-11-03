package br.com.tarefas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI tarefasOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Tarefas API")
                .description("API REST para gerenciar tarefas, com prioridade de 1 a 5.")
                .version("0.0.1"));
    }
}
