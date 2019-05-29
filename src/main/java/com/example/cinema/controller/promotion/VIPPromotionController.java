package com.example.cinema.controller.promotion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cinema.bl.promotion.VIPPromotionService;
import com.example.cinema.vo.ResponseVO;

@RestController()
@RequestMapping("/vipPromotion")
public class VIPPromotionController {
	@Autowired
	VIPPromotionService vipPromotionService;
	
	@GetMapping("/get")
	public ResponseVO getVIPPromotion() {
		return vipPromotionService.getVIPPromotion();
	}
	
	@PostMapping("/set")
	public ResponseVO setVIPPromotion(@RequestParam int target,@RequestParam int discount) {
		return vipPromotionService.setVIPPromotion(target, discount);
	}
}
