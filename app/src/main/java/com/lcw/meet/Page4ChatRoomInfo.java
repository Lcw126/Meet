package com.lcw.meet;

public class Page4ChatRoomInfo {

    String currentNickname;
    String otherNicnkname;
    String roomName;

    public Page4ChatRoomInfo(String currentNickname, String otherNicnkname, String roomName) {
        this.currentNickname = currentNickname;
        this.otherNicnkname = otherNicnkname;
        this.roomName = roomName;
    }

    public Page4ChatRoomInfo() {
    }

    public String getCurrentNickname() {
        return currentNickname;
    }

    public void setCurrentNickname(String currentNickname) {
        this.currentNickname = currentNickname;
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
