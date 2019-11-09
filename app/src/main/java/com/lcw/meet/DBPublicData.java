package com.lcw.meet;

import java.util.ArrayList;

public class DBPublicData {

    static  String  currentkakaoIDNUM;
    public static ArrayList<String> kakaoIDes= new ArrayList<>();
    static  String currentNickname;
    public static ArrayList<Page1Item> DBdatas= new ArrayList<>();

    public DBPublicData(String currentkakaoIDNUM, ArrayList<String> kakaoIDes) {
        this.currentkakaoIDNUM = currentkakaoIDNUM;
        this.kakaoIDes = kakaoIDes;
    }

    public static String getCurrentNickname() {
        return currentNickname;
    }

    public static void setCurrentNickname(String currentNickname) {
        DBPublicData.currentNickname = currentNickname;
    }

    public ArrayList<String> getKakaoIDes() {
        return kakaoIDes;
    }

    public void setKakaoIDes(ArrayList<String> kakaoIDes) {
        this.kakaoIDes = kakaoIDes;
    }

    public String getCurrentkakaoIDNUM() {
        return currentkakaoIDNUM;
    }

    public void setCurrentkakaoIDNUM(String currentkakaoIDNUM) {
        this.currentkakaoIDNUM = currentkakaoIDNUM;
    }
}
