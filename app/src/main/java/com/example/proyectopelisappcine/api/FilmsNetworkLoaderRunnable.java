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
    FilmDatabase filmDatabase = FilmDatabase.getInstance(FilmsNetworkLoaderRunnable.this);
    FilmRepository filmRepository;

    public FilmsNetworkLoaderRunnable(String name, OnFilmsLoadedListener onFilmsLoadedListener) {
        pOnFilmsLoadedListener = onFilmsLoadedListener;
        pName = name;
    }

    @Override
    public void run() {
        // Instanciacion de Retrofit y llamada asíncrona
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FilmService service = retrofit.create(FilmService.class);

        filmRepository = FilmRepository.getInstance(filmDatabase.filmDAO());

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

                InsercionPeliculasEnBaseDeDatos(repositorio);
            }

            @Override
            public void onFailure(Call<Films> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    public void InsercionPeliculasEnBaseDeDatos(List<Film> films) {
        ///Verificar si las peliculas ya existen en la base de datos antes de insertarlas
        for (Film film : films) {
            if (filmRepository.getFilm(film.getId()) != null) {
                    InsercionPeliculaEnBaseDeDatos(film);
            }
        }
    }

    public void InsercionPeliculaEnBaseDeDatos(Film film) {
        filmRepository.InsertFilm(film);
    }

    //public boolean ExistePeliculaEnBaseDeDatos(int idfilm) {
    public boolean ExistePeliculaEnRepositorio(int idfilm) {
        if (filmRepository.getFilm(idfilm) != null) {
            return true;
        }
        else {
            return false;
        }
        /*FilmDatabase filmDatabase = FilmDatabase.getInstance(FilmsNetworkLoaderRunnable.this);
        if (filmDatabase.filmDAO().getFilm(idfilm) != null) {
            return true;
        }
        else {
            return false;
        }*/
    }
}