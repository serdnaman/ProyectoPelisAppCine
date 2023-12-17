package com.example.proyectopelisappcine;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectopelisappcine.repository.FavoriteRepository;
import com.example.proyectopelisappcine.repository.FilmRepository;
import com.example.proyectopelisappcine.repository.UserRepository;

public class MainViewModelFactory implements ViewModelProvider.Factory {
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final FavoriteRepository favoriteRepository;

    public MainViewModelFactory(UserRepository userRepository, FilmRepository filmRepository, FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(userRepository, filmRepository, favoriteRepository);
        }
        throw new IllegalArgumentException("Clase ViewModel desconocida");
    }
}