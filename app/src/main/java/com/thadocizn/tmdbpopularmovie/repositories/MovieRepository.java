package com.thadocizn.tmdbpopularmovie.repositories;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;

import com.thadocizn.tmdbpopularmovie.R;
import com.thadocizn.tmdbpopularmovie.model.Movie;
import com.thadocizn.tmdbpopularmovie.model.MovieDbResponse;
import com.thadocizn.tmdbpopularmovie.service.MovieDataService;
import com.thadocizn.tmdbpopularmovie.service.RetroInstance;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by charles on 12,June,2019
 */
public class MovieRepository {

    private Application application;
    private Observable<MovieDbResponse> movieDbResponseObservable;
    private ArrayList<Movie> movies;
    private MutableLiveData<List<Movie>> mutableLiveData = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    public MovieRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<Movie>> getMovieList(){
        movies = new ArrayList<>();
        MovieDataService movieDataService = RetroInstance.getServices();

        movieDbResponseObservable = movieDataService.getPopularMovies(application.getApplicationContext().getString(R.string.api_key));

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

                        mutableLiveData.postValue(movies);
                    }
                }));
        return mutableLiveData;
    }

    public void clear(){
        disposable.clear();
    }
}
