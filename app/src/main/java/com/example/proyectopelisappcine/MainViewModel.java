package com.example.proyectopelisappcine;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectopelisappcine.model.Favorite;
import com.example.proyectopelisappcine.model.Film;
import com.example.proyectopelisappcine.model.User;
import com.example.proyectopelisappcine.repository.FavoriteRepository;
import com.example.proyectopelisappcine.repository.FilmRepository;
import com.example.proyectopelisappcine.repository.UserRepository;

public class MainViewModel extends ViewModel {
    private UserRepository userRepository;
    private FilmRepository filmRepository;
    private FavoriteRepository favoriteRepository;
    private LiveData<User> user;
    private LiveData<Film> film;
    private MutableLiveData<Boolean> mStateAuthentication;
    private String usuarioActual;
    private MutableLiveData<Boolean> mStateVerification;

    public MainViewModel(UserRepository userRepository, FilmRepository filmRepository, FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
        this.favoriteRepository = favoriteRepository;
        user = userRepository.getUser("username");// Reemplaza con el nombre de usuario deseado o pásalo como parámetro según tu lógica
        film = filmRepository.getFilm("Titulo de pelicula", "Fecha de pelicula");
        mStateAuthentication = new MutableLiveData<>();
        mStateAuthentication.setValue(false);
        mStateVerification = new MutableLiveData<>();
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

    public LiveData<Boolean> getStateAuthentication() {
        return mStateAuthentication;
    }

    public void setStateAuthentication(Boolean state) {
        mStateAuthentication.setValue(state);
    }

    public LiveData<Boolean> getStateVerification() {
        return mStateVerification;
    }

    public void setStateVerification(boolean state) {
        this.mStateVerification = mStateVerification;
    }

    public void AutenticarUsuario(String username, String password) {
        userRepository.getUserAuth().observeForever(user -> {
            if (user != null && user.getPassword().equals(password)) {
                mStateAuthentication.setValue(true);
                //Se almacena el nombre del usuario
                setUsuarioActual(username);
            }
            else {
                mStateAuthentication.setValue(false);
            }
        });
        userRepository.setUsername(username);
    }

    public void VerificarUsuario(String username) {
        userRepository.getUserAuth().observeForever(user -> {
            if (user != null) {
                mStateVerification.setValue(true);
            }
            else {
                mStateVerification.setValue(false);
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
        //Se actualiza el usuario actual de la aplicacion
        UserManager.getInstance().setUsuarioActual(user);
    }

    public void ModificarUsuario(String username, String password, String email) {
        User user = new User(username, password, email);
        userRepository.UpdateUser(user);
    }

    public void InsertarEnFavoritos(Integer id_film, String username_film) {
        Favorite favorite = new Favorite(id_film, username_film);//EL MAINVIWEMODEL VA POR ACTIVIDSADES, POR ESOS ES QUE NO SE GUARDA EL USUARIO PARA OTRAS ACTIVIDADES, PORQUE SOLO SE TIENE EN ESA ACTIVDSAD
        favoriteRepository.InsertFavorite(favorite);
    }

    public void BorrarEnFavoritos(Integer id_film, String username_film) {
        Favorite favorite = new Favorite(id_film, username_film);
        favoriteRepository.DeleteFavorite(favorite);
    }

    public void DeleteUserByUsername(String username) {
        userRepository.DeleteUserByUsername(username);
    }

    public void CerrarSesion() {
        //Se notifica que el usuario ha cerrado sesion
        setStateAuthentication(false);
    }
}