package com.example.proyectopelisappcine.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.proyectopelisappcine.model.Favorite;

@Dao
public interface FavoriteDAO {
    @Insert
    void InsertFavorites(Favorite favorite);

    @Delete
    void DeleteFavorites(Favorite favorite);

    @Query("SELECT * FROM Favorite WHERE idfilm = :idfilm AND usernamefilm = :usernamefilm")
    Favorite getFilmFromUser(int idfilm, String usernamefilm);

    @Query("SELECT * FROM Favorite WHERE idfilm = :idfilm AND usernamefilm = :usernamefilm")
    LiveData<Favorite> getFilmFromUserLiveData(int idfilm, String usernamefilm);

    @Query("SELECT EXISTS (SELECT 1 FROM Favorite WHERE idfilm = :idfilm AND usernamefilm = :usernamefilm LIMIT 1)")
    boolean isFilmInFavorites(int idfilm, String usernamefilm);
}