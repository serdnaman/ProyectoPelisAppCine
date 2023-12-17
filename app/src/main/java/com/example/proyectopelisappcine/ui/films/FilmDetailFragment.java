package com.example.proyectopelisappcine.ui.films;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.proyectopelisappcine.MainViewModel;
import com.example.proyectopelisappcine.MainViewModelFactory;
import com.example.proyectopelisappcine.R;
import com.example.proyectopelisappcine.UserFilms;
import com.example.proyectopelisappcine.UserManager;
import com.example.proyectopelisappcine.api.FilmsNetworkDataSource;
import com.example.proyectopelisappcine.databinding.FragmentFilmDetailBinding;
import com.example.proyectopelisappcine.model.Film;
import com.example.proyectopelisappcine.model.User;
import com.example.proyectopelisappcine.repository.FavoriteRepository;
import com.example.proyectopelisappcine.repository.FilmRepository;
import com.example.proyectopelisappcine.repository.UserRepository;
import com.example.proyectopelisappcine.roomdb.FilmDatabase;

public class FilmDetailFragment extends Fragment {
    private FragmentFilmDetailBinding binding;
    private Film pelicula;
    private TextView titulo;
    private TextView fecha_lanzamiento;
    private TextView titulo_original;
    private TextView descripcion;
    private TextView popularidad;
    private TextView promedio_votos;
    private TextView conteo_votos;
    private ImageView poster;
    private Button anadir_favoritos;
    private FilmListener filmListener;
    private User user;
    FilmDatabase filmDatabase;
    FilmRepository filmRepository;
    FavoriteRepository favoriteRepository;
    UserRepository userRepository;
    private MainViewModel mainViewModel;
    FilmsNetworkDataSource filmsNetworkDataSource;

    public interface FilmListener {
        Film getFilm();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Creación de una instancia de MainViewModel pasando los repositorios
        MainViewModelFactory mainViewModelFactory = new MainViewModelFactory(userRepository, filmRepository, favoriteRepository);
        mainViewModel = new ViewModelProvider(this, mainViewModelFactory).get(MainViewModel.class);

        if (mainViewModel.getFilm() != null) {
            mainViewModel.getFilm().observe(getViewLifecycleOwner(), film -> {
                if (film != null) {
                    titulo.setText(film.getTitle());
                    fecha_lanzamiento.setText(film.getReleaseDate());
                    titulo_original.setText(film.getOriginalTitle());
                    descripcion.setText(film.getOverview());
                    popularidad.setText(String.valueOf(film.getPopularity()));
                    promedio_votos.setText(String.valueOf(film.getVoteAverage()));
                    conteo_votos.setText(String.valueOf(film.getVoteCount()));
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFilmDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        filmDatabase = FilmDatabase.getInstance(getContext());
        filmsNetworkDataSource = FilmsNetworkDataSource.getInstance();
        filmRepository = FilmRepository.getInstance(filmDatabase.filmDAO(), filmsNetworkDataSource);
        favoriteRepository = FavoriteRepository.getInstance(filmDatabase.favoriteDAO());
        userRepository = UserRepository.getInstance(filmDatabase.userDAO());

        titulo = root.findViewById(R.id.film_title);
        fecha_lanzamiento = root.findViewById(R.id.film_fecha);
        titulo_original = root.findViewById(R.id.original_film_title);
        descripcion = root.findViewById(R.id.film_description);
        popularidad = root.findViewById(R.id.film_popularity);
        promedio_votos = root.findViewById(R.id.film_vote_average);
        conteo_votos = root.findViewById(R.id.film_vote_count);
        poster = root.findViewById(R.id.poster_detalle);
        anadir_favoritos = root.findViewById(R.id.anadir_favoritos);

        //Se obtiene el usuario actual de la aplicacion
        user = UserManager.getInstance().getUsuarioActual();

        //Añadir o quitar de la lista de favoritos
        anadir_favoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!EstaPeliculaEnFavoritos()) {
                    AnadirAFavoritos();
                    Toast.makeText(getActivity(), "La película ha sido añadida a favoritos", Toast.LENGTH_SHORT).show();
                    anadir_favoritos.setText(R.string.removeFavorites);
                }
                else {
                    EliminarDeFavoritos();
                    Toast.makeText(getActivity(), "La película ha sido eliminada de favoritos", Toast.LENGTH_SHORT).show();
                    anadir_favoritos.setText(R.string.addFavorites);
                }
            }
        });
        return root;
    }

    public boolean EstaPeliculaEnFavoritos() {
        UserFilms userFilms = UserFilms.getInstance();
        if (userFilms.userfilmsfavorites.get(pelicula.getId()) != null) {
            return true;
        }
        else {
            return false;
        }
    }

    public void AnadirAFavoritos() {
        if (pelicula != null) {
            UserFilms userFilms = UserFilms.getInstance();
            userFilms.userfilmsfavorites.put(pelicula.getId(), pelicula);
            if (user != null) {
                mainViewModel.InsertarEnFavoritos(pelicula.getId(), user.getUsername());
            }
            else {
                Toast.makeText(getActivity(), "El usuario que ha accedido a la pelicula es nulo", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getActivity(), "La pelicula a la que se ha accedido es nula", Toast.LENGTH_SHORT).show();
        }
    }

    public void EliminarDeFavoritos() {
        if (pelicula != null) {
            UserFilms userFilms = UserFilms.getInstance();
            userFilms.userfilmsfavorites.remove(pelicula.getId());
            if (user != null) {
                mainViewModel.BorrarEnFavoritos(pelicula.getId(), user.getUsername());
            }
            else {
                Toast.makeText(getActivity(), "El usuario que ha accedido a la pelicula es nulo", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getActivity(), "La pelicula a la que se ha accedido es nula", Toast.LENGTH_SHORT).show();
        }
    }

    public void ActualizarInterfazUsuario(Film film) {
        if (film != null) {
            this.pelicula = film;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!EstaPeliculaEnFavoritos()) {
                        anadir_favoritos.setText(R.string.addFavorites);
                    }
                    else {
                        anadir_favoritos.setText(R.string.removeFavorites);
                    }
                    titulo.setText(film.getTitle());
                    fecha_lanzamiento.setText(film.getReleaseDate());
                    titulo_original.setText(film.getOriginalTitle());
                    descripcion.setText(film.getOverview());
                    popularidad.setText(String.valueOf(film.getPopularity()));
                    promedio_votos.setText(String.valueOf(film.getVoteAverage()));
                    conteo_votos.setText(String.valueOf(film.getVoteCount()));
                    Glide.with(getContext()).load("https://image.tmdb.org/t/p/original/" + film.getPosterPath()).into(poster);
                }
            });
        }
    }
}