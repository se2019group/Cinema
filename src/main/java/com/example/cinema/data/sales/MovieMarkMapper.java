package com.example.cinema.data.sales;

import com.example.cinema.po.Price;
import com.example.cinema.po.Ticket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * Created by liying on 2019/4/16.
 */
@Mapper
public interface MovieMarkMapper {
    /**
     * 插入一条评价信息
     * @param userid
     * @param mark
     * @param movieid
     * @param text
     * @return
     */
    int insertEvaluation(@Param("userid") int userid,@Param("ticketid")int ticketid,@Param("mark")double mark,@Param("movieid")int movieid,@Param("text")String text);




}

