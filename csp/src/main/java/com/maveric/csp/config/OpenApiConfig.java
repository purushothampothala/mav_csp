package com.maveric.csp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        servers = {
                @Server(
                        url = "http://localhost:8080"
                ),

        }

)
public class OpenApiConfig {
}
