package com.example.proyectopelisappcine.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proyectopelisappcine.model.Film;

import java.util.List;

@Dao
public interface FilmDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFilm(Film film);

    @Update
    void updateFilm(Film film);

    @Delete
    void deleteFilm(Film film);

    @Query("SELECT * FROM Film WHERE idfilm = :idfilm")
    Film getFilm(int idfilm);

    @Query("SELECT * FROM Film WHERE idfilm = :idfilm")
    LiveData<Film> getFilmLiveData(int idfilm);

    @Query("SELECT * FROM Film WHERE title = :title AND releaseDate = :releaseDate")
    Film getFilm(String title, String releaseDate);

    @Query("SELECT * FROM Film WHERE title = :title AND releaseDate = :releaseDate")
    LiveData<Film> getFilmLiveData(String title, String releaseDate);

    @Query("SELECT count(*) FROM Film")
    int getNumberOfFilms();

    @Query("SELECT count(*) FROM Film")
    LiveData<Integer> getNumberOfFilmsLiveData();

    @Query("SELECT * FROM Film")
    List<Film> getAllFilms();
    
    @Query("SELECT * FROM Film")
    LiveData<List<Film>> getAllFilmsLiveData();

    @Query("SELECT p.title, p.releaseDate FROM Film p JOIN Favorite f ON p.idfilm = f.idfilm WHERE f.usernamefilm = :username")
    List<Film> getAllFavoritesFilmsForUser(String username);

    @Query("SELECT p.title, p.releaseDate FROM Film p JOIN Favorite f ON p.idfilm = f.idfilm WHERE f.usernamefilm = :username")
    LiveData<List<Film>> getAllFavoritesFilmsForUserLiveData(String username);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListFilms(List<Film> filmList);

    @Query("DELETE FROM Film")
    void deleteAllFilms();
}