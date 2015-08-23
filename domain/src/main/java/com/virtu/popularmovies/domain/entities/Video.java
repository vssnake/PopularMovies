package com.virtu.popularmovies.domain.entities;

/**
 * Created by virtu on 23/08/2015.
 */
public class Video {
    private String name;
    private String site;
    private String type;
    private String keyVideo;

    public Video(){}
    public String getName() {return name;
    }

    public void setName(String name) {this.name = name;}

    public String getSite() {return site;}

    public void setSite(String site) {this.site = site;}

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}

    public String getKeyVideo() {return keyVideo;}

    public void setKeyVideo(String keyVideo) {this.keyVideo = keyVideo;}

    @Override public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("***** Video Details *****\n");
        stringBuilder.append("Name=" + this.getName() + "\n");
        stringBuilder.append("Site=" + this.getSite() + "\n");
        stringBuilder.append("Type=" + this.getType() + "\n");
        stringBuilder.append("KeyVideo=" + this.getKeyVideo() + "\n");
        stringBuilder.append("*******************************");

        return stringBuilder.toString();
    }
}
