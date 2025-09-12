package com.example.application2test;
//好友数据类
public class Friend {
    private String name;
    private String status;
    private int avatarResId;

    public Friend(String name, String status, int avatarResId) {
        this.name = name;
        this.status = status;
        this.avatarResId = avatarResId;
    }

    public String getName() { return name; }
    public String getStatus() { return status; }
    public int getAvatarResId() { return avatarResId; }
}
