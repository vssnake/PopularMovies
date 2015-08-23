package com.virtu.popularmovies.domain.entities;

/**
 * Created by virtu on 23/08/2015.
 */
public class Review {
    private String author;
    private String content;

    public Review(){}

    public String getAuthor() {return author;}

    public void setAuthor(String author) {this.author = author;}

    public String getContent() {return content;}

    public void setContent(String content) {this.content = content;}

    @Override public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("***** Review Details *****\n");
        stringBuilder.append("Author=" + this.getAuthor() + "\n");
        stringBuilder.append("Content=" + this.getContent() + "\n");
        stringBuilder.append("*******************************");

        return stringBuilder.toString();
    }
}
