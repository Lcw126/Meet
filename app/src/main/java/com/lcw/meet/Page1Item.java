package com.lcw.meet;

public class Page1Item {
    String kakakoID;
    String nickname;
    String gender;
    String year;
    String local;
    String intro;
    String cahrac;
    String ImgPath01;
    String ImgPath02;
    String ImgPath03;
    String tome;
    String fromme;


    /////////////////////test용 트래픽을 사용하지 않기 위해
    int testimg;

    public int getTestimg() {
        return testimg;
    }

    public void setTestimg(int testimg) {
        this.testimg = testimg;
    }
    /////////////////////////////////////////////////////


    public Page1Item() {
    }

    public Page1Item(String kakakoID, String nickname, String gender, String year, String local, String intro, String cahrac, String imgPath01, String imgPath02, String imgPath03,String tome,String fromme) {
        this.kakakoID= kakakoID;
        this.nickname = nickname;
        this.gender = gender;
        this.year = year;
        this.local = local;
        this.intro = intro;
        this.cahrac = cahrac;
        this.ImgPath01 = imgPath01;
        this.ImgPath02 = imgPath02;
        this. ImgPath03 = imgPath03;
        this.tome = tome;
        this.fromme = fromme;
    }

    /////////////////////test용 트래픽을 사용하지 않기 위해
    public Page1Item(String nickname, String year, String local, int testimg) {
        this.nickname = nickname;
        this.year = year;
        this.local = local;
        this.testimg= testimg;
    }
    /////////////////////////////////////////////////////

    public String getKakakoID() {
        return kakakoID;
    }

    public void setKakakoID(String kakakoID) {
        this.kakakoID = kakakoID;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getCahrac() {
        return cahrac;
    }

    public void setCahrac(String cahrac) {
        this.cahrac = cahrac;
    }

    public String getImgPath02() {
        return ImgPath02;
    }

    public void setImgPath02(String imgPath02) {
        ImgPath02 = imgPath02;
    }

    public String getImgPath03() {
        return ImgPath03;
    }

    public void setImgPath03(String imgPath03) {
        ImgPath03 = imgPath03;
    }

    public String getImgPath01() {
        return ImgPath01;
    }

    public void setImgPath01(String imgPath) {
        ImgPath01 = imgPath;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
