package com.virtu.popularmovies.data.exception;

import com.virtu.popularmovies.domain.exception.ErrorBundle;

/**
 * Created by virtu on 09/07/2015.
 */
public class RepositoryErrorBundle implements ErrorBundle {

    private final Exception mException;

    public RepositoryErrorBundle(Exception exception){
        this.mException = exception;
    }
    @Override
    public Exception getException() {
        return mException;
    }

    @Override
    public String getErrorMessage() {
        String message = "";
        if (this.mException != null){
            message = this.mException.getMessage();
        }
        return message;
    }
}
