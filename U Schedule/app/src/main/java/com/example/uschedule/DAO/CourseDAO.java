package com.example.uschedule.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.uschedule.Entities.CourseEntity;

import java.util.List;

@Dao
public interface CourseDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CourseEntity course);

    @Delete
    void delete(CourseEntity course);

    @Query("DELETE FROM Courses")
    void deleteAllCourses();

    @Query("SELECT * FROM Courses ORDER BY courseID ASC")
    LiveData<List<CourseEntity>> getAllCourses();

    @Query("SELECT * FROM Courses WHERE termID= :termID ORDER BY courseID ASC")
    LiveData<List<CourseEntity>> getAllAssociatedCourses(int termID);

    @Query("SELECT COUNT(*) FROM courses")
    int getRowCount();
}
