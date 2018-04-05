package com.alex.app.services

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.gsonfire.GsonFireBuilder
import io.searchbox.client.AbstractJestClient
import io.searchbox.client.JestClient
import io.searchbox.client.JestClientFactory
import io.searchbox.client.config.HttpClientConfig
import org.springframework.stereotype.Service

@Service
class JestClientService implements Serializable {
    private static final long serialVersionUID = 1L

    JestClient client = null

//    @Value(environment.getProperty("spring.elasticsearch.jest.uris"))
//    String host = environment.getProperty("spring.elasticsearch.jest.host")
    String host = "192.168.56.101";

//    @Value("${jest.elasticsearch.port}")
//    String port = environment.getProperty("spring.elasticsearch.jest.port")
    String port = "9200";

//    @Value("${jest.elasticsearch.index}")
    String indexName

    JestClient getClient() {

        if (this.client == null) {
            GsonFireBuilder fireBuilder = new GsonFireBuilder();
            fireBuilder.enableExposeMethodResult();

            GsonBuilder builder = fireBuilder.createGsonBuilder();
            builder.excludeFieldsWithoutExposeAnnotation();

            final Gson gson = builder.setDateFormat(AbstractJestClient.ELASTIC_SEARCH_DATE_FORMAT).create();

            println("Establishing JEST Connection to Elasticsearch over HTTP: " + "http://" + this.host + ":" + this.port)
            JestClientFactory factory = new JestClientFactory()
            factory.setHttpClientConfig(new HttpClientConfig
                    .Builder("http://" + this.host + ":" + this.port)
                    .multiThreaded(true)
                    .readTimeout(20000)
                    .gson(gson)
                    .build());
            this.client = factory.getObject()
        }

        return this.client;

    }

}
