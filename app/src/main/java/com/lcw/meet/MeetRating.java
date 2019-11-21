package com.lcw.meet;

public class MeetRating {
    String db_setuser;
    String db_getuser;
    float db_grade;

    public MeetRating(String db_setuser, String db_getuser, float db_grade) {
        this.db_setuser = db_setuser;
        this.db_getuser = db_getuser;
        this.db_grade = db_grade;
    }
}
