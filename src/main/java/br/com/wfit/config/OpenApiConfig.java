package br.com.wfit.config;

import org.eclipse.microprofile.openapi.annotations.Components;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeIn;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(   title = "Sistema de exemplo de transações, curso Quarkus CoffeeandIT",
                        version = "1.0.0", contact = @Contact(
                        name = "Willyan T. Ferdinando",
                        url = "https://github.com/wferdinando",
                        email = "willyantferdinando@gmail.com")
        ),
        tags = {
                @Tag(name = "/v1/pix", description = "Grupo de API's para manipulação de transações PIX"),
                @Tag(name = "/v1/chaves", description = "Grupo de API's para manipulação de chaves PIX")
        },
        servers = {
                @Server(url = "http://localhost:8080")
        },
        security = {
                @SecurityRequirement(name = "jwt", scopes = {"coffeeandit"})
        },
        components = @Components(
                securitySchemes = {
                        @SecurityScheme(
                                securitySchemeName = "jwt",
                                type = SecuritySchemeType.HTTP,
                                scheme = "bearer",
                                bearerFormat = "bearer",
                                in = SecuritySchemeIn.HEADER
                        )
                })
)



public class OpenApiConfig extends Application {
}
