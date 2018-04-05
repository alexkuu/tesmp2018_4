package com.alex.app.services

import io.searchbox.client.JestClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service

@Service
class JestClientBean {
    @Autowired
    JestClientService jestClientService

    @Bean
    JestClient client() {
        return jestClientService.getClient()
    }
}
