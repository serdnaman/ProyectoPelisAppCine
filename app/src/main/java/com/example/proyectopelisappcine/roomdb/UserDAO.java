package com.example.proyectopelisappcine.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proyectopelisappcine.model.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert
    void InsertUser(User user);

    @Update
    void UpdateUser(User user);

    @Delete
    void DeleteUser(User user);

    @Query("SELECT * FROM User WHERE username = :username")
    User getUser(String username);

    @Query("SELECT * FROM User WHERE username = :username")
    LiveData<User> getUserLiveData(String username);

    @Query("DELETE FROM User WHERE username = :username")
    void DeleteUserByUsername(String username);

    @Query("DELETE FROM User WHERE username = :username")
    void DeleteUserByUsernameLiveData(String username);

    @Query("UPDATE User SET username = :newusername, password = :newpassword, email = :newemail WHERE username = :username")
    void UpdateUserChanges(String username, String newusername, String newpassword, String newemail);

    @Query("SELECT count(*) FROM User")
    int getNumberOfUsers();

    @Query("SELECT count(*) FROM User")
    LiveData<Integer> getNumberOfUsersLiveData();

    @Query("SELECT * FROM User")
    List<User> getUsers();

    @Query("SELECT * FROM User")
    LiveData<List<User>> getUsersLiveData();
}