package com.alex.app.entities

import com.google.gson.JsonObject

class Post {

    private String index
    private String type
    private String title
    private String body
    private String date
    private JsonObject source

    Post() {
    }

    Post(String index, String type, String title, String body, String date) {
        super()
        this.index = index
        this.type = type
        this.title = title
        this.body = body
        this.date = date
    }

    void makeSource(){
        JsonObject source = new JsonObject()
        source.addProperty("title", this.title)
        source.addProperty("body", this.body)
        source.addProperty("date", this.date)
        this.source = source
    }

    JsonObject getSource() {
        return source
    }

    String getIndex() {
        return index
    }

    void setIndex(String index) {
        this.index = index
    }

    String getType() {
        return type
    }

    void setType(String type) {
        this.type = type
    }

    String getTitle() {
        return title
    }

    void setTitle(String title) {
        this.title = title
    }

    String getBody() {
        return body
    }

    void setBody(String body) {
        this.body = body
    }

    String getDate() {
        return date
    }

    void setDate(String date) {
        this.date = date
    }
}
