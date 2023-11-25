package com.example.proyectopelisappcine.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
public class User {

    @PrimaryKey
    @ColumnInfo(name = "username")
    @NonNull
    private String username;
    private String password;
    private String email;

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(){
        this.username = null;
        this.password = null;
        this.email = null;
    }

    public User(@NonNull String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
