package com.example.cinema.blImpl.sales;

import com.example.cinema.bl.promotion.*;
import com.example.cinema.bl.sales.TicketService;
import com.example.cinema.blImpl.management.hall.HallServiceForBl;
import com.example.cinema.blImpl.management.schedule.ScheduleServiceForBl;
import com.example.cinema.blImpl.promotion.CouponServiceImpl;
import com.example.cinema.data.management.MovieMapper;
import com.example.cinema.data.promotion.ActivityMapper;
import com.example.cinema.data.promotion.CouponMapper;
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
    VIPService vipService;
    
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
    			
    			tickets.add(ticket);
    			i+=1;
    		}
    		
    		return ResponseVO.buildSuccess(ticketMapper.insertTickets(tickets));
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
            int i,j;
            TicketWithCouponVO ticketWithCouponVO=new TicketWithCouponVO();
            Timestamp now=new Timestamp(System.currentTimeMillis());
            Coupon coupon=couponMapper.selectById(couponId);
            List<Coupon> coupons=new ArrayList<Coupon>();
            List<TicketVO> ticketVOList=new ArrayList<TicketVO>();
            List<Activity> activities=activityMapper.selectActivities();
            ticketWithCouponVO.setActivities(activities);
            
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
            
            //检验优惠券是否可以使用
            if(coupon.getStartTime().before(now) && now.before(coupon.getEndTime())
            		&& sumFare>=coupon.getTargetAmount()) {
            	ticketWithCouponVO.setTotal(sumFare-coupon.getDiscountAmount());
            }
            else {
            	ticketWithCouponVO.setTotal(sumFare);
            }
            ticketWithCouponVO.setTicketVOList(ticketVOList);
            
            //根据优惠策略生成优惠券
            i=0;
            while(i<activities.size()) {
            	j=0;
            	while(j<id.size()) {
            		if(activities.get(i).getMovieList().contains(movieMapper.selectMovieById(id.get(j)))) {
            			break;
            		}
            		j+=1;
            	}
            	
            	if(j!=id.size()) {
            		coupons.add(activities.get(i).getCoupon());
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
            int i,j;
            TicketWithCouponVO ticketWithCouponVO=new TicketWithCouponVO();
            List<Coupon> coupons=new ArrayList<Coupon>();
            List<TicketVO> ticketVOList=new ArrayList<TicketVO>();
            List<Activity> activities=activityMapper.selectActivities();
            ticketWithCouponVO.setActivities(activities);
            VIPCard vipCard;
            
            //得到电影票的总价并将会员卡扣费
            i=0;
            sumFare=0;
            while(i<id.size()) {
            	ticket=ticketMapper.selectTicketById(id.get(i));
            	ticketVOList.add(ticket.getVO());
            	scheduleItem=scheduleService.getScheduleItemById(ticket.getScheduleId());
            	sumFare+=scheduleItem.getFare();
            	vipCard=(VIPCard)vipService.getCardByUserId(ticket.getUserId()).getContent();
            	if(vipCard.getBalance()<scheduleItem.getFare()) {
            		return ResponseVO.buildFailure("余额不足！");
            	}
            	vipCard.setBalance(vipCard.getBalance()-scheduleItem.getFare());
            	
            	i+=1;
            }
            ticketWithCouponVO.setTotal(sumFare);
            ticketWithCouponVO.setTicketVOList(ticketVOList);
            
            //根据优惠策略生成优惠券
            i=0;
            while(i<activities.size()) {
            	j=0;
            	while(j<id.size()) {
            		if(activities.get(i).getMovieList().contains(movieMapper.selectMovieById(id.get(j)))) {
            			break;
            		}
            		j+=1;
            	}
            	
            	if(j!=id.size()) {
            		coupons.add(activities.get(i).getCoupon());
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
    public ResponseVO cancelTicket(List<Integer> id) {
    	try {
    		int i;
            Ticket ticket;
            
            i=0;
            while(i<id.size()) {
            	if(ticketMapper.selectTicketById(id.get(i)).getState()==0) {
            		ticketMapper.updateTicketState(id.get(i), 1);;//将票的状态设置成“已完成”
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
