package com.example.uschedule.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.uschedule.Entities.TermEntity;

import java.util.List;

@Dao
public interface TermDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TermEntity term);

    @Query("DELETE FROM Terms")
    void deleteAllTerms();

    @Delete
    void delete(TermEntity term);

    @Query("SELECT * FROM Terms where termID = :id")
    TermEntity getTermbyID(int id);

    @Query("SELECT * FROM Terms ORDER BY termID ASC")
    LiveData<List<TermEntity>> getAllTerms();
}
