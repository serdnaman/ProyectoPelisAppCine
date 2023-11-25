package com.example.proyectopelisappcine.repository;

import com.example.proyectopelisappcine.AppExecutors;
import com.example.proyectopelisappcine.model.Favorite;
import com.example.proyectopelisappcine.roomdb.FavoriteDAO;

public class FavoriteRepository {
    private static FavoriteRepository instance;
    private final FavoriteDAO favoriteDAO;

    public FavoriteRepository(FavoriteDAO favoriteDAO) {
        this.favoriteDAO = favoriteDAO;
    }

    public static FavoriteRepository getInstance(FavoriteDAO favoriteDAO) {
        if (instance == null) {
            synchronized (FavoriteRepository.class) {
                if (instance == null) {
                    instance = new FavoriteRepository(favoriteDAO);
                }
            }
        }
        return instance;
    }

    public void InsertFavorite(Favorite favorite) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                favoriteDAO.InsertFavorites(favorite);
            }
        });
    }

    public void DeleteFavorite(Favorite favorite) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                favoriteDAO.DeleteFavorites(favorite);
            }
        });
    }
}
