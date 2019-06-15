package com.example.cinema.controller.router;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author deng
 * @date 2019/03/11
 */
@Controller
public class ViewController {
    @RequestMapping(value = "/index")
    public String getIndex() {
        return "index";
    }

    @RequestMapping(value = "/signUp")
    public String getSignUp() {
        return "signUp";
    }

    @RequestMapping(value = "/admin/movie/manage")
    public String getAdminMovieManage() {
        return "adminMovieManage";
    }

    @RequestMapping(value = "/admin/session/manage")
    public String getAdminSessionManage() {
        return "adminScheduleManage";
    }

    @RequestMapping(value = "/admin/cinema/manage")
    public String getAdminCinemaManage() {
        return "adminCinemaManage";
    }

    @RequestMapping(value = "/admin/promotion/manage")
    public String getAdminPromotionManage() {
        return "adminPromotionManage";
    }

    @RequestMapping(value = "/admin/cinema/statistic")
    public String getAdminCinemaStatistic() {
        return "adminCinemaStatistic";
    }

    @RequestMapping(value = "/admin/VIP/discount")
    public String getAdminVIPdiscount() {
        return "adminVIPdiscount";
    }

    @RequestMapping(value = "/admin/ticket/return")
    public String getAdminTickerRerun() {
        return "adminTicketReturn";
    }

    @RequestMapping(value = "/admin/staff/manage")
    public String getAdminStaffManage() {
        return "adminStaffManage";
    }

    @RequestMapping(value = "/admin/Coupon/give")
    public String getAdminCoupongive() {
        return "adminCoupongive";
    }

    @RequestMapping(value = "/admin/movieDetail")
    public String getAdminMovieDetail(@RequestParam int id) { return "adminMovieDetail"; }

    //以下是员工界面
    @RequestMapping(value = "/member/movie/manage")
    public String getMemberMovieManage() {
        return "memberMovieManage";
    }

    @RequestMapping(value = "/member/session/manage")
    public String getMemberSessionManage() {
        return "memberScheduleManage";
    }

    @RequestMapping(value = "/member/cinema/manage")
    public String getMemberCinemaManage() {
        return "memberCinemaManage";
    }

    @RequestMapping(value = "/member/promotion/manage")
    public String getMemberPromotionManage() {
        return "memberPromotionManage";
    }

    @RequestMapping(value = "/member/cinema/statistic")
    public String getMemberCinemaStatistic() { return "memberCinemaStatistic"; }

    @RequestMapping(value = "/member/VIP/discount")
    public String getMemberVIPdiscount() {
        return "memberVIPdiscount";
    }

    @RequestMapping(value = "/member/ticket/return")
    public String getMemberTickerRerun() {
        return "memberTicketReturn";
    }

    @RequestMapping(value = "/member/Coupon/give")
    public String getMemberCoupongive() {
        return "memberCoupongive";
    }

    @RequestMapping(value = "/member/movieDetail")
    public String getMemberMovieDetail(@RequestParam int id) { return "memberMovieDetail"; }

    //以下是用户界面
    @RequestMapping(value = "/user/home")
    public String getUserHome() {
        return "userHome";
    }

    @RequestMapping(value = "/user/buy")
    public String getUserBuy() {
        return "userBuy";
    }

    @RequestMapping(value = "/user/movieDetail")
    public String getUserMovieDetail(@RequestParam int id) {
        return "userMovieDetail";
    }

    @RequestMapping(value = "/user/movieDetail/buy")
    public String getUserMovieBuy(@RequestParam int id) {
        return "userMovieBuy";
    }

    @RequestMapping(value = "/user/movie")
    public String getUserMovie() {
        return "userMovie";
    }

    @RequestMapping(value = "/user/member")
    public String getUserMember() {
        return "userMember";
    }

    @RequestMapping(value = "/user/consume")
    public String getUserConsume() {
        return "userConsume";
    }

    @RequestMapping(value = "/user/recharge")
    public String getUserRecharge() {
        return "userRecharge";
    }
}
