package com.virtu.popularmovies.presentation.view.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by virtu on 11/07/2015.
 */
public class MoviesLayoutManager extends GridLayoutManager {
    public MoviesLayoutManager(Context context,int numberColumns) {
        super(context, numberColumns);
        generateDefaultLayoutParams().setMargins(0,0,0,0);
    }
}
