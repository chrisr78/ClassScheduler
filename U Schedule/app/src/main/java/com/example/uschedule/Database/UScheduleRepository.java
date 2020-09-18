package com.example.uschedule.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.uschedule.DAO.AssessDAO;
import com.example.uschedule.DAO.CourseDAO;
import com.example.uschedule.DAO.TermDAO;
import com.example.uschedule.Entities.AssessEntity;
import com.example.uschedule.Entities.CourseEntity;
import com.example.uschedule.Entities.TermEntity;

import java.util.List;

public class UScheduleRepository {
    private UScheduleDatabase mDb;
    private CourseDAO mCourseDAO;
    private TermDAO mTermDAO;
    private AssessDAO mAssessDAO;
    private LiveData<List<CourseEntity>> mAllCourses;
    private LiveData<List<CourseEntity>> mAssociatedCourses;
    private LiveData<List<TermEntity>> mAllTerms;
    private LiveData<List<AssessEntity>> mAllAssess;
    private LiveData<List<AssessEntity>> mAssociateAssess;
    int termID;
    int courseID;


    public UScheduleRepository(Application application) {
        UScheduleDatabase db = UScheduleDatabase.getDatabase(application);
        mCourseDAO = db.courseDAO();
        mTermDAO = db.termDAO();
        mAssessDAO = db.assessDAO();
        mAllCourses = mCourseDAO.getAllCourses();
        mAssociatedCourses = mCourseDAO.getAllAssociatedCourses(termID);
        mAllTerms = mTermDAO.getAllTerms();
        mAllAssess = mAssessDAO.getAllAssess();
        mAssociateAssess = mAssessDAO.getAllAssociatedAssess(courseID);

    }

    public LiveData<List<CourseEntity>> getAllCourses() {
        return mAllCourses;
    }

    public LiveData<List<CourseEntity>> getAssociatedCourses(int termID) {
        return mAssociatedCourses;
    }

    public LiveData<List<TermEntity>> getAllTerms() {
        return mAllTerms;
    }

    public LiveData<List<AssessEntity>> getmAllAssess() {return mAllAssess; }

    public LiveData<List<AssessEntity>> getmAssociateAssess(int courseID) {
        return mAssociateAssess;
    }

    public TermEntity getTermByID(int termID) {
        return mDb.termDAO().getTermbyID(termID);
    }

    public void insert(CourseEntity courseEntity) {
        new insertAsyncTask1(mCourseDAO).execute(courseEntity);
    }

    public void insert(TermEntity termEntity) {
        new insertAsyncTask2(mTermDAO).execute(termEntity);
    }

    public void insert(AssessEntity assessEntity) {
        new insertAsyncTask3(mAssessDAO).execute(assessEntity);
    }

    public void delete(TermEntity termEntity) {
        new deleteAsyncTask1(mTermDAO).execute(termEntity);
    }

    public void delete(CourseEntity courseEntity) {
        new deleteAsyncTask2(mCourseDAO).execute(courseEntity);
    }

    public void delete(AssessEntity assessEntity) {
        new deleteAsyncTask3(mAssessDAO).execute(assessEntity);
    }

    private static class insertAsyncTask1 extends AsyncTask<CourseEntity, Void, Void> {

        private CourseDAO mAsyncTaskDao;

        insertAsyncTask1(CourseDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CourseEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class insertAsyncTask2 extends AsyncTask<TermEntity, Void, Void> {

        private TermDAO mAsyncTaskDao;

        insertAsyncTask2(TermDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TermEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class insertAsyncTask3 extends AsyncTask<AssessEntity, Void, Void> {

        private AssessDAO mAsyncTaskDao;

        insertAsyncTask3(AssessDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final AssessEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask1 extends AsyncTask<TermEntity, Void, Void>{

        private TermDAO mAsyncTaskDao;

        deleteAsyncTask1(TermDAO dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final TermEntity... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask2 extends AsyncTask<CourseEntity, Void, Void>{

        private CourseDAO mAsyncTaskDao;

        deleteAsyncTask2(CourseDAO dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final CourseEntity... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask3 extends AsyncTask<AssessEntity, Void, Void> {

        private AssessDAO mAsyncTaskDao;

        deleteAsyncTask3(AssessDAO dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final AssessEntity... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
