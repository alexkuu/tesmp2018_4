package com.angrybambr.example.app.loader

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@ComponentScan(basePackages = "com.angrybambr.example.app")
@EnableAutoConfiguration
@SpringBootApplication
class App {
    static void main(String[] args) {
        SpringApplication.run(App.class, args)
    }

}
