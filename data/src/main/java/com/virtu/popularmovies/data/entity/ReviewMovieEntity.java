package com.virtu.popularmovies.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by virtu on 23/08/2015.
 */
public class ReviewMovieEntity {

    @SerializedName("id")
    private String id;
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;
    @SerializedName("url")
    private String url;

    public ReviewMovieEntity(){
        //Empty Constructor
    }

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getAuthor() {return author;}

    public void setAuthor(String author) {this.author = author;}

    public String getContent() {return content;}

    public void setContent(String content) {this.content = content;}

    public String getUrl() {return url;}

    public void setUrl(String url) {this.url = url;}


    @Override public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("***** Review Entity Details *****\n");
        stringBuilder.append("id=" + this.getId() + "\n");
        stringBuilder.append("Author=" + this.getAuthor() + "\n");
        stringBuilder.append("Content Review=" + this.getContent() + "\n");
        stringBuilder.append("Url Reviewo=" + this.getUrl() + "\n");
        stringBuilder.append("*******************************");

        return stringBuilder.toString();
    }
}
