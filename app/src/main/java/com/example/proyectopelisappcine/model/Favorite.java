package com.example.proyectopelisappcine.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "Favorite", primaryKeys = {"idfilm", "usernamefilm"},
        foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "username", childColumns = "usernamefilm", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Film.class, parentColumns = "idfilm", childColumns = "idfilm", onDelete = ForeignKey.CASCADE)})
public class Favorite {
    @ColumnInfo(name = "idfilm")
    @NonNull
    private Integer id_film;
    @ColumnInfo(name = "usernamefilm")
    @NonNull
    private String username_film;

    public Integer getId_film() {
        return id_film;
    }

    public void setId_film(Integer id_film) {
        this.id_film = id_film;
    }

    @NonNull
    public String getUsername_film() {
        return username_film;
    }

    public void setUsername_film(@NonNull String username_film) {
        this.username_film = username_film;
    }

    public Favorite(Integer id_film, @NonNull String username_film) {
        this.id_film = id_film;
        this.username_film = username_film;
    }
}