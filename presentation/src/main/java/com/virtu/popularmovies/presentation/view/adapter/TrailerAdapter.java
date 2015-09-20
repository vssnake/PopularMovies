package com.virtu.popularmovies.presentation.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.virtu.popularmovies.domain.entities.Video;
import com.virtu.popularmovies.presentation.R;
import com.virtu.popularmovies.presentation.model.MovieModelPresenter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by virtu on 19/09/2015.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>{

    List<Video> mMovieDetailsVideo;
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;

    public TrailerAdapter(MovieModelPresenter movieDetails,Context context){
       this.mMovieDetailsVideo = movieDetails.getVideos();
        this.mLayoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.mLayoutInflater.inflate(R.layout.trailer_item,parent,false);
        TrailerViewHolder trailerViewHolder = new TrailerViewHolder(view);
        return trailerViewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
            holder.title.setText(mMovieDetailsVideo.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mMovieDetailsVideo.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.trailer_item_name)
        TextView title;

        @OnClick(R.id.trailer_item_container)
        public void onContainerClick(){
           // String videoId = "VideoIDGoesHere";
            //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
            //intent.putExtra("VIDEO_ID", mMovieDetailsVideo.get(getAdapterPosition()).getKeyVideo());
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" +
                            "" + mMovieDetailsVideo.get(getAdapterPosition()).getKeyVideo()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            /*mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" +
                                    "" + mMovieDetailsVideo.get(getAdapterPosition()).getKeyVideo())));*/
            mContext.startActivity(intent);
           // mContext.startActivity(intent);

        }

        public TrailerViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }
}
