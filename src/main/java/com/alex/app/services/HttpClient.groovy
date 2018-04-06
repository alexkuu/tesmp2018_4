package com.alex.app.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class HttpClient {

    @Autowired
    Environment environment

    String host
    String port

    private String server
    private RestTemplate rest
    private HttpHeaders headers
    private HttpStatus status
    private String response

    HttpClient() {
        this.rest = new RestTemplate()
        this.headers = new HttpHeaders()
        headers.add("Content-Type", "application/json")
        headers.add("Accept", "*/*")
    }

    private void setProperties() {
        this.host = environment.getProperty("server.address")
        this.port = environment.getProperty("server.port")
        this.server = "http://" + this.host + ":" + this.port
    }

    String get(String uri) {
        if (server == null) setProperties()
        HttpEntity<String> requestEntity = new HttpEntity<String>("", headers)
        ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.GET, requestEntity, String.class)
        this.setStatus(responseEntity.getStatusCode())
        this.setResponse(responseEntity.getBody())
        responseEntity.getBody()
    }

    String post(String uri, String json) {
        if (server == null) setProperties()
        HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers)
        ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.POST, requestEntity, String.class)
        this.setStatus(responseEntity.getStatusCode())
        this.setResponse(responseEntity.getBody())
        responseEntity.getBody()
    }

    String getResponse() {
        response
    }

    private void setResponse(String response) {
        this.response = response
    }

    HttpStatus getStatus() {
        status
    }

    void setStatus(HttpStatus status) {
        this.status = status
    }

}
