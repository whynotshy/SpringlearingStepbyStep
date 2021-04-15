package com;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
@ComponentScan(basePackages = {"com.huwei","com.mimi"})
public class AppConfig {

        @Bean //beanid:"r"
        public Random r(){
            return new Random();
        }
    }
}
