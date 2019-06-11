package com.example.cinema.po;

public class MarkRecord {

    private int userid;

    private double mark;

    private int movieid;

    private int ticketid;

    private String text;

    public int getUserId(){ return userid;}

    public void setUserId(int userId){ this.userid=userId;}

    public int getMovieId(){ return movieid;}

    public void setMovieId(int movieId){ this.movieid=movieId;}

    public int getTicketId(){ return ticketid;}

    public void setTicketId(int ticketId){ this.ticketid=ticketId;}

    public double getMark(){ return mark;}

    public void setMark(double mark){ this.mark=mark;}

    public String getComment(){ return text;}

    public void setComment(String text){ this.text=text;}
}
