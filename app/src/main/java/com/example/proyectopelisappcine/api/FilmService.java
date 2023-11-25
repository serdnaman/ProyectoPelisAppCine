package com.example.proyectopelisappcine.api;

import com.example.proyectopelisappcine.model.Films;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FilmService {
    @GET("3/movie/now_playing")
    Call<Films> getFilms(@Query("api_key") String key, @Query("language") String language);
}
