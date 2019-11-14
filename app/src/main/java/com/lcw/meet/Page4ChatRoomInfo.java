package com.lcw.meet;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class Page4ChatRoomInfo {


    String otherNicnkname;
    String roomName;

    static ArrayList<Page4ChatRoomInfo> saveRoomInfoChilds= new ArrayList<>();
    public Page4ChatRoomInfo(String otherNicnkname, String roomName) {

        this.otherNicnkname = otherNicnkname;
        this.roomName = roomName;
    }

    public Page4ChatRoomInfo() {
    }


    public String getOtherNicnkname() {
        return otherNicnkname;
    }

    public void setOtherNicnkname(String otherNicnkname) {
        this.otherNicnkname = otherNicnkname;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
