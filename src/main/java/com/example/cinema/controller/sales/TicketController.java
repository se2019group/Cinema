package com.example.cinema.controller.sales;

import com.example.cinema.bl.sales.TicketService;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.TicketForm;
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





}
