package com.example.cinema.blImpl.promotion;

import com.example.cinema.bl.promotion.CouponService;
import com.example.cinema.data.promotion.CouponMapper;
import com.example.cinema.po.Coupon;
import com.example.cinema.vo.CouponForm;
import com.example.cinema.vo.ResponseVO;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liying on 2019/4/17.
 */
@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    CouponMapper couponMapper;

    @Override
    public ResponseVO getCouponsByUser(int userId) {
        try {
            return ResponseVO.buildSuccess(couponMapper.selectCouponByUser(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO addCoupon(CouponForm couponForm) {
        try {
            Coupon coupon=new Coupon();
            coupon.setName(couponForm.getName());
            coupon.setDescription(couponForm.getDescription());
            coupon.setTargetAmount(couponForm.getTargetAmount());
            coupon.setDiscountAmount(couponForm.getDiscountAmount());
            coupon.setStartTime(couponForm.getStartTime());
            coupon.setEndTime(couponForm.getEndTime());
            couponMapper.insertCoupon(coupon);
            return ResponseVO.buildSuccess(coupon);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO issueCoupon(int couponId, int userId) {
        try {
            couponMapper.insertCouponUser(couponId,userId);
            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }

    }
    
    @Override
	public ResponseVO getAllCoupons() {
		try {
			List<Coupon> coupons=couponMapper.selectAllCoupon();
			Coupon coupon;
			Timestamp now=new Timestamp(System.currentTimeMillis());
			int i;
			
			i=0;
			while(i<coupons.size()) {
				coupon=coupons.get(i);
				if(!(coupon.getStartTime().before(now) && now.before(coupon.getEndTime()))) {
					coupons.remove(coupons.get(i));
					i-=1;
				}
				i+=1;
			}
            return ResponseVO.buildSuccess(coupons);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
	}

	@Override
	public ResponseVO issueCoupons(List<Integer> userIdList, List<Integer> couponIdList) {
		try {
            int i,j;
            
            i=0;
            while(i<userIdList.size()) {
            	j=0;
            	while(j<couponIdList.size()) {
            		couponMapper.insertCouponUser(couponIdList.get(j), userIdList.get(i));
            		j+=1;
            	}
            	i+=1;
            }
            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
	}
}
