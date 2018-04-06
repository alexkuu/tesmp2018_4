package com.alex.app.services


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.gsonfire.GsonFireBuilder
import io.searchbox.client.AbstractJestClient
import io.searchbox.client.JestClient
import io.searchbox.client.JestClientFactory
import io.searchbox.client.config.HttpClientConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

@Service
class JestClientService implements Serializable {

    @Autowired
    Environment environment

    JestClient client = null

    private String host
    private String port

    JestClient getClient() {

        if (this.client == null) {
            this.host = environment.getProperty("elastic.search.host")
            this.port = environment.getProperty("elastic.search.port")
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
        this.client;
    }

}
