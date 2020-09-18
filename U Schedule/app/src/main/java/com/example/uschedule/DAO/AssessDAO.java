package com.example.uschedule.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.uschedule.Entities.AssessEntity;

import java.util.List;

@Dao
public interface AssessDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AssessEntity assess);

    @Delete
    void delete(AssessEntity assess);

    @Query("DELETE FROM Assessments")
    void deleteAllAssess();

    @Query("SELECT * FROM Assessments ORDER BY assessID ASC")
    LiveData<List<AssessEntity>> getAllAssess();

    @Query("SELECT * FROM Assessments WHERE courseID = :courseID ORDER BY assessID ASC")
    LiveData<List<AssessEntity>> getAllAssociatedAssess(int courseID);
}
