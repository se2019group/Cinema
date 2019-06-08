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
			if(target<=0) {
				return ResponseVO.buildFailure("门槛金额不能小于等于0！");
			}
			else if(discount<=0) {
				return ResponseVO.buildFailure("赠送金额不能小于等于0！");
			}
			vipPromotionMapper.updateVIPPromotion(target, discount,1);
			return ResponseVO.buildSuccess();
		}catch (Exception e) {
			return ResponseVO.buildFailure("你遇到了奇怪的错误……");
		}
	}

}
