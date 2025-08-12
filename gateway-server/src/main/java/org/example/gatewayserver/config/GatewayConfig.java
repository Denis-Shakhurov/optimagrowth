package org.example.gatewayserver.config;

import org.example.gatewayserver.config.filter.JwtRolesFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public JwtRolesFilter jwtRolesFilter() {
        return new JwtRolesFilter();
    }
}
