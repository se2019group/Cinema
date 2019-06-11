package com.example.cinema.controller.sales;

import com.example.cinema.bl.sales.TicketService;
import com.example.cinema.po.MarkRecord;
import com.example.cinema.po.TicketPromotion;
import com.example.cinema.vo.ConsumeForm;
import com.example.cinema.vo.MarkRecordForm;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.TicketForm;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by liying on 2019/4/16.
 */
@RestController
@RequestMapping("/ticket")
public class TicketController {
    @Autowired
    TicketService ticketService;

    @RequestMapping(value="/vip/buy", method = RequestMethod.POST)
    public ResponseVO buyTicketByVIPCard(@RequestParam("ids") List<Integer> ticketId, @RequestParam int couponId){
        return ticketService.completeByVIPCard(ticketId,couponId);
    }

    @PostMapping("/lockSeat")
    public ResponseVO lockSeat(@RequestBody TicketForm ticketForm){
        return ticketService.addTicket(ticketForm);
    }

    @RequestMapping(value="/buy", method = RequestMethod.POST)
    public ResponseVO buyTicket(@RequestParam(value="ids") List<Integer> ticketId,@RequestParam int couponId){
        return ticketService.completeTicket(ticketId,couponId);
    }

    @GetMapping("/get/{userId}")
    public ResponseVO getTicketByUserId(@PathVariable int userId){
        return ticketService.getTicketByUser(userId);
    }

    @GetMapping("/get/occupiedSeats")
    public ResponseVO getOccupiedSeats(@RequestParam int scheduleId){
        return ticketService.getBySchedule(scheduleId);
    }

    @RequestMapping(value="/cancel", method = RequestMethod.POST)
    public ResponseVO cancelTicket(@RequestParam("ids") List<Integer> ticketId){
        return ticketService.cancelTicket(ticketId);
    }

    @PostMapping("/delete")
    public ResponseVO deleteTicket(@RequestParam int ticketId){
        return ticketService.deleteTicket(ticketId);
    }


    @PostMapping("/consume")
    public ResponseVO ConsumeRecord(@RequestBody ConsumeForm consumeform){
        return ticketService.Consume_Record(consumeform);
    }

    @PostMapping("/abolish")
    public ResponseVO abolishTicket(@RequestParam int ticketId){
        return ticketService.abolishTicket(ticketId);
    }

    @GetMapping("/consume/{userId}")
    public ResponseVO getConsumeByuserId(@PathVariable int userId){
        return ticketService.getConsume_Record(userId);
    }

    @GetMapping("/{ticketId}")
    public ResponseVO getCost(@PathVariable int ticketId){
        return ticketService.getCost(ticketId);
    }

    @GetMapping("/schedule/{ticketId}")
    public ResponseVO getscheduleId(@PathVariable int ticketId){
        return ticketService.getscheduleId(ticketId);
    }

    @GetMapping("/promotion/get")
    public ResponseVO getTicketPromotion(){
        return ticketService.TicketPromotion();
    }
    @PostMapping("/promotion/change")
    public ResponseVO changeTicketPromotion(@RequestBody TicketPromotion ticketPromotion){
        return ticketService.changeTicketPromotion(ticketPromotion);
    }
    @PostMapping("/return/{ticketId}")
    public ResponseVO TicketReturn(@PathVariable int ticketId){
        return ticketService.TicketReturn(ticketId);
    }

    @PostMapping("/price")
    public ResponseVO TicketReturn(@RequestParam double totalcost,@RequestParam List<Integer> ticketIds){
        return ticketService.TicketPrice(totalcost,ticketIds);
    }

    @PostMapping("/evaluate")
    public ResponseVO Evaluate(@RequestBody MarkRecordForm markRecordForm){
        return ticketService.Evaluate(markRecordForm);
    }
}
