package com.example.cinema.data.sales;

import com.example.cinema.po.TicketPromotion;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface  TicketPromotionMapper {
    List<TicketPromotion> getPromotion(int id);
    void changePromotion(int FullTime,int PartTime,float Discounts,int OutTime);
}
