package com.datapoamobilidade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class DatapoaMobilidadeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatapoaMobilidadeApplication.class, args);
    }

}
