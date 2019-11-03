package com.royalteck.progtobi.fintrakhr.LocalDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.royalteck.progtobi.fintrakhr.model.UserModel;

import java.util.List;

@Dao
public interface PatrecDAO {

    @Query("Select * from usermodel ORDER BY id DESC")
    List<UserModel> fetchUsers();

    @Insert()
    void insertToUser(UserModel userModel);

    @Query("DELETE FROM usermodel Where id = :id")
    void deleteUser(int id);


    @Query("UPDATE usermodel set name = :name, position = :position, salary = :salary WHERE id = :id")
    void updateUser(String name, String position, String salary, int id);

}


