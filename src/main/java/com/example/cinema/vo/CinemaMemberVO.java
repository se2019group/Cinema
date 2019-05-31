package com.example.cinema.vo;

import com.example.cinema.po.CinemaMember;

public class CinemaMemberVO {
    private Integer type;
    private Integer id;
    private String username;
    private String password;

    public CinemaMemberVO(CinemaMember cinemaMember){
        this.type=cinemaMember.getType();
        this.id = cinemaMember.getId();
        this.username = cinemaMember.getUsername();
        this.password = cinemaMember.getPassword();
    }

    public Integer getType(){return type;}

    public void setType(Integer type){ this.type=type;}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
