package com.example.proyectopelisappcine.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.Observer;

import com.example.proyectopelisappcine.AppExecutors;
import com.example.proyectopelisappcine.model.User;
import com.example.proyectopelisappcine.roomdb.UserDAO;

import java.util.List;

public class UserRepository {
    private static UserRepository instance;
    private final UserDAO userDAO;
    private final MutableLiveData<String> userFilterLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> users;
    private String usuarioActual;

    public UserRepository(UserDAO userDAO) {
        this.userDAO = userDAO;
        users = new MutableLiveData<>();
    }

    public static UserRepository getInstance(UserDAO userDAO) {
        if (instance == null) {
            synchronized (UserRepository.class) {
                if (instance == null) {
                    instance = new UserRepository(userDAO);
                }
            }
        }
        return instance;
    }

    public void InsertUser(User user) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                userDAO.InsertUser(user);
            }
        });
    }

    public void UpdateUser(User user) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                userDAO.UpdateUser(user);
            }
        });
    }

    public void DeleteUser(User user) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                userDAO.DeleteUser(user);
            }
        });
    }

    public LiveData<User> getUser(String username) {
        return userDAO.getUserLiveData(username);
        //return Transformations.switchMap(userFilterLiveData, userDAO::getUserLiveData);

    }

    public void DeleteUserByUsername(String username) {
        //Se obtiene el LiveData del usuario que se quiere eliminar
        LiveData<User> userLiveData = userDAO.getUserLiveData(username);
        //Se crea un observador
        Observer<User> observer = new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            userDAO.DeleteUser(user);
                        }
                    }).start();
                    //Se detiene la observación después de realizar la acción
                    userLiveData.removeObserver(this);
                }
            }
        };
        //Ahora observa los cambios en el LiveData
        userLiveData.observeForever(observer);
    }

    public LiveData<Integer> getNumberOfUsers() {
        return userDAO.getNumberOfUsersLiveData();
    }

    public LiveData<List<User>> getUsers() {
        return userDAO.getUsersLiveData();
    }

    public String getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(String usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    /*public String getUsuarioIniciado() {EL USUARIO YA NO SE guarda, hay un problema con shared preferences, ver como arreglar esto
        if (sharedPreferences != null) {
            return sharedPreferences.getString("Usuario","");
        }
        else {
            return "";
        }
        return "";
    }*/

    public void setUsername(String username) {
        userFilterLiveData.setValue(username);
    }

    public LiveData<User> getUserAuth() {
        return Transformations.switchMap(userFilterLiveData, userDAO::getUserLiveData);
    }
}
