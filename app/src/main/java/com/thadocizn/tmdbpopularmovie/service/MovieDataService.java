package com.thadocizn.tmdbpopularmovie.service;

import com.thadocizn.tmdbpopularmovie.model.MovieDbResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by charles on 08,June,2019
 */
public interface MovieDataService {

    @GET("movie/popular")
    Observable<MovieDbResponse> getPopularMovies(@Query("api_key") String apiKey);
}
