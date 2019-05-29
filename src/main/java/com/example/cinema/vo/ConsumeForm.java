package com.example.cinema.vo;
public class ConsumeForm {
    private int userid;
    private int type;
    private float amount;
    private int scheduleId;
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

    public int getscheduleId() {
        return scheduleId;
    }
    public void setscheduleId(int scheduleId) {
        this.scheduleId =scheduleId ;
    }


}
