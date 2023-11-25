package com.example.proyectopelisappcine.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.proyectopelisappcine.model.Favorite;
import com.example.proyectopelisappcine.model.Film;
import com.example.proyectopelisappcine.model.User;

@Database(entities = {User.class, Film.class, Favorite.class}, version = 1)
public abstract class FilmDatabase extends RoomDatabase {
    public static volatile FilmDatabase instance;

    private static User usuario = new User();

    public static FilmDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (FilmDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, FilmDatabase.class,"Films.db").build();
                }
            }
        }
        return instance;
    }

    public static User getUser() {
        return usuario;
    }

    public static void setUser(User usuario) {
        FilmDatabase.usuario = new User(usuario.getUsername(), usuario.getPassword(), usuario.getEmail());
    }

    public abstract UserDAO userDAO();
    public abstract FilmDAO filmDAO();
    public abstract FavoriteDAO favoriteDAO();
}
