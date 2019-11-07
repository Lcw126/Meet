package com.lcw.meet;

import java.util.ArrayList;

public class UsePublicData {

    static  String  currentkakaoIDNUM;
    public static ArrayList<String> kakaoIDes;

    public UsePublicData(String currentkakaoIDNUM,ArrayList<String> kakaoIDes) {
        this.currentkakaoIDNUM = currentkakaoIDNUM;
        this.kakaoIDes = kakaoIDes;
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
