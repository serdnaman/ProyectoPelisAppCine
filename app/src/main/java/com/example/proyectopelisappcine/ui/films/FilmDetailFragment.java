package com.example.proyectopelisappcine.ui.films;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.navigation.NavController;

import com.bumptech.glide.Glide;
import com.example.proyectopelisappcine.MainViewModel;
import com.example.proyectopelisappcine.MainViewModelFactory;
import com.example.proyectopelisappcine.R;
import com.example.proyectopelisappcine.UserFilms;
import com.example.proyectopelisappcine.databinding.FragmentFilmDetailBinding;
import com.example.proyectopelisappcine.model.Favorite;
import com.example.proyectopelisappcine.model.Film;
import com.example.proyectopelisappcine.model.User;
import com.example.proyectopelisappcine.repository.FavoriteRepository;
import com.example.proyectopelisappcine.repository.FilmRepository;
import com.example.proyectopelisappcine.repository.UserRepository;
import com.example.proyectopelisappcine.roomdb.FilmDatabase;

public class FilmDetailFragment extends Fragment {
    private FragmentFilmDetailBinding binding;
    private SharedPreferences preferences;
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
    private static final Object cerradura = new Object();
    private final User user = FilmDatabase.getUser();//Se obtiene el usuario de la base de datos//VER SI HAY QUE CAMBIAR ESTO
    FilmDatabase filmDatabase;
    FilmRepository filmRepository;
    FavoriteRepository favoriteRepository;
    UserRepository userRepository;
    private MainViewModel mainViewModel;

    public interface FilmListener {
        Film getFilm();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        filmDatabase = FilmDatabase.getInstance(getContext());
        filmRepository = FilmRepository.getInstance(filmDatabase.filmDAO());
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

        //Añadir o quitar de la lista de favoritos
        anadir_favoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (!EstaPeliculaEnFavoritos()) {
                if (pelicula != null && mainViewModel.getFilmRepository().getFilm(pelicula.getId()) != null) {
                    AnadirAFavoritos();
                    anadir_favoritos.setText(R.string.addFavorites);
                    Toast.makeText(getActivity(), "La pelicula ha sido añadida a favoritos", Toast.LENGTH_SHORT).show();
                }
                else {
                    EliminarDeFavoritos();
                    anadir_favoritos.setText(R.string.removeFavorites);
                    Toast.makeText(getActivity(), "La pelicula ha sido eliminada de favoritos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }

    public boolean EstaPeliculaEnFavoritos() {
        /*UserFilms userFilms = UserFilms.getInstance();
        if (userFilms.userfilmsfavorites.get(pelicula.getId()) != null) {
            return true;
        }
        else {
            return false;
        }*/
        if (pelicula != null && mainViewModel.getFilmRepository().getFilm(pelicula.getId()) != null) {
            return true;
        }
        else {
            return false;
        }
    }

    public void AnadirAFavoritos() {
        UserFilms userFilms = UserFilms.getInstance();
        userFilms.userfilmsfavorites.put(pelicula.getId(), pelicula);
        favoriteRepository.InsertFavorite(new Favorite(pelicula.getId(), user.getUsername()));
    }

    public void EliminarDeFavoritos() {
        UserFilms userFilms = UserFilms.getInstance();
        userFilms.userfilmsfavorites.remove(pelicula.getId());
        favoriteRepository.DeleteFavorite(new Favorite(pelicula.getId(), user.getUsername()));
    }

    public void ActualizarInterfazUsuario(Film film) {
        this.pelicula = film;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //if (!EstaPeliculaEnFavoritos()) {
                if (pelicula != null && mainViewModel.getFilmRepository().getFilm(pelicula.getId()) != null) {
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
                Glide.with(getContext()).load("https://image.tmdb.org/t/p/original/"+film.getPosterPath()).into(poster);
            }
        });
    }
}