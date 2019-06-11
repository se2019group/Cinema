package com.example.cinema.po;

public class MarkRecord {

    private int userId;

    private double mark;

    private int movieId;

    private int ticketId;

    private String comment;

    public int getUserId(){ return userId;}

    public void setUserId(int userId){ this.userId=userId;}

    public int getMovieId(){ return movieId;}

    public void setMovieId(int movieId){ this.movieId=movieId;}

    public int getTicketId(){ return ticketId;}

    public void setTicketId(int ticketId){ this.ticketId=ticketId;}

    public double getMark(){ return mark;}

    public void setMark(double mark){ this.mark=mark;}

    public String getComment(){ return comment;}

    public void setComment(String comment){ this.comment=comment;}
}
