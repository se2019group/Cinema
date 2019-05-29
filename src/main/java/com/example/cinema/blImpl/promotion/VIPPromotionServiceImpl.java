package com.example.cinema.blImpl.promotion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cinema.bl.promotion.VIPPromotionService;
import com.example.cinema.data.promotion.VIPPromotionMapper;
import com.example.cinema.vo.ResponseVO;

@Service
public class VIPPromotionServiceImpl implements VIPPromotionService{
	
	@Autowired
	VIPPromotionMapper vipPromotionMapper;
	
	@Override
	public ResponseVO getVIPPromotion() {
		try {
			return ResponseVO.buildSuccess(vipPromotionMapper.getVIPPromotion());
		}catch (Exception e) {
			return ResponseVO.buildFailure("失败");
		}
	}
	
	@Override
	public ResponseVO setVIPPromotion(int target,int discount) {
		try {
			vipPromotionMapper.updateVIPPromotion(target, discount,1);
			return ResponseVO.buildSuccess();
		}catch (Exception e) {
			return ResponseVO.buildFailure("失败");
		}
	}

}
