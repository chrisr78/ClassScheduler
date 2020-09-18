package com.example.uschedule.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Courses")
public class CourseEntity {
    @PrimaryKey
    private int courseID;
    @ColumnInfo(name = "courseName")
    private String courseName;
    private String startDate;
    private String endDate;
    private String status;
    private String mentor;
    private String note;
    private int termID;

    public CourseEntity(int courseID, String courseName, String startDate, String endDate, String status, String mentor, String note, int termID) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.mentor = mentor;
        this.note = note;
        this.termID = termID;
    }

    @Override
    public String toString() {
        return "CourseEntity{" +
                "courseID=" + courseID +
                ", courseName='" + courseName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                ", mentor='" + mentor + '\'' +
                ", note='" + note + '\'' +
                ", termID=" + termID +
                '}';
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getTermID() {
        return termID;
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMentor() {
        return mentor;
    }

    public void setMentor(String mentor) {
        this.mentor = mentor;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
