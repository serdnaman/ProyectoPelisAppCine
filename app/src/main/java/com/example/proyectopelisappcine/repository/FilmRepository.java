package com.example.proyectopelisappcine.repository;

import androidx.lifecycle.LiveData;

import com.example.proyectopelisappcine.AppExecutors;
import com.example.proyectopelisappcine.model.Film;
import com.example.proyectopelisappcine.roomdb.FilmDAO;

import java.util.List;

public class FilmRepository {
    private static FilmRepository instance;
    private final FilmDAO filmDAO;

    public FilmRepository(FilmDAO filmDAO) {
        this.filmDAO = filmDAO;
    }

    public static FilmRepository getInstance(FilmDAO filmDAO) {
        if (instance == null) {
            synchronized (FilmRepository.class) {
                if (instance == null) {
                    instance = new FilmRepository(filmDAO);
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
}
