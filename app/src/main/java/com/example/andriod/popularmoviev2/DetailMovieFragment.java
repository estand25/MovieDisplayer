package com.example.andriod.popularmoviev2;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.andriod.popularmoviev2.model.Genres;
import com.example.andriod.popularmoviev2.model.Movie;
import com.example.andriod.popularmoviev2.model.Review;
import com.example.andriod.popularmoviev2.model.ReviewColl;
import com.example.andriod.popularmoviev2.model.Trailer;
import com.example.andriod.popularmoviev2.model.TrailerColl;
import com.example.andriod.popularmoviev2.service.TheMovieDBAPIService;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailMovieFragment extends Fragment {
    // Local class Log tag variable
    private final String LOG_TAG = DetailMovieFragment.class.getSimpleName();
    HashMap<Integer,String> genreList = new HashMap<Integer, String>();

    private String movieRoot = "http://api.themoviedb.org/3/";

    public DetailMovieFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get the current DetailActivityFragment view layout
        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // Create a local instance of Intent and get all the information that was past
        // from the activity
        Intent intent = getActivity().getIntent();

        // Create a local String Array from the received intent data
        final Movie onClickMovie = intent.getParcelableExtra("movie");

        // Get the ImageView from the current layout
        ImageView posterView = (ImageView) rootView.findViewById(R.id.detail_imageView);

        // Replaced Picassa with AQery per the below form post. The image were loading to slow
        //so I looked and found a soluation (https://discussions.udacity.com/t/picassa-image-caching-and-loading/175512)
        AQuery aq = new AQuery(posterView);
        aq.id(R.id.detail_imageView).image(onClickMovie.getPosterPath()).visible();

        /// Get the TextView from the current layout and set the text
        // to what appears at position 0 in the string array
        TextView titleText = (TextView) rootView.findViewById(R.id.detail_textView1);
        titleText.setText(onClickMovie.getOriginalTitle());

        // Get the TextView from the current layout and set the text
        // to what appears at position 2 in the string array
        TextView synopsisText = (TextView) rootView.findViewById(R.id.detail_textView2);
        synopsisText.setText(onClickMovie.getOverview());

        // Get the TextView from the current layout and set the text
        // to what appears at position 3 in the string array
        TextView userRatingText = (TextView) rootView.findViewById(R.id.detail_textView3);
        userRatingText.setText(onClickMovie.getVoteAverage().toString());

        // Get the TextView from the current layout and set the text
        // to what appears at position 4 in the string array
        final TextView releaseDateText = (TextView) rootView.findViewById(R.id.detail_textView4);
        releaseDateText.setText(onClickMovie.getReleaseDate());

        // Create an instance of the framework that creates the Url and converter the json to gson
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(movieRoot)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Uses the framework services to connect to the API
        TheMovieDBAPIService service = retrofit.create(TheMovieDBAPIService.class);

        Call<Genres> jsonMovieGenres = service.getMovieGenres(BuildConfig.THE_MOVIE_DB_API);
        jsonMovieGenres.enqueue(new Callback<Genres>() {
            @Override
            public void onResponse(Call<Genres> call, Response<Genres> response) {
                genreList = response.body().genreMap();

                //  Local String variable for Genre
                String genres = "";

                for(int i =0;i<onClickMovie.getGenreIds().size();i++){
                    if(genres.isEmpty()){
                        genres = genreList.get(onClickMovie.getGenreIds().get(i));
                    }else {
                        genres = genres + "|" + genreList.get(onClickMovie.getGenreIds().get(i));
                    }
                }

                Log.v("OnResponse - Genres ",genres);

                // Get the TextView from the current layout and set the text
                // to what appears at position 6 in the string array
                TextView genreText = (TextView) rootView.findViewById(R.id.detail_textView5);
                genreText.setText(genres);
            }

            @Override
            public void onFailure(Call<Genres> call, Throwable t) {
                Snackbar.make(getView(), "Movie Genres Detail not error!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        Call<ReviewColl> jsonMovieReview = service.getMovieReview(onClickMovie.getId(),BuildConfig.THE_MOVIE_DB_API);
        jsonMovieReview.enqueue(new Callback<ReviewColl>() {
            @Override
            public void onResponse(Call<ReviewColl> call, Response<ReviewColl> response) {
                List<Review> reviews = response.body().getReviews();
                String reviewList = "";

                for(int i = 0;i<reviews.size();i++){
                    if(reviewList.isEmpty()){
                        reviewList = reviews.get(i).getAuthor()+" :"+reviews.get(i).getContent();
                    }else{
                        reviewList = reviewList+"\n "+reviews.get(i).getAuthor()+" :"+reviews.get(i).getContent();
                    }
                }

                Log.v("OnResponse - Review ",reviewList);

                // Get the TextView from the current layout and set the text
                // to what appears at position 6 in the string array
                TextView genreText = (TextView) rootView.findViewById(R.id.detail_textView6);
                genreText.setText(reviewList);
            }

            @Override
            public void onFailure(Call<ReviewColl> call, Throwable t) {
                Snackbar.make(getView(), "Movie has not been review yet!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Call<TrailerColl> jsonMovieTrailer = service.getMovieTrailer(onClickMovie.getId(),BuildConfig.THE_MOVIE_DB_API);
        jsonMovieTrailer.enqueue(new Callback<TrailerColl>() {
            @Override
            public void onResponse(Call<TrailerColl> call, Response<TrailerColl> response) {
                List<Trailer> movieTrailers = response.body().getTrailers();
                String trailers = "";

                for(int i = 0;i<movieTrailers.size();i++){
                    if(trailers.isEmpty()){
                        trailers = " Site: "+movieTrailers.get(i).getSite()+" Trailer Name:"+movieTrailers.get(i).getName();
                    }else{
                        trailers = trailers +"\n "+ "Site: "+movieTrailers.get(i).getSite()+" Trailer Name:"+movieTrailers.get(i).getName();
                    }
                }

                Log.v("OnResponse - Trailers ",trailers);

                // Get the TextView from the current layout and set the text
                // to what appears at position 6 in the string array
                TextView genreText = (TextView) rootView.findViewById(R.id.detail_textView7);
                genreText.setText(trailers);

                /*
                    Code to trigger youtube with selected trailer
                    Found on Stackoverflow - http://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
                    public static void watchYoutubeVideo(String id){
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
                            startActivity(intent);
                        } catch (ActivityNotFoundException ex) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://www.youtube.com/watch?v=" + id));
                            startActivity(intent);
                        }
                    }
                 */
            }

            @Override
            public void onFailure(Call<TrailerColl> call, Throwable t) {
                Snackbar.make(getView(), "No Movie Trailer Available!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return rootView;
    }

    /*
        My first try at getting the name for the genres
        I need help here because I'm using a lot of memery
     */
    public String getGenreName(String line){
        String result = "";
        result = line.substring(1, line.length()-1);
        return result;
    }
}
