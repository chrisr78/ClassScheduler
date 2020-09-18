package com.example.uschedule.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Assessments")
public class AssessEntity {
    @PrimaryKey
    int assessID;
    String assessName;
    String type;
    String dueDate;
    int courseID;


    public AssessEntity(int assessID, String assessName, String type, String dueDate, int courseID) {
        this.assessID = assessID;
        this.type = type;
        this.assessName = assessName;
        this.dueDate = dueDate;
        this.courseID = courseID;
    }

    public int getAssessID() {
        return assessID;
    }

    public void setAssessID(int assessID) {
        this.assessID = assessID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAssessName() {
        return assessName;
    }

    public void setAssessName(String assessName) {
        this.assessName = assessName;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }
}
