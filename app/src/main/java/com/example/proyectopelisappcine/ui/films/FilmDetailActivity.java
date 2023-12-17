package com.example.proyectopelisappcine.ui.films;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectopelisappcine.AppExecutors;
import com.example.proyectopelisappcine.R;
import com.example.proyectopelisappcine.model.Film;
import com.example.proyectopelisappcine.roomdb.FilmDatabase;

public class FilmDetailActivity extends AppCompatActivity implements FilmDetailFragment.FilmListener {
    private Film film;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);
        //Se obtiene la pelicula de la que se quiere mostrar informacion
        film = (Film) getIntent().getSerializableExtra("Pelicula");
        //Se recuperan los datos de la pelicula
        ObtenerDatosPelicula();
    }

    @Override
    public Film getFilm() {
        return film;
    }

    public void ObtenerDatosPelicula() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                FilmDatabase filmDatabase = FilmDatabase.getInstance(FilmDetailActivity.this);
                film = filmDatabase.filmDAO().getFilm(film.getId());
                FilmDetailFragment filmDetailFragment = (FilmDetailFragment) getSupportFragmentManager().findFragmentById(R.id.details_container);//CON ESTO YA PARECE COGER EL FRAGMENTO DESPUES DE HACER EL DEBUG
                if (filmDetailFragment != null) {
                    filmDetailFragment.ActualizarInterfazUsuario(film);
                }
            }
        });
    }
}