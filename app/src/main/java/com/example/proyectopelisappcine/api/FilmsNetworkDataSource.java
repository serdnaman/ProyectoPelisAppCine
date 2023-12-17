package com.example.proyectopelisappcine.api;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.proyectopelisappcine.AppExecutors;
import com.example.proyectopelisappcine.model.Film;

import java.util.List;

public class FilmsNetworkDataSource {
    private static FilmsNetworkDataSource instance;
    private final MutableLiveData<List<Film>> films;
    private static final String LOG_TAG = FilmsNetworkDataSource.class.getSimpleName();

    public FilmsNetworkDataSource() {
        films = new MutableLiveData<>();
    }

    public synchronized static FilmsNetworkDataSource getInstance() {
        if (instance == null) {
            synchronized (FilmsNetworkDataSource.class) {
                if (instance == null) {
                    instance = new FilmsNetworkDataSource();
                }
            }
        }
        return instance;
    }

    public LiveData<List<Film>> getCurrentFilms() {
        return films;
    }

    public void fetchFilms() {
        Log.d(LOG_TAG,"Comenzando extraccion de películas");
        AppExecutors.getInstance().networkIO().execute(new FilmsNetworkLoaderRunnable("Usuario",
                films -> this.films.postValue(films.getResults())));
    }
}