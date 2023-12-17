package com.example.proyectopelisappcine.ui.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectopelisappcine.MainActivity;
import com.example.proyectopelisappcine.MainViewModel;
import com.example.proyectopelisappcine.R;
import com.example.proyectopelisappcine.UserFilms;
import com.example.proyectopelisappcine.adapters.FilmsAdapter;
import com.example.proyectopelisappcine.api.FilmsNetworkDataSource;
import com.example.proyectopelisappcine.model.Film;
import com.example.proyectopelisappcine.repository.FilmRepository;
import com.example.proyectopelisappcine.roomdb.FilmDatabase;

import java.util.ArrayList;
import java.util.List;

public class FavoritosFragment extends Fragment {
    private FilmsAdapter filmsAdapter;
    private List<Film> filmList;
    FilmDatabase filmDatabase;
    FilmRepository filmRepository;
    private MainViewModel mainViewModel;
    FilmsNetworkDataSource filmsNetworkDataSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_film_list_favorites,container,false);

        filmDatabase = FilmDatabase.getInstance(getContext());

        filmsNetworkDataSource = FilmsNetworkDataSource.getInstance();

        filmRepository = FilmRepository.getInstance(filmDatabase.filmDAO(), filmsNetworkDataSource);

        // Obtén el MainViewModel de la MainActivity
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            mainViewModel = ((MainActivity) getActivity()).getMainViewModel();
        }

        //Obtencion de la coleccion de peliculas favoritos
        UserFilms userFilms = UserFilms.getInstance();
        filmList = new ArrayList<>(userFilms.userfilmsfavorites.values());

        //Construccion de un adaptador para la coleccion de las peliculas favoritas
        filmsAdapter = new FilmsAdapter(filmList, R.layout.content_film_list_favorites,getContext());

        //Se obtiene el layout que contiene los detalles de las peliculas favoritas
        View view = root.findViewById(R.id.fragment_film_list_favorites);
        assert view != null;
        setupRecyclerView((RecyclerView) view);

        return root;
    }

    public void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(filmsAdapter);
    }

    @Override
    public void onResume() {
        UserFilms userFilms = UserFilms.getInstance();
        List<Film> filmList = new ArrayList<>(userFilms.userfilmsfavorites.values());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                filmsAdapter.swap(filmList);
            }
        });
        super.onResume();
    }
}