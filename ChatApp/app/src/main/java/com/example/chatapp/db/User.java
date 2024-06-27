package com.example.chatapp.db;

// 创建 User.java 文件

public class User {
    private String nickname;
    private String phone;
    private String password;

    public User(String nickname, String phone, String password) {
        this.nickname = nickname;
        this.phone = phone;
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

