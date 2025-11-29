package com.example.tarefas.config;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.ErrorResponse;

import java.util.Map;

@Configuration
public class SwaggerConfig {

    @Bean
    public OperationCustomizer globalErrorResponses() {
        return (operation, handlerMethod) -> {

            // Skip one specific method if needed
            if (handlerMethod.getMethod().getName().equals("login")) {
                return operation;
            }

            // Add common error response
            operation.getResponses().addApiResponse("401",
                    new io.swagger.v3.oas.models.responses.ApiResponse()
                            .description("Token error")
                            .content(new io.swagger.v3.oas.models.media.Content()
                                    .addMediaType("application/json",
                                            new io.swagger.v3.oas.models.media.MediaType()
                                                    .schema(new io.swagger.v3.oas.models.media.Schema<ErrorResponse>()
                                                            .$ref("#/components/schemas/ErrorResponse")
                                                    )
                                                    .example(Map.of("error", "Token not informed"))
                                    )
                            )
            );

            return operation;
        };
    }
}