package com.lcw.meet;

public class Page4MessageItem {

    String nickname;
    String message;
    String time;
    String pofileiv;

    public Page4MessageItem(String nickname, String message, String time, String pofileiv) {
        this.nickname = nickname;
        this.message = message;
        this.time = time;
        this.pofileiv = pofileiv;
    }

    public Page4MessageItem() {
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPofileiv() {
        return pofileiv;
    }

    public void setPofileiv(String pofileiv) {
        this.pofileiv = pofileiv;
    }
}


