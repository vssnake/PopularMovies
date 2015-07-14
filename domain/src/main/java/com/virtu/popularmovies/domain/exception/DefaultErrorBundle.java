package com.virtu.popularmovies.domain.exception;

/**
 * Created by unai on 08/07/2015.
 */
public class DefaultErrorBundle implements ErrorBundle {

    private final Exception mException;

    public DefaultErrorBundle(Exception exception){
        this.mException = exception;
    }

    @Override
    public Exception getException() {
        return mException;
    }

    @Override
    public String getErrorMessage() {
        String message ="";
        if (mException != null) this.mException.getMessage();
        return message;
    }
}
