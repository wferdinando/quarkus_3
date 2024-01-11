package br.com.wfit.config;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.ws.rs.core.Application;

@OpenAPIDefinition(
    tags = {
        @Tag(name = "Curso Quarkus 3.0", description = "Projeto Inicial da grade curricular do curso.")
    },
    info = @Info(
        title = "Quarkus 3.0",
        version = "1.0.0",
        contact = @Contact(
            name = "Willyan T. Ferdinando",
            url = "https://github.com/wferdinando",
            email = "willyantferdinando@gmail.com"),
    license = @License(
        name = "WFIT",
        url = "https://github.com/wferdinando"))
)

public class OpenApiConfig extends Application {
    
}
