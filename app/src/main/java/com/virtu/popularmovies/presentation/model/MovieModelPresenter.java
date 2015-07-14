package com.virtu.popularmovies.presentation.model;

/**
 * Created by virtu on 25/06/2015.
 */
public class MovieModelPresenter{


    public MovieModelPresenter(Long movieID){
        this.mId = movieID;
    }

    private Long mId;
    private String mTitle;
    private String mReleaseDate;
    private String mPosterUrl;
    private float mVote_average;
    private String mOverView;

    public Long getId() {
        return mId;
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

    public void setVote_average(float mVote_average) {
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

        stringBuilder.append("***** MovieModelPresenter Details *****\n");
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
