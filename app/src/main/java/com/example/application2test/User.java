package com.example.application2test;
//用户数据类
import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private int avatarResId;

    public User(String username, String password, int avatarResId) {
        this.username = username;
        this.password = password;
        this.avatarResId = avatarResId;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getAvatarResId() { return avatarResId; }
    public void setAvatarResId(int avatarResId) { this.avatarResId = avatarResId; }
}
