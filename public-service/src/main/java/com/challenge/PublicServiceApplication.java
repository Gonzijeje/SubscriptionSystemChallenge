package com.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This is the main class of the application
 *
 * @author Gonzalo Collada
 * @version 1.0.0
 */
@SpringBootApplication
public class PublicServiceApplication {

    // Local storage RocksDB name
    public static String STORE_NAME = "store";

    public static void main(String[] args) {
        SpringApplication.run(PublicServiceApplication.class, args);
    }

}
