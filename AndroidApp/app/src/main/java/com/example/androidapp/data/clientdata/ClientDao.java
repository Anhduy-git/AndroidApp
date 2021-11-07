package com.example.androidapp.data.clientdata;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidapp.data.menudata.Dish;

import java.util.List;

@Dao
public interface ClientDao {

    @Insert
    void insertClient(Client client);

    @Update
    void updateClient(Client client);

    @Delete
    void deleteClient(Client client);

    @Query("DELETE FROM client_table")
    void deleteAllClients();

    @Query("SELECT * FROM client_table")
    LiveData<List<Client>> getAllClients();
}