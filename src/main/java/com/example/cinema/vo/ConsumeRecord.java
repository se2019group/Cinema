package com.example.cinema.vo;

import java.util.Date;

public class ConsumeRecord {
    private int userid;
    private int type;
    private String cardNumber;
    private float amount;
    private String content;
    private Date time;
    public int getuserid() {
        return userid;
    }
    public void setuserid(int userid) {
        this.userid = userid;
    }

    public int gettype() {
        return type;
    }
    public void setypet(int type) {
        this.type =type ;
    }

    public float getamount() { return amount; }
    public void setamount(float amount) {
        this.amount = amount;
    }

    public String getcardNumber() {
        return cardNumber;
    }
    public void setcardNumber(String cardNumber) {
        this.cardNumber =cardNumber ;
    }

    public String getcontent(){return  content;}
    public void setcontent(String content){this.content=content;}

    public Date gettime() {
        return time;
    }

    public void settime(Date time) {
        this.time = time;
    }
}
