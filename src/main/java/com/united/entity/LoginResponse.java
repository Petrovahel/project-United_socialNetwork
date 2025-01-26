package com.united.entity;

public class LoginResponse {
    private String nickname;

    public LoginResponse(String nickname) {
        this.nickname = nickname;
    }

    // Getter
    public String getNickname() {
        return nickname;
    }

    // Setter
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}

