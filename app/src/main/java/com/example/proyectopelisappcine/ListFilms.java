package com.example.proyectopelisappcine;

import com.example.proyectopelisappcine.model.Film;

import java.util.HashMap;

public class ListFilms {
    private static ListFilms instance;
    public HashMap<Integer, Film> listfilms = new HashMap<>();

    public static ListFilms getInstance() {
        if (instance == null) {
            instance = new ListFilms();
        }
        return instance;
    }
}