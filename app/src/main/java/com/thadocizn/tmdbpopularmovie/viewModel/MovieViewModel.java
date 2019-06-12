package com.thadocizn.tmdbpopularmovie.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.thadocizn.tmdbpopularmovie.model.Movie;
import com.thadocizn.tmdbpopularmovie.repositories.MovieRepository;

import java.util.List;

/**
 * Created by charles on 12,June,2019
 */
public class MovieViewModel extends AndroidViewModel {

    private MovieRepository repository;

    public MovieViewModel(@NonNull Application application) {

        super(application);
        repository = new MovieRepository(application);
    }

    public LiveData<List<Movie>> getAllMovies(){
        return repository.getMovieList();
    }

    public void clear(){
        repository.clear();
    }

}
