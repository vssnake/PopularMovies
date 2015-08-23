package com.virtu.popularmovies.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by virtu on 23/08/2015.
 */
public class VideoMovieEntity {

    @SerializedName("id")
    private String id;
    @SerializedName("iso_639_1")
    private String language;
    @SerializedName("key")
    private String key;
    @SerializedName("name")
    private String name;
    @SerializedName("site")
    private String site;
    @SerializedName("size")
    private int resolution;
    @SerializedName("type")
    private String typeVideo;


    public VideoMovieEntity(){
        //Empty Constructor
    }

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getLanguage() {return language;}

    public void setLanguage(String language) {this.language = language;}

    public String getKey() {return key;}

    public void setKey(String key) {this.key = key;}

    public String getSite() {return site;}

    public void setSite(String site) {this.site = site;}

    public int getResolution() {return resolution;}

    public void setResolution(int resolution) {this.resolution = resolution;}

    public String getTypeVideo() {return typeVideo;}

    public void setTypeVideo(String typeVideo) {this.typeVideo = typeVideo;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}


    @Override public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("***** Video Entity Details *****\n");
        stringBuilder.append("id=" + this.getId() + "\n");
        stringBuilder.append("Language=" + this.getLanguage() + "\n");
        stringBuilder.append("Key video=" + this.getKey() + "\n");
        stringBuilder.append("Name video=" + this.getName() + "\n");
        stringBuilder.append("Size video=" + this.getResolution() + "\n");
        stringBuilder.append("Site video=" + this.getSite() + "\n");
        stringBuilder.append("Type video=" + this.getTypeVideo() + "\n");
        stringBuilder.append("*******************************");

        return stringBuilder.toString();
    }

}
