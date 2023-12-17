package com.example.proyectopelisappcine.api;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectopelisappcine.AppExecutors;
import com.example.proyectopelisappcine.model.Film;
import com.example.proyectopelisappcine.model.Films;
import com.example.proyectopelisappcine.repository.FilmRepository;
import com.example.proyectopelisappcine.roomdb.FilmDatabase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FilmsNetworkLoaderRunnable extends AppCompatActivity implements Runnable {
    private final OnFilmsLoadedListener pOnFilmsLoadedListener;
    private final String pName;
    private static final String claveAPI = "2f8cf95083b723fe47d0b71b93a1728c";
    private static final String language = "es-ES";
    private static final String url = "https://api.themoviedb.org";
    FilmRepository filmRepository;
    FilmDatabase filmDatabase;
    FilmsNetworkDataSource filmsNetworkDataSource;

    public FilmsNetworkLoaderRunnable(String name, OnFilmsLoadedListener onFilmsLoadedListener) {
        pOnFilmsLoadedListener = onFilmsLoadedListener;
        pName = name;
        filmDatabase = FilmDatabase.getInstance(FilmsNetworkLoaderRunnable.this);
        filmsNetworkDataSource = FilmsNetworkDataSource.getInstance();
        filmRepository = FilmRepository.getInstance(filmDatabase.filmDAO(),filmsNetworkDataSource);
    }

    @Override
    public void run() {
        // Instanciacion de Retrofit y llamada asíncrona
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FilmService service = retrofit.create(FilmService.class);

        Call<Films> call = service.getFilms(claveAPI, language);

        call.enqueue(new Callback<Films>() {
            @Override
            public void onResponse(Call<Films> call, Response<Films> response) {
                Films repos = response.body() == null ? new Films() : response.body();
                Log.d("Andres", String.valueOf(response.code()));
                Log.d("Andres", response.toString());
                AppExecutors.getInstance().mainThread().execute(() -> pOnFilmsLoadedListener.onFilmLoaded(repos));

                assert response.body() != null; //Se comprueba si es nulo
                List<Film> repositorio = response.body().getResults();

                for (Film film : repositorio) {
                    filmRepository.InsertFilm(film);
                }
            }

            @Override
            public void onFailure(Call<Films> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}