package com.lcw.meet;

public class Page3item {

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

    public Page3item(String nickname, String year, String local, String ImgPath01) {
        this.nickname = nickname;
        this.year = year;
        this.local = local;
        this.ImgPath01 = ImgPath01;
    }

    public Page3item(String kakakoID, String nickname, String gender, String year, String local, String intro, String cahrac, String imgPath01, String imgPath02, String imgPath03, String tome, String fromme) {
        this.kakakoID = kakakoID;
        this.nickname = nickname;
        this.gender = gender;
        this.year = year;
        this.local = local;
        this.intro = intro;
        this.cahrac = cahrac;
        ImgPath01 = imgPath01;
        ImgPath02 = imgPath02;
        ImgPath03 = imgPath03;
        this.tome = tome;
        this.fromme = fromme;
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

    public String getIv() {
        return ImgPath01;
    }

    public void setIv(String ImgPath01) {
        this.ImgPath01 = ImgPath01;
    }
}
