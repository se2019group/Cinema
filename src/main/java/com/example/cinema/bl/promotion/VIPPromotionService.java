package com.example.cinema.bl.promotion;

import com.example.cinema.vo.ResponseVO;

public interface VIPPromotionService {
	
	ResponseVO getVIPPromotion();
	
	ResponseVO setVIPPromotion(int target,int discount);
}
