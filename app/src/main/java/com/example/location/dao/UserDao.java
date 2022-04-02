package com.example.location.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.location.entities.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void registerUser(User user);

    @Query("SELECT email FROM Users")
   List<String> email();

    @Query("SELECT password FROM Users WHERE email = :email")
    List<String> password(String email);

}
