package com.example.uschedule.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.uschedule.DAO.AssessDAO;
import com.example.uschedule.DAO.CourseDAO;
import com.example.uschedule.DAO.TermDAO;
import com.example.uschedule.Entities.AssessEntity;
import com.example.uschedule.Entities.CourseEntity;
import com.example.uschedule.Entities.TermEntity;


@Database(entities = {CourseEntity.class, TermEntity.class, AssessEntity.class}, version = 9)

public abstract class UScheduleDatabase extends RoomDatabase {
    private static volatile UScheduleDatabase INSTANCE;
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    static UScheduleDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UScheduleDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), UScheduleDatabase.class, "schedule_management_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract CourseDAO courseDAO();

    public abstract TermDAO termDAO();

    public abstract AssessDAO assessDAO();


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final CourseDAO mCourseDao;
        private final TermDAO mTermDao;
        private final AssessDAO mAssessDao;

        PopulateDbAsync(UScheduleDatabase db) {
            mTermDao = db.termDAO();
            mCourseDao = db.courseDAO();
            mAssessDao = db.assessDAO();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            String start = "12/01/2019";
            String end = "12/31/2019";

            TermEntity term = new TermEntity(1, "Term 1", start, end);
            mTermDao.insert(term);
            term = new TermEntity(2, "Term 2", start, end);
            mTermDao.insert(term);

            CourseEntity course = new CourseEntity(1, "C196", start, end, "In Progress", "Carolyn Sher-DeCusatis", "This course is almost completed, oh thank heavens!",1);
            mCourseDao.insert(course);
            course = new CourseEntity(2, "C482", start, end, "In Progress", "Mark Kinkead", "", 1);
            mCourseDao.insert(course);
            course = new CourseEntity(3, "C988", start, end, "Not Started", "Not Assigned", "", 2);
            mCourseDao.insert(course);

            AssessEntity assessment = new AssessEntity(1,"Mobile App", "Performance", "12/31/2019", 1);
            mAssessDao.insert(assessment);
            assessment = new AssessEntity(2, "Java Software Program", "Performance", "02/27/2020", 2);
            mAssessDao.insert(assessment);

            return null;
        }
    }

}
