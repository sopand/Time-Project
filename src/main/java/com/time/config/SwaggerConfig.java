package com.time.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

	@Bean
	  public OpenAPI openAPI() {
	    return new OpenAPI()
	        .info(new Info()
	            .title("Time-Web API")
	            .description("하루의 일정을 등록할 수 있어요")
	            .version("1.0.0"))
	        .components(new Components()
	            .addSecuritySchemes("bearer-key",
	                new SecurityScheme()
	                .type(Type.HTTP)
	                .scheme("bearer")
	                .bearerFormat("JWT")));

	  }
}
