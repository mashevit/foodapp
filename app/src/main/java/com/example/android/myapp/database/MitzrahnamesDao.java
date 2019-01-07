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
public interface MitzrahnamesDao {

    @Query("SELECT * FROM mitzrahimnames")
    LiveData<List<Mitzrahnames>> loadAllingreds();

    @Query("SELECT * FROM mitzrahimnames")
    List<Mitzrahnames> loadAllingredsnr();

    @Insert
    void insertingreds(Mitzrahnames dish);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateingreds(Mitzrahnames dish);

    @Delete
    void deleteingreds(Mitzrahnames dish);

    @Query("SELECT * FROM mitzrahimnames WHERE ingredientidglobal LIKE :id")
    LiveData<Mitzrahnames> loadingredsById(String id);

    @Query("Delete FROM mitzrahimnames WHERE ingredientname = :name")
    int delingredsBynm(String name);


    @Query("SELECT Distinct * FROM MITZRAHIMNAMES "
            + "INNER JOIN MITZRAHIM ON MITZRAHIM.ingredientname1 LIKE mitzrahimnames.ingredientidglobal "
            + "INNER JOIN dishent ON dishent.id LIKE mitzrahim.dishId "
            + "WHERE dishent.id IN (:a) Group By MITZRAHIMNAMES.ingredientidglobal")
    LiveData<List<Mitzrahnames>> loadAllingredswithp(String[] a);

    @Query("SELECT MITZRAHIMNAMES.ingredientname FROM MITZRAHIMNAMES "
            + "INNER JOIN MITZRAHIM ON MITZRAHIM.ingredientname1 LIKE mitzrahimnames.ingredientidglobal "
            + "INNER JOIN dishent ON dishent.id LIKE mitzrahim.dishId "
            + "WHERE dishent.id LIKE :a")
    List<String> loadAllingredfordish(String a);

    @Query("SELECT * FROM mitzrahimnames inner Join mitzrahim On mitzrahim.ingredientname1 LIKE mitzrahimnames.ingredientidglobal  Where mitzrahim.dishId = :a")
    List<Mitzrahnames> loadAllingredswithp1(int a);


    @Query("SELECT * FROM mitzrahimnames WHERE ingredientname LIKE :name")
    List<Mitzrahnames> loadIngrediByname(String name);
}
