package com.example.android.myapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DishDao {


    @Query("SELECT * FROM dishent")
    LiveData<List<Dish>> loadAllDishes();

    @Insert
    void insertDish(Dish dish);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDish(Dish dish);

    @Delete
    void deleteDish(Dish dish);

    @Query("SELECT * FROM dishent WHERE id = :id")
   Dish loadDishByIdw(String id);


@Query("SELECT * FROM dishent WHERE id = :id")
    LiveData<Dish> loadDishById(String id);
    @Query("SELECT * FROM dishent WHERE id LIKE :id")
    Dish loadDishByIdnor(String id);
    @Query("Delete FROM dishent WHERE dishname = :name")
    int delDishBynm(String name);

    @Query("SELECT * FROM dishent WHERE dishname LIKE :name")
    List<Dish> loadDishByname(String name);



}
