package com.example.cinema.controller.statistics;

import com.example.cinema.bl.statistics.MovieLikeService;
import com.example.cinema.bl.statistics.StatisticsService;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author fjj
 * @date 2019/4/16 1:34 PM
 */
@RestController
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private MovieLikeService movieLikeService;

    @RequestMapping(value = "statistics/scheduleRate", method = RequestMethod.GET)
    public ResponseVO getScheduleRateByDate(@RequestParam(required = false) Date date){
        //此处date为非必填参数，若不填则默认为当天
        return statisticsService.getScheduleRateByDate(date);
    }

    @RequestMapping(value = "statistics/boxOffice/total", method = RequestMethod.GET)
    public ResponseVO getTotalBoxOffice(){
        return statisticsService.getTotalBoxOffice();
    }

    @RequestMapping(value = "statistics/audience/price", method = RequestMethod.GET)
    public ResponseVO getAudiencePrice(){
        return statisticsService.getAudiencePriceSevenDays();
    }

    @RequestMapping(value = "statistics/PlacingRate", method = RequestMethod.GET)
    public ResponseVO getMoviePlacingRateByDate(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date){
        return statisticsService.getMoviePlacingRateByDate(date);
    }

    @RequestMapping(value = "statistics/popular/movie", method = RequestMethod.GET)
    public ResponseVO getPopularMovies(@RequestParam int days, @RequestParam int movieNum){
        return statisticsService.getPopularMovies(days, movieNum);
    }

    @RequestMapping(value = "movieLike/top10", method = RequestMethod.GET)
    public ResponseVO getTop10Movie(){
        return movieLikeService.getTop10Movie();
    }

    @RequestMapping(value = "movieMark/average", method = RequestMethod.GET)
    public ResponseVO getMarkRecord(@RequestParam int movieId){
        return statisticsService.getMarkRecord(movieId);
    }

    @RequestMapping(value = "movieMark/comment", method = RequestMethod.GET)
    public ResponseVO getAllComments(@RequestParam int movieId){
        return statisticsService.getAllComments(movieId);
    }

    @RequestMapping(value = "movieMark/commentforUser", method = RequestMethod.GET)
    public ResponseVO getCommentsByUserId(@RequestParam int userId){ return statisticsService.getCommentsByUserId(userId);}








}
