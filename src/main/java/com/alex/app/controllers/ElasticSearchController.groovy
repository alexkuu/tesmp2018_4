package com.alex.app.controllers

import com.alex.app.entities.Post
import com.alex.app.repository.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class ElasticSearchController {

    @Autowired
    Repository repository

    @RequestMapping("/deleteall")
    String deleteAll() {
        return repository.deleteAll()
    }

    @RequestMapping("/getaliases")
    String getAliases() {
        return repository.getAliases()
    }

    @RequestMapping("/getall")
    String getAll() {
        return repository.getAll()
    }

    @RequestMapping("indexexist/{name}")
    String checkIndexExist(@PathVariable String name) {
        return "Index " + name + " exists: " + repository.indexExist(name)
    }

    @RequestMapping("delete/index/{name}")
    String deleteIndexByName(@PathVariable String name) {
        return repository.deleteIndexByName(name)
    }

    @RequestMapping("create/index/{name}")
    String createIndex(@PathVariable String name) {
        return repository.createIndex(name)
    }

    @RequestMapping(method = RequestMethod.POST, value = "/putpost")
    String putPost(@RequestBody Post post) {
        return repository.putPost(post)
    }

}
