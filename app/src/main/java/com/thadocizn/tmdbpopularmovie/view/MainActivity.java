package com.thadocizn.tmdbpopularmovie.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.thadocizn.tmdbpopularmovie.R;
import com.thadocizn.tmdbpopularmovie.adapter.MovieAdapter;
import com.thadocizn.tmdbpopularmovie.model.Movie;
import com.thadocizn.tmdbpopularmovie.model.MovieDbResponse;
import com.thadocizn.tmdbpopularmovie.service.MovieDataService;
import com.thadocizn.tmdbpopularmovie.service.RetroInstance;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Movie> movies;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private SwipeRefreshLayout refreshLayout;
    private Observable<MovieDbResponse> movieDbResponseObservable;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("TMDB Popular Movies Today");

        getPopularMovies();

        refreshLayout = findViewById(R.id.swipe_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPopularMovies();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

    public void getPopularMovies() {

        movies = new ArrayList<>();
        MovieDataService movieDataService = RetroInstance.getServices();

        movieDbResponseObservable = movieDataService.getPopularMovies(this.getString(R.string.api_key));

        disposable.add(movieDbResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<MovieDbResponse, ObservableSource<Movie>>() {
                    @Override
                    public ObservableSource<Movie> apply(MovieDbResponse movieDbResponse) throws Exception {
                        return Observable.fromArray(movieDbResponse.getMovies().toArray(new Movie[0]));
                    }
                })
                .filter(new Predicate<Movie>() {
                    @Override
                    public boolean test(Movie movie) throws Exception {
                        return movie.getVoteAverage() > 7.0;
                    }
                })
                .subscribeWith(new DisposableObserver<Movie>() {

                    @Override
                    public void onNext(Movie movie) {

                        movies.add(movie);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                        init();
                    }
                }));
    }

    public void init() {

        recyclerView = findViewById(R.id.rvMovies);
        movieAdapter = new MovieAdapter(this, movies);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {

            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();

    }
}
