package com.example.uschedule.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.uschedule.Database.UScheduleRepository;
import com.example.uschedule.Entities.AssessEntity;

import java.util.List;

public class AssessViewModel extends AndroidViewModel{
    int courseID;

    private UScheduleRepository mRepository;
    private LiveData<List<AssessEntity>> mAssociatedAssesss;
    private LiveData<List<AssessEntity>> mAllAssess;

    public AssessViewModel(Application application, int courseID) {
        super(application);
        mRepository = new UScheduleRepository(application);
        mAssociatedAssesss = mRepository.getmAssociateAssess(courseID);
    }

    public AssessViewModel(Application application) {
        super(application);
        mRepository = new UScheduleRepository(application);
        mAllAssess = mRepository.getmAllAssess();
        mAssociatedAssesss = mRepository.getmAssociateAssess(courseID);
    }

    public LiveData<List<AssessEntity>> getAssociatedAssesss(int courseID) {
        return mRepository.getmAssociateAssess(courseID);
    }

    public LiveData<List<AssessEntity>> getAllAssess() {
        return mAllAssess;
    }

    public void insert(AssessEntity assessEntity) {
        mRepository.insert(assessEntity);
    }

    public void delete(AssessEntity assessEntity) {mRepository.delete(assessEntity);}

    public int lastID() {
        return mAllAssess.getValue().size();
    }
}
