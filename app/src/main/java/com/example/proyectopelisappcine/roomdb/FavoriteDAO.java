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
}