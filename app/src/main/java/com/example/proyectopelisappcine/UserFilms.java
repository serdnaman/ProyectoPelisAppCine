package com.example.proyectopelisappcine;

import com.example.proyectopelisappcine.model.Film;

import java.util.HashMap;

public class UserFilms {
    private static UserFilms instance;

    public HashMap<Integer, Film> userfilmsfavorites = new HashMap<>();

    public static UserFilms getInstance() {
        if (instance == null) {
            instance = new UserFilms();
        }
        return instance;
    }
}
