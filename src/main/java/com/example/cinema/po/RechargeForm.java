package com.example.cinema.po;

import java.util.Date;

public class RechargeForm {
    private int user_id;
    private String cardNumber;
    private int amount;
    private Date time;
    public Integer getuser_id() {
        return user_id;
    }

    public void setuser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getcardNumber() {
        return cardNumber;
    }

    public void setcardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getamount() {
        return amount;
    }

    public void setamount(int amount) {
        this.amount = amount;
    }

    public Date gettime() {
        return time;
    }

    public void settime(Date time) {
        this.time = time;
    }

}
