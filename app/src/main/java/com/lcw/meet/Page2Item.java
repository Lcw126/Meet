package com.lcw.meet;

public class Page2Item {
    String kakaoID;
    String nickname;
    String year;
    String memo;
    String img;
    String imgPath;

    public Page2Item() {
    }

    public Page2Item(String kakaoID, String nickname,String year, String memo, String img, String imgPath) {
        this.kakaoID = kakaoID;
        this.nickname=nickname;
        this.year=year;
        this.memo = memo;
        this.img=img;
        this.imgPath = imgPath;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getKakaoID() {
        return kakaoID;
    }

    public void setKakaoID(String kakaoID) {
        this.kakaoID = kakaoID;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
