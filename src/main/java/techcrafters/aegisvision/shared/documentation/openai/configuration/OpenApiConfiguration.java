package techcrafters.aegisvision.shared.documentation.openai.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
  @Bean
  public OpenAPI aegisVisionPlatformOpenApi(){
    return new OpenAPI()
        .info(new Info().title("Aegis Vision API")
            .description(
                "Aegis Vision Application REST API Documentation")
            .version("v1.0.0")
            .license(new License().name("Apache 2.0").url("https://springdoc.org")))
        .externalDocs(new ExternalDocumentation()
            .description("Aegis Vision Wiki Documentation")
            .url("https://aegis-vision-service.wiki.github.org/docs"))
        .addSecurityItem(new SecurityRequirement().addList("AegisVisionSecurityScheme"))
        .components(new Components().addSecuritySchemes("AegisVisionSecurityScheme", new SecurityScheme()
            .name("AegisVisionSecurityScheme").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
  }
}