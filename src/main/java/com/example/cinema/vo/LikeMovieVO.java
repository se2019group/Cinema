package com.example.cinema.vo;

public class LikeMovieVO{
    /**
     * 电影的喜爱人数
     */
    private int likeNum;

    /**
     * 电影的名字
     */
    private String name;

    public void setLikeNum(int likeNum){
        this.likeNum=likeNum;
    }

    public int getLikeNum(){
        return this.likeNum;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getName(){
        return this.name;
    }
}