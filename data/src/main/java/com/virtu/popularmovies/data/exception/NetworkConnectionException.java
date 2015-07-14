package com.virtu.popularmovies.data.exception;

/**
 * Created by virtu on 09/07/2015.
 */
public class NetworkConnectionException extends Exception {

    public NetworkConnectionException(){
        super();
    }

    public NetworkConnectionException(final String message) {
        super(message);
    }

    public NetworkConnectionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NetworkConnectionException(final Throwable cause) {
        super(cause);
    }


}
