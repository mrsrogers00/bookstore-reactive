package com.iwallet.bookstore.configuration;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Set;

@Configuration
@EnableConfigurationProperties(WebEndpointProperties.class)
public class ActuatorConfiguration {

    public ActuatorConfiguration(WebEndpointProperties webEndpointProperties, ManagementServerProperties managementServerProperties) {
        managementServerProperties.setBasePath("");
   //     webEndpointProperties.getExposure().getInclude().clear();
        webEndpointProperties.getExposure().setInclude(Set.of("health", "info"));
    }
}
