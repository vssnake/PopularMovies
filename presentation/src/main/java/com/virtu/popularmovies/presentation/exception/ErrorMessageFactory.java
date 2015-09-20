package com.virtu.popularmovies.presentation.exception;

import android.content.Context;
import android.util.Log;

import com.virtu.popularmovies.presentation.R;
import com.virtu.popularmovies.data.exception.MovieNotFoundException;
import com.virtu.popularmovies.data.exception.NetworkConnectionException;

/**
 * Created by virtu on 11/07/2015.
 */
public class ErrorMessageFactory {

    static final String TAG = ErrorMessageFactory.class.getSimpleName();

    private ErrorMessageFactory() {
        //empty Constructor
    }

    public static String create(Context context, Exception exception) {

        String message = context.getString(R.string.exception_message_generic);

        if (exception instanceof NetworkConnectionException) {
            message = context.getString(R.string.exception_message_no_connection);
        } else if (exception instanceof MovieNotFoundException) {
            message = context.getString(R.string.exception_message_movie_not_found);
        }

        exception.printStackTrace();

        Log.e(TAG,exception.toString(),exception);

        return message;
    }
}
