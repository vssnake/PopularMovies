package com.virtu.popularmovies.presentation.view.connectors;

import com.virtu.popularmovies.presentation.model.MovieModelPresenter;

/**
 * Created by virtu on 13/07/2015.
 */
public interface MovieDetailsView  extends LoadDataView {

    public void renderMovieDetail(MovieModelPresenter movie);
}
