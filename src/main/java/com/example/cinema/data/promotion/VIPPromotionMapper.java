package com.example.cinema.data.promotion;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.cinema.po.VIPPromotion;

@Mapper
public interface VIPPromotionMapper {
	/**
	 * 得到会员卡优惠策略
	 * @return
	 */
	VIPPromotion getVIPPromotion();
	
	/**
	 * 更新会员卡优惠策略
	 * @param target
	 * @param discount
	 */
	void updateVIPPromotion(@Param("target")int target,@Param("discount")int discount,@Param("id")int id);
}
