package com.virtu.popularmovies.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by virtu on 08/07/2015.
 * Movie Entity used in data layer
 */
public class MovieEntity {

    @SerializedName("id")
    private Long mId;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("release_date")
    private String mReleaseDate;
    @SerializedName("poster_path")
    private String mPosterUrl;
    @SerializedName("vote_average")
    private float mVote_average;
    @SerializedName("overview")
    private String mOverView;

    public MovieEntity(){
        //Empty Constructor
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public void setPosterUrl(String mPosterUrl) {
        this.mPosterUrl = mPosterUrl;
    }

    public float getVote_average() {
        return mVote_average;
    }

    public void setVote_average(Integer mVote_average) {
        this.mVote_average = mVote_average;
    }

    public String getOverView() {
        return mOverView;
    }

    public void setOverView(String mOverView) {
        this.mOverView = mOverView;
    }

    @Override public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("***** Movie Entity Details *****\n");
        stringBuilder.append("id=" + this.getId() + "\n");
        stringBuilder.append("Title=" + this.getReleaseDate() + "\n");
        stringBuilder.append("Release Date=" + this.getReleaseDate() + "\n");
        stringBuilder.append("Poster Patch=" + this.getPosterUrl() + "\n");
        stringBuilder.append("Vote Average=" + this.getVote_average() + "\n");
        stringBuilder.append("Overview=" + this.getOverView() + "\n");
        stringBuilder.append("*******************************");

        return stringBuilder.toString();
    }
}
