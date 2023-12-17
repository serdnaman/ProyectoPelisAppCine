package com.example.proyectopelisappcine;

import com.example.proyectopelisappcine.model.User;

public class UserManager {
    private static UserManager instance;
    private User usuarioActual;

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public User getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(User usuarioActual) {
        this.usuarioActual = usuarioActual;
    }
}