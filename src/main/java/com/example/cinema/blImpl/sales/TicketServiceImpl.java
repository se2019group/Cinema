package com.example.cinema.blImpl.sales;

import java.text.DecimalFormat;
import com.example.cinema.bl.promotion.*;
import com.example.cinema.bl.sales.TicketService;
import com.example.cinema.blImpl.management.hall.HallServiceForBl;
import com.example.cinema.blImpl.management.schedule.ScheduleServiceForBl;
import com.example.cinema.blImpl.promotion.CouponServiceImpl;
import com.example.cinema.data.management.MovieMapper;
import com.example.cinema.data.management.ScheduleMapper;
import com.example.cinema.data.promotion.ActivityMapper;
import com.example.cinema.data.promotion.CouponMapper;
import com.example.cinema.data.promotion.VIPCardMapper;
import com.example.cinema.data.sales.MovieMarkMapper;
import com.example.cinema.data.sales.TicketMapper;
import com.example.cinema.data.sales.TicketPromotionMapper;
import com.example.cinema.data.sales.ConsumeMapper;
import com.example.cinema.po.*;
import com.example.cinema.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liying on 2019/4/16.
 */
@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    TicketMapper ticketMapper;
    @Autowired
    ConsumeMapper consumeMapper;
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
    @Autowired
    TicketPromotionMapper  ticketPromotionMapper;
    @Autowired
    ScheduleMapper scheduleMapper;
    @Autowired
    MovieMarkMapper movieMarkMapper;
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

    @Override
    public ResponseVO deleteTicket(int ticketId) {
        try{
            Ticket ticket=ticketMapper.selectTicketById(ticketId);
            ScheduleItem scheduleItem=scheduleService.getScheduleItemById(ticket.getScheduleId());
            Date endtime=scheduleItem.getEndTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date now=simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            endtime=simpleDateFormat.parse(simpleDateFormat.format(endtime));
            if(ticket.getState()==2){
                ticketMapper.deleteTicket(ticketId);
                return ResponseVO.buildSuccess();
            }else {
                if (endtime.before(now)) {
                    ticketMapper.deleteTicket(ticketId);
                    return ResponseVO.buildSuccess();
                } else {
                    return ResponseVO.buildFailure("失败");
                }
            }
        }catch(Exception e){
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO Consume_Record(ConsumeForm consumeform){
        String cardNumber="123123123";
        ConsumeRecord onerecord=new ConsumeRecord();
       onerecord.setamount(consumeform.getamount());
       onerecord.setuserid(consumeform.getuserid());
       onerecord.setypet(consumeform.gettype());
       if(consumeform.gettype()==1){
           cardNumber=(vipCardMapper.selectCardByUserId(consumeform.getuserid())).getId()+"";
       }
          String content=(scheduleService.getScheduleItemById(consumeform.getscheduleId())).getMovieName();
       onerecord.setcontent(content);
       onerecord.setcardNumber(cardNumber);
       consumeMapper.insertConsume(onerecord);
        return ResponseVO.buildSuccess();

 }


    @Override
    public ResponseVO abolishTicket(int ticketId){
        try{
            Ticket ticket=ticketMapper.selectTicketById(ticketId);
            ticketMapper.updateTicketState(ticketId, 2);//将票的状态设置成“已失效”
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            return ResponseVO.buildFailure("失败");
        }

    }



    @Override
   public ResponseVO  getConsume_Record(int userId){
        List<ConsumeRecord> results=new  ArrayList<ConsumeRecord>();
        results=consumeMapper.searchConsumeByuserId(userId);
        return ResponseVO.buildSuccess(results);
    }
    @Override
    public ResponseVO  TicketPromotion(){
        return ResponseVO.buildSuccess(ticketPromotionMapper.getPromotion(1));
    }
    @Override
    public ResponseVO changeTicketPromotion(TicketPromotion ticketPromotion){
        ticketPromotionMapper.changePromotion(ticketPromotion.getFullTime(),ticketPromotion.getPartTime(),ticketPromotion.getDiscounts(),ticketPromotion.getOutTime());
        return ResponseVO.buildSuccess();
    }
    //策略说明，在全额时间前的全部退，在全额时间与部分退款时间中，折扣退，在部分退款和不能退款中折扣上继续折扣退，在不能退款时间后不能退。
    //默认退款给VIP卡，没有VIP卡的退到银行卡，因为默认是123123123，所以相当于不退款。
    //第二次的折扣是在第一次的价格上减去百分之10，此百分之10为整数，向下取整。
    @Override
    public ResponseVO TicketReturn(int ticketId){
        boolean canreturn=true;
        double returnamount=0;
        Ticket ticket=ticketMapper.selectTicketById(ticketId);
        int userid=ticket.getUserId();
        VIPCard vip=vipCardMapper.selectCardByUserId(userid);
        int scheduleId=ticket.getScheduleId();
        ScheduleItem s= scheduleService.getScheduleItemById(scheduleId);
        Price p=ticketMapper.selectTicketPriceById(ticketId);
        double amount=p.getprice();
        Date start_time=s.getStartTime();
        Date nowtime= new Date();
        List<TicketPromotion> t=ticketPromotionMapper.getPromotion(1);
        TicketPromotion ticketPromotion=t.get(0);
        int fulltime=ticketPromotion.getFullTime();
        int parttme=ticketPromotion.getPartTime();
        float discount=ticketPromotion.getDiscounts();
        int outtime=ticketPromotion.getOutTime();
        Date FullTime=new Date(start_time.getTime() -  fulltime * 60 * 60 * 1000);
        Date PartTime=new Date(start_time.getTime() -  parttme * 60 * 60 * 1000);
        Date OutTime=new Date(start_time.getTime() -  outtime * 60 * 60 * 1000);
        if(nowtime.compareTo(FullTime)==-1){
            returnamount=amount;
        }
        else{
            if ( nowtime.compareTo(PartTime)==-1){
                returnamount=amount*discount;
            }
            else{
                if (nowtime.compareTo(OutTime)==-1){
                    returnamount=amount*discount-(int)(amount*discount*0.1);
                }
                else{
                    canreturn=false;
                }
            }
        }
        if(canreturn){
            int VIPId=0;
            abolishTicket(ticket.getId());
        try{
           VIPId=vip.getId();
        }catch (Exception e){
         VIPId=0;
        }
            if(VIPId!=0){
                returnamount+=vip.getBalance();
                vipCardMapper.updateCardBalance(vip.getId(),returnamount);
            }
            return ResponseVO.buildSuccess("1");
        }
        else{
            return ResponseVO.buildSuccess("0");
        }
    }
    @Override
    public  ResponseVO getCost(int ticketId){
        int ID=ticketMapper.selectTicketById(ticketId).getScheduleId();
        return ResponseVO.buildSuccess(scheduleService.getScheduleItemById(ID).getFare());
    }
    @Override
    public ResponseVO getscheduleId(int ticketId){
        return ResponseVO.buildSuccess(ticketMapper.selectTicketById(ticketId).getScheduleId());
    }
    @Override
    public ResponseVO TicketPrice(double totalcost,List<Integer> ticketIds){
                 int n=ticketIds.size();
                 double price=totalcost/(double) n;
        DecimalFormat    df   = new DecimalFormat("######0.00");
        String str_price=df.format(price);
        double lastprice=Double.parseDouble(str_price);
        int i=0;
        while(i<n){
            ticketMapper.insertTicketPrice(ticketIds.get(i),lastprice);
            i++;
        }
        return ResponseVO.buildSuccess();

    }

    @Override
    public ResponseVO Evaluate(MarkRecordForm markRecordForm){
        try{
            Ticket ticket=ticketMapper.selectTicketById(markRecordForm.getTicketId());
            MarkRecord markRecord=movieMarkMapper.selectRecordByTicketId(ticket.getId());
            ScheduleItem scheduleItem=scheduleMapper.selectScheduleById(ticket.getScheduleId());

            long l = System.currentTimeMillis();
            Date now=new Date(l);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date end = scheduleItem.getEndTime();
            now=simpleDateFormat.parse(simpleDateFormat.format(now));
            end=simpleDateFormat.parse(simpleDateFormat.format(end));

            if(now.before(end)){
                return ResponseVO.buildFailure("您尚未看完电影");
            }

            if(markRecord!=null){
                return ResponseVO.buildFailure("您已经评分过");
            }


            double mark=markRecordForm.getMark();
            String comment=markRecordForm.getComment();
            if(!(mark==1 || mark==2 || mark==3 || mark==4 || mark==5)){
                return ResponseVO.buildFailure("评分只能为1 2 3 4 5");
            }
            movieMarkMapper.insertEvaluation(ticket.getUserId(),ticket.getId(),mark,scheduleItem.getMovieId(),comment);
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }

    }
}
