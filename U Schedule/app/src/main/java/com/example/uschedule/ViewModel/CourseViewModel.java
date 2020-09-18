package com.example.uschedule.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.uschedule.Database.UScheduleRepository;
import com.example.uschedule.Entities.CourseEntity;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {

    int termID;

    private UScheduleRepository mRepository;
    private LiveData<List<CourseEntity>> mAssociatedCourses;
    private LiveData<List<CourseEntity>> mAllCourses;

    public CourseViewModel(Application application, int termID) {
        super(application);
        mRepository = new UScheduleRepository(application);
        mAssociatedCourses = mRepository.getAssociatedCourses(termID);
    }

    public CourseViewModel(Application application) {
        super(application);
        mRepository = new UScheduleRepository(application);
        mAllCourses = mRepository.getAllCourses();
        mAssociatedCourses = mRepository.getAssociatedCourses(termID);
    }

    public LiveData<List<CourseEntity>> getAssociatedCourses(int termID) {
        return mRepository.getAssociatedCourses(termID);
    }

    public void delete(CourseEntity courseEntity) {mRepository.delete(courseEntity);}

    public LiveData<List<CourseEntity>> getAllCourses() {
        return mAllCourses;
    }

    public void insert(CourseEntity courseEntity) {
        mRepository.insert(courseEntity);
    }

    public int lastID() {
        return mAllCourses.getValue().size();
    }
}
