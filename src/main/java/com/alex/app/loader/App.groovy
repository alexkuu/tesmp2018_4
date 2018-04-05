package com.alex.app.loader

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.PropertySource
import org.springframework.web.bind.annotation.RequestMapping

@ComponentScan(basePackages = "com.alex.app")
@EnableAutoConfiguration
@SpringBootApplication
class App {

    static void main(String[] args) {
        SpringApplication.run(App.class, args)
    }

}