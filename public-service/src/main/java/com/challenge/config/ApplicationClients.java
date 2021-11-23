package com.challenge.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to store in local memory the different users/clients of the application declared
 * on application.yaml file
 * 
 * @author Gonzalo Collada
 * @version 1.0.0
 */
@ConfigurationProperties("application")
public class ApplicationClients {

    private final List<ApplicationClient> clients = new ArrayList<>();

    public List<ApplicationClient> getClients() {
        return this.clients;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplicationClient {
        private String username;
        private String password;
        private String[] roles;
    }
}