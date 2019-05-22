package com.example.cinema.blImpl.sales;

import com.example.cinema.bl.promotion.*;
import com.example.cinema.bl.sales.TicketService;
import com.example.cinema.blImpl.management.hall.HallServiceForBl;
import com.example.cinema.blImpl.management.schedule.ScheduleServiceForBl;
import com.example.cinema.blImpl.promotion.CouponServiceImpl;
import com.example.cinema.data.management.MovieMapper;
import com.example.cinema.data.promotion.ActivityMapper;
import com.example.cinema.data.promotion.CouponMapper;
import com.example.cinema.data.promotion.VIPCardMapper;
import com.example.cinema.data.sales.TicketMapper;
import com.example.cinema.po.Activity;
import com.example.cinema.po.Coupon;
import com.example.cinema.po.Hall;
import com.example.cinema.po.ScheduleItem;
import com.example.cinema.po.Ticket;
import com.example.cinema.po.VIPCard;
import com.example.cinema.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liying on 2019/4/16.
 */
@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    TicketMapper ticketMapper;
    @Autowired
    ScheduleServiceForBl scheduleService;
    @Autowired
    HallServiceForBl hallService;
    @Autowired
    CouponMapper couponMapper;
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    MovieMapper movieMapper;
    @Autowired
    VIPCardMapper vipCardMapper;
    
    @Override
    @Transactional
    public ResponseVO addTicket(TicketForm ticketForm) {
    	try {
    		List<Ticket> tickets=new ArrayList<Ticket>();
    		int userId=ticketForm.getUserId();
    		int scheduleId=ticketForm.getScheduleId();
    		List<SeatForm> seats=ticketForm.getSeats();
    		int i;
    		Ticket ticket;
    		
    		i=0;
    		while(i<seats.size()) {
    			ticket=new Ticket();
    			ticket.setUserId(userId);
    			ticket.setScheduleId(scheduleId);
    			ticket.setColumnIndex(seats.get(i).getColumnIndex());
    			ticket.setRowIndex(seats.get(i).getRowIndex());
    			ticket.setState(0);//状态为未完成
    			ticket.setTime(new Timestamp(System.currentTimeMillis()));
    			
    			ticketMapper.insertTicket(ticket);
    			ticket=ticketMapper.selectTicketByScheduleIdAndSeat(scheduleId, seats.get(i).getColumnIndex(),seats.get(i).getRowIndex());
    			
    			tickets.add(ticket);
    			i+=1;
    		}
    		return ResponseVO.buildSuccess(tickets);
    	}catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    @Transactional
    public ResponseVO completeTicket(List<Integer> id, int couponId) {
    	try {
            double sumFare;
            Ticket ticket;
            ScheduleItem scheduleItem;
            int i,j,k;
            TicketWithCouponVO ticketWithCouponVO=new TicketWithCouponVO();
            Timestamp now=new Timestamp(System.currentTimeMillis());
            Coupon coupon=null;
            List<Coupon> coupons=new ArrayList<Coupon>();
            List<TicketVO> ticketVOList=new ArrayList<TicketVO>();
            List<Activity> activities=activityMapper.selectActivities();
            ticketWithCouponVO.setActivities(activities);
            
            //检验是否有优惠券
            if(couponId>=0) {
            	coupon=couponMapper.selectById(couponId);
            }
            
            //将电影票的状态设置成 已完成
            i=0;
            while(i<id.size()) {
            	ticketMapper.updateTicketState(id.get(i), 1);
            	i+=1;
            }
            
            //得到电影票的总价
            i=0;
            sumFare=0;
            while(i<id.size()) {
            	ticket=ticketMapper.selectTicketById(id.get(i));
            	ticketVOList.add(ticket.getVO());
            	scheduleItem=scheduleService.getScheduleItemById(ticket.getScheduleId());
            	sumFare+=scheduleItem.getFare();
            	i+=1;
            }
            
            //检验优惠券是否可以使用，如果可以，在使用后删除该用户的这个优惠券
            if(coupon!=null) {
	            if(coupon.getStartTime().before(now) && now.before(coupon.getEndTime())
	            		&& sumFare>=coupon.getTargetAmount()) {
	            	ticketWithCouponVO.setTotal(sumFare-coupon.getDiscountAmount());
	            	couponMapper.deleteCouponUser(couponId, ticketMapper.selectTicketById(id.get(0)).getUserId());
	            }
	            else {
	            	ticketWithCouponVO.setTotal(sumFare);
	            }
            }
            else {
            	ticketWithCouponVO.setTotal(sumFare);
            }
            ticketWithCouponVO.setTicketVOList(ticketVOList);
            
            //根据优惠策略生成优惠券并将优惠券放入数据库中
            i=0;
            while(i<activities.size()) {
            	//判断时间是否满足
    			if(!(activities.get(i).getStartTime().before(now) && now.before(activities.get(i).getEndTime()))) {
            		i+=1;
            		continue;
            	}
    			
    			//如果优惠活动满足的电影数量为0，则任何电影都满足
    			if(activities.get(i).getMovieList().size()==0) {
    				coupons.add(activities.get(i).getCoupon());
            		couponMapper.insertCouponUser(activities.get(i).getCoupon().getId(), ticketMapper.selectTicketById(id.get(0)).getUserId());
            		i+=1;
            		continue;
    			}
    			
    			j=0;
            	Lable:
            	while(j<id.size()) {
            		k=0;
            		while(k<activities.get(i).getMovieList().size()) {
            			if(scheduleService.getScheduleItemById(ticketMapper.selectTicketById(id.get(j)).getScheduleId()).getMovieId()
            					==activities.get(i).getMovieList().get(k).getId()) {
            				break Lable;
            			}
            			k+=1;
            		}
            		j+=1;
            	}
            	if(j!=id.size()) {
            		coupons.add(activities.get(i).getCoupon());
            		couponMapper.insertCouponUser(activities.get(i).getCoupon().getId(), ticketMapper.selectTicketById(id.get(0)).getUserId());
            	}
            	i+=1;
            }
            ticketWithCouponVO.setCoupons(coupons);
            
            return ResponseVO.buildSuccess(ticketWithCouponVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getBySchedule(int scheduleId) {
        try {
            List<Ticket> tickets = ticketMapper.selectTicketsBySchedule(scheduleId);
            ScheduleItem schedule=scheduleService.getScheduleItemById(scheduleId);
            Hall hall=hallService.getHallById(schedule.getHallId());
            int[][] seats=new int[hall.getRow()][hall.getColumn()];
            tickets.stream().forEach(ticket -> {
                seats[ticket.getRowIndex()][ticket.getColumnIndex()]=1;
            });
            ScheduleWithSeatVO scheduleWithSeatVO=new ScheduleWithSeatVO();
            scheduleWithSeatVO.setScheduleItem(schedule);
            scheduleWithSeatVO.setSeats(seats);
            return ResponseVO.buildSuccess(scheduleWithSeatVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getTicketByUser(int userId) {
    	 try {
             return ResponseVO.buildSuccess(ticketMapper.selectTicketByUser(userId));
         } catch (Exception e) {
             e.printStackTrace();
             return ResponseVO.buildFailure("失败");
         }
    }

    @Override
    @Transactional
    public ResponseVO completeByVIPCard(List<Integer> id, int couponId) {
    	try {
            double sumFare;
            Ticket ticket;
            ScheduleItem scheduleItem;
            int i,j,k;
            TicketWithCouponVO ticketWithCouponVO=new TicketWithCouponVO();
            List<Coupon> coupons=new ArrayList<Coupon>();
            List<TicketVO> ticketVOList=new ArrayList<TicketVO>();
            List<Activity> activities=activityMapper.selectActivities();
            ticketWithCouponVO.setActivities(activities);
            VIPCard vipCard=vipCardMapper.selectCardByUserId(ticketMapper.selectTicketById(id.get(0)).getUserId());
            Coupon coupon=null;
            Timestamp now=new Timestamp(System.currentTimeMillis());
            boolean useCoupon=false;
            
            if(vipCard==null) {
        		return ResponseVO.buildFailure("会员卡不存在！");
        	}
            
            //检验是否有优惠券
            if(couponId>=0) {
            	coupon=couponMapper.selectById(couponId);
            }
            
            //得到电影票的总价
            i=0;
            sumFare=0;
            while(i<id.size()) {
            	ticket=ticketMapper.selectTicketById(id.get(i));
            	ticketVOList.add(ticket.getVO());
            	scheduleItem=scheduleService.getScheduleItemById(ticket.getScheduleId());
            	sumFare+=scheduleItem.getFare();
            	
            	i+=1;
            }
            
            //检验优惠券
            if(coupon!=null) {
	            if(coupon.getStartTime().before(now) && now.before(coupon.getEndTime()) && sumFare>=coupon.getTargetAmount()) {
	            	sumFare-=coupon.getDiscountAmount();
	            	useCoupon=true;
	            }
            }
            ticketWithCouponVO.setTotal(sumFare);
            
            //会员卡扣款
            if(vipCard.getBalance()<sumFare) {
            	return ResponseVO.buildFailure("余额不足！");
            }
            else {
            	vipCardMapper.updateCardBalance(vipCard.getId(), vipCard.getBalance()-sumFare);
            }
            
            //根据优惠策略生成优惠券并将优惠券放入数据库中
            i=0;
            while(i<activities.size()) {
            	//判断时间是否满足
    			if(!(activities.get(i).getStartTime().before(now) && now.before(activities.get(i).getEndTime()))) {
            		i+=1;
            		continue;
            	}
    			
    			//如果优惠活动满足的电影数量为0，则任何电影都满足
    			if(activities.get(i).getMovieList().size()==0) {
    				coupons.add(activities.get(i).getCoupon());
            		couponMapper.insertCouponUser(activities.get(i).getCoupon().getId(), ticketMapper.selectTicketById(id.get(0)).getUserId());
            		i+=1;
            		continue;
    			}
    			
    			j=0;
            	Lable:
            	while(j<id.size()) {
            		k=0;
            		while(k<activities.get(i).getMovieList().size()) {
            			if(scheduleService.getScheduleItemById(ticketMapper.selectTicketById(id.get(j)).getScheduleId()).getMovieId()
            					==activities.get(i).getMovieList().get(k).getId()) {
            				break Lable;
            			}
            			k+=1;
            		}
            		j+=1;
            	}
            	if(j!=id.size()) {
            		coupons.add(activities.get(i).getCoupon());
            		couponMapper.insertCouponUser(activities.get(i).getCoupon().getId(), ticketMapper.selectTicketById(id.get(0)).getUserId());
            	}
            	i+=1;
            }
            ticketWithCouponVO.setCoupons(coupons);
            
            //检验是否使用了优惠券
            if(useCoupon) {
            	couponMapper.deleteCouponUser(couponId, ticketMapper.selectTicketById(id.get(0)).getUserId());
            }
            
            //将电影票的状态设置成 已完成
            i=0;
            while(i<id.size()) {
            	ticketMapper.updateTicketState(id.get(i), 1);
            	i+=1;
            }
            i=0;
            while(i<ticketVOList.size()) {
            	ticketVOList.get(i).setState("已完成");
            	i+=1;
            }
            ticketWithCouponVO.setTicketVOList(ticketVOList);
            
            return ResponseVO.buildSuccess(ticketWithCouponVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO cancelTicket(List<Integer> id) {
    	try {
    		int i;
            Ticket ticket;
            
            i=0;
            while(i<id.size()) {
            	ticket=ticketMapper.selectTicketById(id.get(i));
            	if(ticket!=null && (ticket.getState()==0 || ticket.getState()==1)) {
            		ticketMapper.updateTicketState(id.get(i), 2);//将票的状态设置成“已失效”
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
