package com.example.uschedule.Entities;

public class MentorInfo {

    private String mentorName;
    private String mentorNumber;
    private String mentorEmail;

    public MentorInfo(String mentorName, String mentorNumber, String mentorEmail) {
        this.mentorName = mentorName;
        this.mentorNumber = mentorNumber;
        this.mentorEmail = mentorEmail;
    }

    public String getMentorName() {
        return mentorName;
    }

    public String getMentorNumber() {
        return mentorNumber;
    }

    public String getMentorEmail() {
        return mentorEmail;
    }
}
