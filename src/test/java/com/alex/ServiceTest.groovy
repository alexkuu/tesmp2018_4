package com.alex

import com.alex.app.entities.Post
import com.alex.app.loader.App
import com.alex.app.repository.Repository
import com.alex.app.services.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert
import org.testng.annotations.Test

@SpringBootTest(classes = App, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    Logger log

    @Autowired
    Repository repository

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
        String indexName = "alextest"
        String resp = repository.createIndex(indexName)
        log.info(resp)
        Assert.assertEquals(resp, "Index " + indexName + " created")
        repository.deleteAll()
    }

    @Test(description = "Negative create index test")
    void createIndexNegativeTest() {
        repository.deleteAll()
        log.info("Negative create index test")
        String indexName = "AlexTest"
        String resp = repository.createIndex(indexName)
        log.info(resp)
        Assert.assertEquals(resp, "\"InvalidIndexNameException[[" + indexName + "] Invalid index name [" + indexName + "], must be lowercase]\"")
    }

    @Test(description = "Create index which already exist")
    void createExistingIndexTest() {
        repository.deleteAll()
        log.info("Create index which already exist")
        String indexName = "alextest"
        String resp = repository.createIndex(indexName)
        log.info(resp)
        resp = repository.createIndex(indexName)
        log.info(resp)
        Assert.assertEquals(resp, "Index " + indexName + " already exist")
        repository.deleteAll()
    }

    @Test(description = "Check index exist test")
    void checkIndexExistTest() {
        repository.deleteAll()
        log.info("Check index exist test")
        String indexName = "alextest"
        boolean resp = repository.indexExist(indexName)
        log.info("Index exists: " + resp)
        Assert.assertFalse(resp)
        String createResp = repository.createIndex(indexName)
        log.info(createResp)
        Assert.assertEquals(createResp, "Index " + indexName + " created")
        resp = repository.indexExist(indexName)
        log.info("Index exists: " + resp)
        Assert.assertTrue(resp)
        repository.deleteAll()
    }

    @Test(description = "Put post")
    void putPostTest() {
        repository.deleteAll()
        log.info("Put post test")
        String indexName = "alextest"
        String type = "post"
        String title = "Test Post"
        String body = "Test post body. Body of the test post"
        String date = "01-02-2005"
        Post post = new Post(indexName, type, title, body, date)
        String resp = repository.putPost(post)
        log.info(resp)
        Assert.assertTrue(resp.contains("\"errors\":false"))
        Assert.assertTrue(resp.contains("\"_index\":\"" + indexName + "\""))
        Assert.assertTrue(resp.contains("\"_type\":\"" + type + "\""))
        Assert.assertTrue(resp.contains("\"_version\":1"))
        Assert.assertTrue(resp.contains("\"status\":201"))
        repository.deleteAll()
    }


}
