package com.virtu.popularmovies.data.exception;

/**
 * Created by virtu on 09/07/2015.
 */
public class MovieNotFoundException extends Exception {

    public MovieNotFoundException() {
        super();
    }

    public MovieNotFoundException(final String message) {
        super(message);
    }

    public MovieNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MovieNotFoundException(final Throwable cause) {
        super(cause);
    }
}
