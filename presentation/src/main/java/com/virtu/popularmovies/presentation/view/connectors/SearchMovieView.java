package com.virtu.popularmovies.presentation.view.connectors;

import com.virtu.popularmovies.presentation.model.MovieModelPresenter;

import java.util.Collection;

/**
 * Created by virtu on 11/07/2015.
 */
public interface SearchMovieView extends LoadDataView {

    void renderMoviesList(Collection<MovieModelPresenter> moviesModelCollection);

    void viewMovie(MovieModelPresenter movieModel);
}
