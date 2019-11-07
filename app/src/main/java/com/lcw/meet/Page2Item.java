package com.lcw.meet;

public class Page2Item {
    String kakaoID;
    String nickname;
    String memo;
    String imgPath;

    public Page2Item() {
    }

    public Page2Item(String kakaoID, String nickname, String memo, String imgPath) {
        this.kakaoID = kakaoID;
        this.memo = memo;
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
