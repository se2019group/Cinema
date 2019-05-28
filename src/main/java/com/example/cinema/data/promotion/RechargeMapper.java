package com.example.cinema.data.promotion;

import com.example.cinema.po.Activity;
import com.example.cinema.po.RechargeForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liying on 2019/4/20.
 */
@Mapper
public interface RechargeMapper {

    List<RechargeForm> getrecord(int userid);
}