package com.example.proyectopelisappcine;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.proyectopelisappcine.model.Film;
import com.example.proyectopelisappcine.model.User;
import com.example.proyectopelisappcine.repository.FavoriteRepository;
import com.example.proyectopelisappcine.repository.FilmRepository;
import com.example.proyectopelisappcine.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {
    private UserRepository userRepository;
    private FilmRepository filmRepository;
    private FavoriteRepository favoriteRepository;
    private MutableLiveData<List<User>> users;
    private MutableLiveData<List<Film>> films;
    private LiveData<User> user;
    private LiveData<Film> film;
    private MutableLiveData<Boolean> mStateAuthentication;
    private String usuarioActual;

    public MainViewModel(UserRepository userRepository, FilmRepository filmRepository, FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
        this.favoriteRepository = favoriteRepository;
        //String username = userRepository.getUsuarioActual();
        String username = getUsuarioActual();
        user = userRepository.getUser(username);// Reemplaza con el nombre de usuario deseado o pásalo como parámetro según tu lógica
        film = filmRepository.getFilm("Titulo de pelicula", "Fecha de pelicula");
        mStateAuthentication = new MutableLiveData<>();
        mStateAuthentication.setValue(false);
    }

    public LiveData<List<User>> getUsers() {
        return users;
    }

    public LiveData<List<Film>> getFilms() {
        return films;
    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<Film> getFilm() {
        return film;
    }

    public void setUser(LiveData<User> user) {
        this.user = user;
    }

    public void setFilm(LiveData<Film> film) {
        this.film = film;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public FilmRepository getFilmRepository() {
        return filmRepository;
    }

    public FavoriteRepository getFavoriteRepository() {
        return favoriteRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setFilmRepository(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public void setFavoriteRepository(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public LiveData<Boolean> getStateAuthentication() {
        return mStateAuthentication;
    }

    public void setStateAuthentication(Boolean state) {
        mStateAuthentication.setValue(state);
    }

    public void AutenticarUsuario(String username, String password) {
        userRepository.getUserAuth().observeForever(user -> {
            if (user != null && user.getPassword().equals(password)) {
                mStateAuthentication.setValue(true);

                //Se almancena el nombre del usuario
                setUsuarioActual(username);
            }
            else {
                mStateAuthentication.setValue(false);
            }
        });
        userRepository.setUsername(username);
    }

    public String getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(String usuarioActual) {
        this.usuarioActual = usuarioActual;
    }


    public void RegistroUsuario(String username, String password, String email) {
        User user = new User(username, password, email);
        userRepository.InsertUser(user);
    }

    public void ModificarUsuario(String username, String password, String email) {
        User user = new User(username, password, email);
        userRepository.UpdateUser(user);
    }

    public LiveData<User> getUserByUsername(String username) {
        //Se obtiene el LiveData del usuario
        LiveData<User> userLiveData = userRepository.getUser(username);
        //Se observa el LiveData del usuario
        userLiveData.observeForever(new Observer<User>() {
            @Override
            public void onChanged(User user) {
                //Cuando se cambia el usuario, se actualiza el LiveData
                setUser(userLiveData);
                userLiveData.removeObserver(this);
            }
        });
        return userLiveData;
    }

    public void DeleteUserByUsername(String username) {
        userRepository.DeleteUserByUsername(username);
    }

    /*public void cargarPeliculas() {
        FilmsNetworkLoaderRunnable filmsNetworkLoaderRunnable = new FilmsNetworkLoaderRunnable("Usuario", films -> {
            this.films.postValue(films.getResults());
            for (Film film : films.getResults()) {
                if (filmRepository.getFilm(film.getId()) != null) {
                    filmRepository.InsertFilm(film);
                }
            }
        });
        Thread thread = new Thread(filmsNetworkLoaderRunnable);
        thread.start();
    }*/

    public void filtrarPeliculas(String newText) {
        //Se crea una lista para almacenar las peliculas filtradas
        List<Film> filmsFiltradas = new ArrayList<>();
        List<Film> filmList = films.getValue();

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
        films.postValue(filmsFiltradas);
    }

    public void CerrarSesion() {
        //limpieza de los datos de la sesion del usuario actual
        //userRepository.setUsuarioActual("");
        //setUsuarioActual("");

        //Se notifica que el usuario ha cerrado sesion
        //mStateAuthentication.setValue(false);

        //Limpieza de datos de la sesión del usuario actual
       // setUser(null);

        //Se notifica que el usuario ha cerrado sesion
        setStateAuthentication(false);
    }
}
