package com.virtu.popularmovies.presentation.view.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by virtu on 11/07/2015.
 */
public class NoSpacingItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = 0;
        outRect.left = 0;
        outRect.right = 0;
        outRect.top = 0;
    }

}
