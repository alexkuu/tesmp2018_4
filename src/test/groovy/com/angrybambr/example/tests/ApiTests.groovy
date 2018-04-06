package com.angrybambr.example.tests

import com.angrybambr.example.app.loader.App
import com.angrybambr.example.app.services.HttpClient
import com.angrybambr.example.app.services.Logger
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.JsonObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert
import org.testng.annotations.Test

import java.util.concurrent.ThreadLocalRandom

@SpringBootTest(classes = App, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ApiTests extends AbstractTestNGSpringContextTests {

    @Autowired
    HttpClient httpClient

    @Autowired
    Logger log

    private final String INDEX_NAME = "TestIndex".toLowerCase()

    @Test(description = "Delete all test")
    void deleteAllTest() {
        log.info("Delete all test")
        String deleteResp = httpClient.get("deleteall")
        log.info(deleteResp)
        Assert.assertEquals(deleteResp, "We've deleted everything :(")
    }

    @Test(description = "Create index test")
    void createIndexTest() {
        httpClient.get("deleteall")
        log.info("Create index test")
        String resp = httpClient.get("create/index/" + INDEX_NAME)
        log.info(resp)
        Assert.assertEquals(resp, "Index " + INDEX_NAME + " created")
        httpClient.get("deleteall")
    }

    @Test(description = "Negative create index test")
    void createIndexNegativeTest() {
        httpClient.get("deleteall")
        log.info("Negative create index test")
        String resp = httpClient.get("create/index/" + INDEX_NAME.toUpperCase())
        log.info(resp)
        Assert.assertEquals(resp, "\"InvalidIndexNameException[[" + INDEX_NAME.toUpperCase() + "] Invalid index name [" + INDEX_NAME.toUpperCase() + "], must be lowercase]\"")
    }

    @Test(description = "Create index which already exist")
    void createExistingIndexTest() {
        httpClient.get("deleteall")
        log.info("Create index which already exist")
        String resp = httpClient.get("create/index/" + INDEX_NAME)
        log.info(resp)
        resp = httpClient.get("create/index/" + INDEX_NAME)
        log.info(resp)
        Assert.assertEquals(resp, "Index " + INDEX_NAME + " already exist")
        httpClient.get("deleteall")
    }

    @Test(description = "Check index exist test")
    void checkIndexExistTest() {
        httpClient.get("deleteall")
        log.info("Check index exist test")
        String resp = httpClient.get("indexexist/" + INDEX_NAME)
        log.info(resp)
        Assert.assertEquals(resp, "Index " + INDEX_NAME + " exists: false")
        resp = httpClient.get("create/index/" + INDEX_NAME)
        log.info(resp)
        Assert.assertEquals(resp, "Index " + INDEX_NAME + " created")
        resp = httpClient.get("indexexist/" + INDEX_NAME)
        log.info(resp)
        Assert.assertEquals(resp, "Index " + INDEX_NAME + " exists: true")
        httpClient.get("deleteall")
    }

    @Test(description = "Put post")
    void putPostTest() {
        httpClient.get("deleteall")
        log.info("Put post test")
        String type = "post"
        String title = "Test Post"
        String body = "Test post body. Body of the test post"
        String date = "01-02-2005"
        JsonObject object = new JsonObject();
        object.addProperty("index", INDEX_NAME)
        object.addProperty("type", type)
        object.addProperty("title", title)
        object.addProperty("body", body)
        object.addProperty("date", date)
        log.info(object.toString())
        String resp = httpClient.post("putpost", object.toString())
        log.info(resp)
        Assert.assertTrue(resp.contains("\"errors\":false"))
        Assert.assertTrue(resp.contains("\"_index\":\"" + INDEX_NAME + "\""))
        Assert.assertTrue(resp.contains("\"_type\":\"" + type + "\""))
        Assert.assertTrue(resp.contains("\"_version\":1"))
        Assert.assertTrue(resp.contains("\"status\":201"))
        httpClient.get("deleteall")
    }

    @Test(description = "Aggr doc_count test")
    void docCountTest() {
        httpClient.get("deleteall")
        log.info("Aggr doc_count test")
        int minCount = 1;
        int maxCount = 20;
        String type = "post"
        String firstTitle = "Music"
        String secondTitle = "Movie"
        String thirdTitle = "Theater"
        String body = "Test post body. Body of the test post"
        String date = "01-02-2005"
        int fisrtRandom = ThreadLocalRandom.current().nextInt(minCount, maxCount + 1)
        int secondRandom = ThreadLocalRandom.current().nextInt(minCount, maxCount + 1)
        int thirdRandom = ThreadLocalRandom.current().nextInt(minCount, maxCount + 1)
        for (int i = 0; i < fisrtRandom; i++) {
            JsonObject object = new JsonObject();
            object.addProperty("index", INDEX_NAME)
            object.addProperty("type", type)
            object.addProperty("title", firstTitle)
            object.addProperty("body", body + i)
            object.addProperty("date", date + i)
            log.info(object.toString())
            String resp = httpClient.post("putpost", object.toString())
            log.info(resp)
        }

        for (int i = 0; i < secondRandom; i++) {
            JsonObject object = new JsonObject();
            object.addProperty("index", INDEX_NAME)
            object.addProperty("type", type)
            object.addProperty("title", secondTitle)
            object.addProperty("body", body + i)
            object.addProperty("date", date + i)
            log.info(object.toString())
            String resp = httpClient.post("putpost", object.toString())
            log.info(resp)
        }

        for (int i = 0; i < thirdRandom; i++) {
            JsonObject object = new JsonObject();
            object.addProperty("index", INDEX_NAME)
            object.addProperty("type", type)
            object.addProperty("title", thirdTitle)
            object.addProperty("body", body + i)
            object.addProperty("date", date + i)
            log.info(object.toString())
            String resp = httpClient.post("putpost", object.toString())
            log.info(resp)
        }
        Thread.sleep(5000)
        String request = "getaggregation/" + INDEX_NAME + "/" + type + "/title"
        log.info("Request: [" + request + "]")
        String response = httpClient.get(request)
        HashMap<String, HashMap<String, Object>> result =
                new ObjectMapper().readValue(response, HashMap.class)
        HashMap<String, Object> allFields = result.get("all_title")
        List<HashMap<String, String>> buckets = allFields.get("buckets")
        Assert.assertEquals(buckets.size(), 3)
        for (HashMap<String, String> bucket : buckets) {
            if (bucket.get("key").equals(firstTitle.toLowerCase())) {
                Assert.assertEquals(bucket.get("doc_count"), fisrtRandom)
            } else if (bucket.get("key").equals(secondTitle.toLowerCase())) {
                Assert.assertEquals(bucket.get("doc_count"), secondRandom)
            } else {
                Assert.assertEquals(bucket.get("doc_count"), thirdRandom)
            }
        }
        httpClient.get("deleteall")
    }

}
