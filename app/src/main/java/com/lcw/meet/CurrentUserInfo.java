package com.lcw.meet;

public class CurrentUserInfo {


    static String db_kakaoID;
    static String db_nickname;
    static String db_gender;
    static String db_year;
    static String db_local;
    static String db_intro;
    static String db_charac;
    static String db_imgPath01;  //이미지는 상대경로라서 앞에 서버 주소를 써야한다.
    static String db_imgPath02;
    static String db_imgPath03;
    static String tome;
    static String fromme;

    public CurrentUserInfo() {
    }

    public CurrentUserInfo(String db_kakaoID, String db_nickname, String db_gender, String db_year, String db_local, String db_intro, String db_charac, String db_imgPath01, String db_imgPath02, String db_imgPath03) {
        this.db_kakaoID = db_kakaoID;
        this.db_nickname = db_nickname;
        this.db_gender = db_gender;
        this.db_year = db_year;
        this.db_local = db_local;
        this.db_intro = db_intro;
        this.db_charac = db_charac;
        this.db_imgPath01 = db_imgPath01;
        this.db_imgPath02 = db_imgPath02;
        this.db_imgPath03 = db_imgPath03;
    }
    public CurrentUserInfo(String db_kakaoID, String db_nickname, String db_gender, String db_year, String db_local, String db_intro, String db_charac, String db_imgPath01, String db_imgPath02, String db_imgPath03, String tome,String fromme) {
        this.db_kakaoID = db_kakaoID;
        this.db_nickname = db_nickname;
        this.db_gender = db_gender;
        this.db_year = db_year;
        this.db_local = db_local;
        this.db_intro = db_intro;
        this.db_charac = db_charac;
        this.db_imgPath01 = db_imgPath01;
        this.db_imgPath02 = db_imgPath02;
        this.db_imgPath03 = db_imgPath03;
        this.tome = tome;
        this.fromme = fromme;
    }


    public CurrentUserInfo(String db_imgPath01) {
        this.db_imgPath01 = db_imgPath01;
    }


    public String getDb_kakaoID() {
        return db_kakaoID;
    }

    public void setDb_kakaoID(String db_kakaoID) {
        this.db_kakaoID = db_kakaoID;
    }

    public String getDb_nickname() {
        return db_nickname;
    }

    public void setDb_nickname(String db_nickname) {
        this.db_nickname = db_nickname;
    }

    public String getDb_gender() {
        return db_gender;
    }

    public void setDb_gender(String db_gender) {
        this.db_gender = db_gender;
    }

    public String getDb_year() {
        return db_year;
    }

    public void setDb_year(String db_year) {
        this.db_year = db_year;
    }

    public String getDb_local() {
        return db_local;
    }

    public void setDb_local(String db_local) {
        this.db_local = db_local;
    }

    public String getDb_intro() {
        return db_intro;
    }

    public void setDb_intro(String db_intro) {
        this.db_intro = db_intro;
    }

    public String getDb_charac() {
        return db_charac;
    }

    public void setDb_charac(String db_charac) {
        this.db_charac = db_charac;
    }

    public String getDb_imgPath02() {
        return db_imgPath02;
    }

    public void setDb_imgPath02(String db_imgPath02) {
        this.db_imgPath02 = db_imgPath02;
    }

    public String getDb_imgPath03() {
        return db_imgPath03;
    }

    public void setDb_imgPath03(String db_imgPath03) {
        this.db_imgPath03 = db_imgPath03;
    }

    public String getDb_imgPath01() {
        return db_imgPath01;
    }

    public void setDb_imgPath01(String db_imgPath01) {
        this.db_imgPath01 = db_imgPath01;
    }
}
