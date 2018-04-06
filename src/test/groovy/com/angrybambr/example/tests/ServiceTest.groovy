package com.angrybambr.example.tests

import com.angrybambr.example.app.entities.Post
import com.angrybambr.example.app.loader.App
import com.angrybambr.example.app.repository.Repository
import com.angrybambr.example.app.services.Logger
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert
import org.testng.annotations.Test

import java.util.concurrent.ThreadLocalRandom

@SpringBootTest(classes = App, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    Logger log

    @Autowired
    Repository repository

    private final String INDEX_NAME = "TestIndex".toLowerCase()

    @Test(description = "Delete all test")
    void deleteAllTest() {
        log.info("Delete all test")
        String deleteResp = repository.deleteAll()
        log.info(deleteResp)
        Assert.assertEquals(deleteResp, "We've deleted everything :(")
    }

    @Test(description = "Create index test")
    void createIndexTest() {
        repository.deleteAll()
        log.info("Create index test")
        String resp = repository.createIndex(INDEX_NAME)
        log.info(resp)
        Assert.assertEquals(resp, "Index " + INDEX_NAME + " created")
        repository.deleteAll()
    }

    @Test(description = "Negative create index test")
    void createIndexNegativeTest() {
        repository.deleteAll()
        log.info("Negative create index test")
        String resp = repository.createIndex(INDEX_NAME.toUpperCase())
        log.info(resp)
        Assert.assertEquals(resp, "\"InvalidIndexNameException[[" + INDEX_NAME.toUpperCase() + "] Invalid index name [" + INDEX_NAME.toUpperCase() + "], must be lowercase]\"")
    }

    @Test(description = "Create index which already exist")
    void createExistingIndexTest() {
        repository.deleteAll()
        log.info("Create index which already exist")
        String resp = repository.createIndex(INDEX_NAME)
        log.info(resp)
        resp = repository.createIndex(INDEX_NAME)
        log.info(resp)
        Assert.assertEquals(resp, "Index " + INDEX_NAME + " already exist")
        repository.deleteAll()
    }

    @Test(description = "Check index exist test")
    void checkIndexExistTest() {
        repository.deleteAll()
        log.info("Check index exist test")
        boolean resp = repository.indexExist(INDEX_NAME)
        log.info("Index exists: " + resp)
        Assert.assertFalse(resp)
        String createResp = repository.createIndex(INDEX_NAME)
        log.info(createResp)
        Assert.assertEquals(createResp, "Index " + INDEX_NAME + " created")
        resp = repository.indexExist(INDEX_NAME)
        log.info("Index exists: " + resp)
        Assert.assertTrue(resp)
        repository.deleteAll()
    }

    @Test(description = "Put post")
    void putPostTest() {
        repository.deleteAll()
        log.info("Put post test")
        String type = "post"
        String title = "Test Post"
        String body = "Test post body. Body of the test post"
        String date = "01-02-2005"
        Post post = new Post(INDEX_NAME, type, title, body, date)
        String resp = repository.putPost(post)
        log.info(resp)
        Assert.assertTrue(resp.contains("\"errors\":false"))
        Assert.assertTrue(resp.contains("\"_index\":\"" + INDEX_NAME + "\""))
        Assert.assertTrue(resp.contains("\"_type\":\"" + type + "\""))
        Assert.assertTrue(resp.contains("\"_version\":1"))
        Assert.assertTrue(resp.contains("\"status\":201"))
        repository.deleteAll()
    }

    @Test(description = "Aggr doc_count test")
    void docCountTest() {
        repository.deleteAll()
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
            Post post = new Post(INDEX_NAME, type, firstTitle, body + i, date + i)
            String resp = repository.putPost(post)
            log.info(resp)
        }

        for (int i = 0; i < secondRandom; i++) {
            Post post = new Post(INDEX_NAME, type, secondTitle, body + i, date + i)
            String resp = repository.putPost(post)
            log.info(resp)
        }

        for (int i = 0; i < thirdRandom; i++) {
            Post post = new Post(INDEX_NAME, type, thirdTitle, body + i, date + i)
            String resp = repository.putPost(post)
            log.info(resp)
        }
        Thread.sleep(5000)
        String request = "getaggregation/" + INDEX_NAME + "/" + type + "/title"
        String response = repository.getAggregationJsonByField(INDEX_NAME, type, "title")
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
        repository.deleteAll()
    }


}
