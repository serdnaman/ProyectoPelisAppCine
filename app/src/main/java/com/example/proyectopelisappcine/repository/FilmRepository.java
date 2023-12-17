package com.example.proyectopelisappcine.repository;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.proyectopelisappcine.AppExecutors;
import com.example.proyectopelisappcine.api.FilmsNetworkDataSource;
import com.example.proyectopelisappcine.model.Film;
import com.example.proyectopelisappcine.roomdb.FilmDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilmRepository {
    private static FilmRepository instance;
    private final FilmDAO filmDAO;
    private final FilmsNetworkDataSource filmsNetworkDataSource;
    private final MutableLiveData<String> filterLiveData = new MutableLiveData<>();
    private final Map<String, Long> ultimaActualizacionTiempoMilisegundos = new HashMap<>();
    private static final long MIN_TIME_FROM_LAST_FETCH_MILLIS = 30000;

    public FilmRepository(FilmDAO filmDAO, FilmsNetworkDataSource filmsNetworkDataSource) {
        this.filmDAO = filmDAO;
        this.filmsNetworkDataSource = filmsNetworkDataSource;

        LiveData<List<Film>> filmsNetwork = this.filmsNetworkDataSource.getCurrentFilms();
        if (filmsNetwork != null) {
            filmsNetwork.observeForever(films -> {
                Log.d("FilmRepository", "Received films from network: " + films.size() + " films");
                AppExecutors.getInstance().diskIO().execute(() -> {
                    if (films.size() > 0) {
                        Log.d("FilmRepository", "Deleting all films from local database");
                        this.filmDAO.deleteAllFilms();
                    }
                    Log.d("FilmRepository", "Inserting films into local database");
                    this.filmDAO.insertListFilms(films);
                });
            });
        }
    }

    public static FilmRepository getInstance(FilmDAO filmDAO, FilmsNetworkDataSource filmsNetworkDataSource) {
        if (instance == null) {
            synchronized (FilmRepository.class) {
                if (instance == null) {
                    instance = new FilmRepository(filmDAO, filmsNetworkDataSource);
                }
            }
        }
        return instance;
    }

    public void InsertFilm(Film film) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                filmDAO.insertFilm(film);
            }
        });
    }

    public void UpdateFilm(Film film) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                filmDAO.updateFilm(film);
            }
        });
    }

    public void DeleteFilm(Film film) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                filmDAO.deleteFilm(film);
            }
        });
    }

    public LiveData<Film> getFilm(int idfilm) {
        return filmDAO.getFilmLiveData(idfilm);
    }

    public LiveData<Film> getFilm(String title, String releaseDate) {
        return filmDAO.getFilmLiveData(title, releaseDate);
    }

    public LiveData<List<Film>> getAllFilms() {
        return filmDAO.getAllFilmsLiveData();
    }

    public LiveData<Integer> getNumberOfFilms() {
        return filmDAO.getNumberOfFilmsLiveData();
    }

    public LiveData<List<Film>> getAllFavoritesFilmsForUser(String username) {
        return filmDAO.getAllFavoritesFilmsForUserLiveData(username);
    }

    public void doFetchFilms() {
        Log.d("FilmRepository", "Fetching films from network");
        AppExecutors.getInstance().diskIO().execute(() -> {
            filmsNetworkDataSource.fetchFilms();
            filmDAO.deleteAllFilms();
        });
    }

    public LiveData<List<Film>> getCurrentFilms() {
        return Transformations.switchMap(filterLiveData, new Function<String, LiveData<List<Film>>>() {
            @Override
            public LiveData<List<Film>> apply(String input) {
                return filmDAO.getAllFilmsLiveData();
            }
        });
    }

    public boolean BusquedaNecesaria(String username) {
        Long ultimaBusquedaMilisegundos = ultimaActualizacionTiempoMilisegundos.get(username);
        ultimaBusquedaMilisegundos = ultimaBusquedaMilisegundos == null ? 0L : ultimaBusquedaMilisegundos;
        long tiempoDesdeUltimaBusqueda = System.currentTimeMillis() - ultimaBusquedaMilisegundos;
        return filmDAO.getNumberOfFilms() == 0 || tiempoDesdeUltimaBusqueda > MIN_TIME_FROM_LAST_FETCH_MILLIS;
    }

    public void doFetchFilms(String username) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            filmDAO.deleteAllFilms();
            filmsNetworkDataSource.fetchFilms();
            ultimaActualizacionTiempoMilisegundos.put(username, System.currentTimeMillis());
        });
    }

    public void InsertFilms(List<Film> films) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                filmDAO.insertListFilms(films);
            }
        });
    }
}