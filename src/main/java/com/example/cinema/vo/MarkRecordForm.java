package com.example.cinema.vo;

public class MarkRecordForm {

    private double mark;

    private int ticketId;

    private String comment;

    public int getTicketId(){ return ticketId;}

    public void setTicketId(int ticketId){ this.ticketId=ticketId;}

    public double getMark(){ return mark;}

    public void setMark(double mark){ this.mark=mark;}

    public String getComment(){ return comment;}

    public void setComment(String comment){ this.comment=comment;}
}
