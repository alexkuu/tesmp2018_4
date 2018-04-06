package com.angrybambr.example.app.controllers

import com.angrybambr.example.app.entities.Post
import com.angrybambr.example.app.repository.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ElasticSearchController {

    @Autowired
    Repository repository

    @RequestMapping("/deleteall")
    String deleteAll() {
        repository.deleteAll()
    }

    @RequestMapping("/getaliases")
    String getAliases() {
        repository.getAliases()
    }

    @RequestMapping("/getall")
    String getAll() {
        repository.getAll()
    }

    @RequestMapping("indexexist/{name}")
    String checkIndexExist(@PathVariable String name) {
        "Index " + name + " exists: " + repository.indexExist(name)
    }

    @RequestMapping("delete/index/{name}")
    String deleteIndexByName(@PathVariable String name) {
        repository.deleteIndexByName(name)
    }

    @RequestMapping("create/index/{name}")
    String createIndex(@PathVariable String name) {
        repository.createIndex(name)
    }

    @RequestMapping(method = RequestMethod.POST, value = "/putpost")
    String putPost(@RequestBody Post post) {
        repository.putPost(post)
    }

    @RequestMapping("search/{index}/{type}")
    String searchPostsInIndex(@PathVariable String index, @PathVariable String type) {
        repository.getPosts(index, type)
    }

    @RequestMapping("count/{index}/{type}")
    String countPostsInIndex(@PathVariable String index, @PathVariable String type) {
        repository.getCount(index, type)
    }

    @RequestMapping("getaggregation/{index}/{type}/{field}")
    String getAggregationJsonByField(
            @PathVariable String index, @PathVariable String type, @PathVariable String field) {
        repository.getAggregationJsonByField(index, type, field)
    }

}
