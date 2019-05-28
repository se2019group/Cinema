package com.example.cinema.bl.promotion;

import com.example.cinema.po.RechargeForm;
import com.example.cinema.vo.VIPCardForm;
import com.example.cinema.vo.ResponseVO;



/**
 * Created by liying on 2019/4/14.
 */

public interface VIPService {

    ResponseVO addVIPCard(int userId);

    ResponseVO getCardById(int id);

    ResponseVO getVIPInfo();

    ResponseVO charge(VIPCardForm vipCardForm);

    ResponseVO getCardByUserId(int userId);
    ResponseVO recharge_record(int userId,int amount);
    ResponseVO get_recharge_record(int userId);


}
