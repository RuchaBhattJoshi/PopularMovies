package com.ruchajoshi.popularmovies;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    /*@BindView(R.id.progress_loading)
     ProgressBar mProgressbar;*/
    @BindView(R.id.img_movie_cover)
     ImageView mMovieCover;
    @BindView(R.id.img_movie_poster)
     ImageView mMoviePoster;
    @BindView(R.id.tv_title)
     TextView mMovieTitle;
    @BindView(R.id.tv_release_date)
     TextView mMovieReleseDate;
    @BindView(R.id.tv_user_rating)
     TextView mMovieUserRating;
    @BindView(R.id.tv_synopsis)
     TextView mMovieOverView;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        if(getIntent().getExtras() != null){
            String poster = getIntent().getStringExtra("poster");
            String title = getIntent().getStringExtra("title");
            String rate = getIntent().getStringExtra("user_rate");
            String release = getIntent().getStringExtra("release_date");
            String overview = getIntent().getStringExtra("overview");

            mMovieTitle.setText(title);
            mMovieOverView.setText(overview);
            mMovieUserRating.setText(rate + "/10");
            mMovieReleseDate.setText(release);

            Picasso.get()
                    .load(poster)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(mMovieCover);

            Picasso.get()
                    .load(poster)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(mMoviePoster);

        }else{
            Log.d("no data","poster");
        }

    }
}
