package com.example.proyectopelisappcine.ui.films;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectopelisappcine.AppExecutors;
import com.example.proyectopelisappcine.ListFilms;
import com.example.proyectopelisappcine.MainActivity;
import com.example.proyectopelisappcine.MainViewModel;
import com.example.proyectopelisappcine.R;
import com.example.proyectopelisappcine.adapters.FilmsAdapter;
import com.example.proyectopelisappcine.api.FilmsNetworkDataSource;
import com.example.proyectopelisappcine.api.FilmsNetworkLoaderRunnable;
import com.example.proyectopelisappcine.model.Film;
import com.example.proyectopelisappcine.repository.FilmRepository;
import com.example.proyectopelisappcine.roomdb.FilmDatabase;

import java.util.ArrayList;
import java.util.List;

public class FilmListFragment extends Fragment implements SearchView.OnQueryTextListener {
    private FilmsAdapter filmsAdapter;
    private SearchView buscador;
    private List<Film> filmList;
    private List<Film> filmsFiltradas;
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
        View root = inflater.inflate(R.layout.fragment_film_list, container, false);

        filmDatabase = FilmDatabase.getInstance(getContext());
        filmsNetworkDataSource = FilmsNetworkDataSource.getInstance();
        filmRepository = FilmRepository.getInstance(filmDatabase.filmDAO(), filmsNetworkDataSource);
        filmRepository.getCurrentFilms().observe(getActivity(), new Observer<List<Film>>() {
            @Override
            public void onChanged(List<Film> films) {
                onFilmsLoaded(films);
            }
        });

        // Obtén el MainViewModel de la MainActivity
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            mainViewModel = ((MainActivity) getActivity()).getMainViewModel();
        }

        //Obtencion de la coleccion de peliculas
        ListFilms listFilms = ListFilms.getInstance();
        filmList = new ArrayList<>(listFilms.listfilms.values());

        filmsFiltradas = new ArrayList<>(filmList);
        //Construccion de un adaptador para la coleccion de las peliculas
        filmsAdapter = new FilmsAdapter(filmsFiltradas, R.layout.content_film_list, getContext());
        RecyclerView recyclerView = root.findViewById(R.id.fragment_film_list);
        setupRecyclerView(recyclerView);

        buscador = root.findViewById(R.id.buscador);

        if (buscador != null) {
            buscador.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //Se crea una lista para almacenar las peliculas filtradas
                    List<Film> filmsFiltradas = new ArrayList<>();

                    ObtenerDatosPeliculas();

                    for (Film film : filmList) {
                        String pelicula = film.getTitle();
                        //Se pasa a minusculas para la comparacion tanto la cadena de busqueda como el titulo de la pelicula
                        newText = newText.toLowerCase();
                        pelicula = pelicula.toLowerCase();

                        //Se comprueba que la cadena de busqueda esta contenida en el titulo de la pelicula
                        if (pelicula.contains(newText)) {
                            filmsFiltradas.add(film);
                        }
                    }

                    filmsAdapter.swap(filmsFiltradas);

                    return false;
                }
            });
        }

        return root;
    }

    public void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(filmsAdapter);
    }

    @Override
    public void onResume() {
        if (filmList.isEmpty()) {
            AppExecutors.getInstance().networkIO().execute(new FilmsNetworkLoaderRunnable("Usuario",
                    films -> filmsAdapter.swap(films.getResults())));
        }
        super.onResume();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    /*@Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        //Se crea una lista para almacenar las peliculas filtradas
        filmsFiltradas.clear();
        for (Film film : filmList) {
            String pelicula = film.getTitle().toLowerCase();
            //Se comprueba que la cadena de busqueda esta contenida en el titulo de la pelicula
            if (pelicula.contains(newText)) {
                filmsFiltradas.add(film);
            }
        }

        //Se notifican los cambios en el adaptador después de haber filtrado todas las peliculas
        filmsAdapter.notifyDataSetChanged();
        return true;
    }*/

    public void onFilmsLoaded(List<Film> films) {
        filmsAdapter.swap(films);
    }

    public void ObtenerDatosPeliculas() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                FilmDatabase filmDatabase = FilmDatabase.getInstance(getContext());
                filmList = filmDatabase.filmDAO().getAllFilms();
            }
        });
    }
}