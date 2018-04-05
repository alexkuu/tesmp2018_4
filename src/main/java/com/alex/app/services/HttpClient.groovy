package com.alex.app.services


import org.springframework.context.annotation.Bean
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import org.springframework.stereotype.Service

@Service
class HttpClient {

//    private @Value('${host}')
    String host = "localhost"

//    private @Value('${port}')
    String port = "8080"

    private String server = "http://" + host + ":" + port
    private RestTemplate rest
    private HttpHeaders headers
    private HttpStatus status

    HttpClient() {
        this.rest = new RestTemplate()
        this.headers = new HttpHeaders()
        headers.add("Content-Type", "application/json")
        headers.add("Accept", "*/*")
    }

    public String get(String uri) {
        HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
        ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.GET, requestEntity, String.class);
        this.setStatus(responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    public String post(String uri, String json) {
        HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
        ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.POST, requestEntity, String.class);
        this.setStatus(responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    HttpStatus getStatus() {
        return status
    }

    void setStatus(HttpStatus status) {
        this.status = status
    }

    @Bean
    static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer()
    }
}
