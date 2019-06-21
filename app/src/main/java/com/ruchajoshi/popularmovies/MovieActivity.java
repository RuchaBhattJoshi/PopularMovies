package com.ruchajoshi.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ruchajoshi.popularmovies.adapter.MovieAdapter;
import com.ruchajoshi.popularmovies.model.Movie;
import com.ruchajoshi.popularmovies.parsing.MovieDbJsonUtils;
import com.ruchajoshi.popularmovies.utils.NetworkUtils;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    @BindView(R.id.rv_movies)
     RecyclerView mDisplayMovieRecycleView;
    @BindView(R.id.layout_empty_view)
     ConstraintLayout mEmptyConstraintLayout;
    @BindView(R.id.progress_loading)
     ProgressBar mEmptyProgressbar;
    @BindView(R.id.textView_empty)
     TextView mEmptyTextview;

    private MovieAdapter mMovieAdapter;
    private Movie[] mMovieData;
    private String mQueryCategory = "popular";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        ButterKnife.bind(this);

        int mNoOfColumns = calculateNoOfColumns(getApplicationContext());

        GridLayoutManager layoutManager = new GridLayoutManager(this, mNoOfColumns);
        mDisplayMovieRecycleView.setLayoutManager(layoutManager);
        mDisplayMovieRecycleView.setHasFixedSize(true);
        mDisplayMovieRecycleView.setAdapter(mMovieAdapter);

        loadMovieData();
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    private void loadMovieData() {
        String theMovieDbQueryType = mQueryCategory;
        showResult();
        new FetchMovieTask().execute(theMovieDbQueryType);
    }

    private void showResult() {
        mEmptyConstraintLayout.setVisibility(View.INVISIBLE);
        mDisplayMovieRecycleView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mDisplayMovieRecycleView.setVisibility(View.INVISIBLE);
        mEmptyConstraintLayout.setVisibility(View.VISIBLE);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mEmptyProgressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            if (params.length == 0){
                return null;
            }

            String sortBy = params[0];
            URL movieRequestUrl = NetworkUtils.buildUrl(sortBy);

            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                mMovieData = MovieDbJsonUtils.getMoviesFromJson(MovieActivity.this, jsonMovieResponse);
                return mMovieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movieData) {
            mEmptyProgressbar.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                showResult();
                mMovieAdapter = new MovieAdapter(getApplicationContext(),movieData,MovieActivity.this);
                mDisplayMovieRecycleView.setAdapter(mMovieAdapter);
            } else {
                showErrorMessage();
            }
        }
    }


    @Override
    public void onClick(int adapterPosition) {
        Intent intentToDeatilActivity = new Intent(this, DetailActivity.class);
        intentToDeatilActivity.putExtra(Intent.EXTRA_TEXT, adapterPosition);
        intentToDeatilActivity.putExtra("title", mMovieData[adapterPosition].getMovieTitle());
        intentToDeatilActivity.putExtra("poster", mMovieData[adapterPosition].getMoviePoster());
        intentToDeatilActivity.putExtra("user_rate", mMovieData[adapterPosition].getMovieUserRating());
        intentToDeatilActivity.putExtra("release_date", mMovieData[adapterPosition].getMovieReleaseDate());
        intentToDeatilActivity.putExtra("overview", mMovieData[adapterPosition].getMovieOverview());

        Log.d("mposter","poster"+mMovieData[adapterPosition].getMoviePoster());
        Log.d("mposition","pos"+mMovieData[adapterPosition]);

        startActivity(intentToDeatilActivity);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies_category_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemSelected = item.getItemId();

        if (menuItemSelected == R.id.action_most_popular) {
            mQueryCategory = "popular";
            loadMovieData();
            return true;
        }

        if (menuItemSelected == R.id.action_top_rated) {
            mQueryCategory = "top_rated";
            loadMovieData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
