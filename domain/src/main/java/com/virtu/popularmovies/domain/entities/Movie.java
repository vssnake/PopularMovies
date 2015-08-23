package com.virtu.popularmovies.domain.entities;

import java.util.List;

/**
 * Created by virtu on 25/06/2015.
 */
public class Movie {


    public static final String API_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";

    public static final String API_W300_ = "w300/";

    public Movie(Long movieID){
        this.mId = movieID;
    }

    private Long mId;
    private String mTitle;
    private String mReleaseDate;
    private String mPosterUrl;
    private float mVote_average;
    private String mOverView;
    private List<Video> mVideos;
    private List<Review> mReviews;

    public Long getId() {return mId;}

    public String getTitle() {return mTitle;}

    public void setTitle(String mTitle) {this.mTitle = mTitle;}

    public String getReleaseDate() {return mReleaseDate;}

    public void setReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public void setPosterUrl(String mPosterUrl) {
        this.mPosterUrl = API_IMAGE_BASE_URL + API_W300_ +mPosterUrl;
    }

    public float getVote_average() {
        return mVote_average;
    }

    public void setVote_average(float mVote_average) {
        this.mVote_average = mVote_average;
    }

    public String getOverView() {
        return mOverView;
    }

    public void setOverView(String mOverView) {this.mOverView = mOverView;}
    public List<Video> gemVideos() {return mVideos;}

    public void setVideos(List<Video> mVideos) {this.mVideos = mVideos;}

    public List<Review> getReviews() {return mReviews;}

    public void setReviews(List<Review> mReviews) {this.mReviews = mReviews;}

    @Override public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("***** Movie Details *****\n");
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
