package com.virtu.popularmovies.domain.exception;

/**
 * Created by unai on 08/07/2015.
 */
public interface ErrorBundle {
    Exception getException();

    String getErrorMessage();
}
