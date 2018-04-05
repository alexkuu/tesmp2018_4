package com.alex.app.repository

import com.alex.app.entities.Post
import com.alex.app.services.Logger
import io.searchbox.client.JestClient
import io.searchbox.client.JestResult
import io.searchbox.core.Bulk
import io.searchbox.core.BulkResult
import io.searchbox.core.Count
import io.searchbox.core.Delete
import io.searchbox.core.Index
import io.searchbox.core.Search
import io.searchbox.indices.CreateIndex
import io.searchbox.indices.IndicesExists
import io.searchbox.indices.aliases.GetAliases
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class Repository {

    @Autowired
    JestClient client

    @Autowired
    Logger log

    String deleteAll() {
        log.info("Delete all")
        boolean result = client.execute(new Delete.Builder().index("_all").build())
        if (result) {
            return "We've deleted everything :("
        }
        return "Something went wrong"
    }

    String deleteIndexByName(String name) {
        log.info("Delete index by name: " + name)
        if (indexExist(name)) {
            boolean result = client.execute(new Delete.Builder().index(name).build())
            if (result) {
                return "We've deleted index " + name
            }
        } else return "Index " + name + " doesn't exist"
        return "Something went wrong"
    }

    boolean indexExist(String name) {
        log.info("Check index exist: " + name)
        IndicesExists indicesExists = new IndicesExists.Builder(name).build()
        JestResult existsResult = client.execute(indicesExists)
        return existsResult.isSucceeded()
    }

    String getAliases() {
        log.info("Get aliases")
        JestResult result = client.execute(new GetAliases.Builder().build())
        return result.getJsonString()
    }

    String getAll() {
        log.info("get all")
        JestResult result = client.execute(new Search.Builder().build())
        return result.getJsonString()
    }

    String createIndex(String name) {
        log.info("Create index: " + name)
        if (indexExist(name)) {
            return "Index " + name + " already exist"
        } else {
            JestResult result = client.execute(new CreateIndex.Builder(name).build())
            if (result.isSucceeded()) {
                return "Index " + name + " created"
            } else {
                return result.errorMessage
            }
        }
    }

    String putPost(Post post) {
        log.info("Put post")
        createIndex(post.index)
        post.makeSource()
        Bulk bulk = new Bulk.Builder().defaultIndex(post.index).defaultType(post.type).addAction(new Index.Builder(post.source).build()).build()
        BulkResult result = client.execute(bulk)
        if (result.isSucceeded()) {
            return result.jsonString
        } else {
            return result.errorMessage
        }
    }

    String getPosts(String indexName, String type) {
        log.info("Getting posts from index: " + indexName + " type: " + type)
        JestResult result = client.execute(new Search.Builder().addIndex(indexName).addType(type).build())
        if (result.isSucceeded()) {
            log.info(result.getJsonString())
            return result.getJsonString()
        } else {
            log.info(result.getErrorMessage())
            return result.errorMessage
        }
    }

    String getCount(String indexName, String type) {
        log.info("Getting count from index: " + indexName + " type: " + type)
        JestResult result = client.execute(new Count.Builder().addIndex(indexName).addType(type).build())
        if (result.isSucceeded()) {
            log.info(result.getJsonString())
            JSONObject object = new JSONObject(result.getJsonString())
            return object.get("count")
        } else {
            log.info(result.getErrorMessage())
            return result.errorMessage
        }
    }

    String getAggregationJsonByField(String indexName, String typeName, String fieldName) {
        log.info("Get aggr using index:[" + indexName + "]/type:[" + typeName + "]/field:[" + fieldName + "]")
        String query = "{\"aggs\": {\"all_" + fieldName + "\": {\"terms\": { \"field\": \"" + fieldName + "\" }}}}";
        log.info("QUERY: [" + query + "]")
        JestResult result = client.execute(new Search.Builder(query).addIndex(indexName).addType(typeName).build())
        if (result.isSucceeded()) {
            log.info(result.jsonString)
            return result.getJsonObject().get("aggregations")
        } else {
            log.info(result.errorMessage)
            return result.errorMessage
        }
    }

}
