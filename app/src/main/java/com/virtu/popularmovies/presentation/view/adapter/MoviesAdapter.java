package com.virtu.popularmovies.presentation.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.virtu.spotifystreamer.R;
import com.squareup.picasso.Picasso;
import com.virtu.popularmovies.presentation.model.MovieModelPresenter;

import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by virtu on 11/07/2015.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private List<MovieModelPresenter> mMoviesCollection;
    private final LayoutInflater mLayoutInflater;

    private final Context mContext;

    private OnItemClickListener onItemClickListener;

    public MoviesAdapter(Context context,Collection<MovieModelPresenter> moviesCollection){
        this.mContext = context.getApplicationContext();
        validateUsersCollection(moviesCollection);
        this.mLayoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.mLayoutInflater.inflate(R.layout.column_movie,parent, false);
        MoviesViewHolder moviesViewHolder = new MoviesViewHolder(view);
        return moviesViewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        final MovieModelPresenter movieModelPresenter = this.mMoviesCollection.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MoviesAdapter.this.onItemClickListener != null){
                    MoviesAdapter.this.onItemClickListener.onUserItemClicked(movieModelPresenter);
                }
            }
        });
        Picasso.with(mContext).load(movieModelPresenter.getPosterUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return (this.mMoviesCollection != null) ? this.mMoviesCollection.size() : 0;
    }

    public void setMoviesCollection(Collection<MovieModelPresenter> moviesCollection){
        this.validateUsersCollection(moviesCollection);
        this.mMoviesCollection = (List<MovieModelPresenter>) moviesCollection;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    private void validateUsersCollection(Collection<MovieModelPresenter> moviesCollection) {
        if (moviesCollection == null) {
            throw new IllegalArgumentException("The list cannot be null");
        }else{
            this.mMoviesCollection = (List<MovieModelPresenter>) moviesCollection;
            this.notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener {
        void onUserItemClicked(MovieModelPresenter userModel);
    }

    static class MoviesViewHolder extends RecyclerView.ViewHolder{


        @Bind(R.id.colum_movie_image)
        ImageView imageView;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
