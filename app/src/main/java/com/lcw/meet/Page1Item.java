package com.lcw.meet;

public class Page1Item {
    String nickname;
    String year;
    String local;
    String ImgPath;

    public Page1Item() {
    }

    public Page1Item(String nickname, String year, String local, String ImgPath) {
        this.nickname = nickname;
        this.year = year;
        this.local = local;
        this.ImgPath= ImgPath;
    }

    public String getImgPath() {
        return ImgPath;
    }

    public void setImgPath(String imgPath) {
        ImgPath = imgPath;
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
