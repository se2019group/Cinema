package com.example.cinema.blImpl.statistics;

import com.example.cinema.bl.statistics.StatisticsService;
import com.example.cinema.data.management.HallMapper;
import com.example.cinema.data.management.MovieMapper;
import com.example.cinema.data.management.ScheduleMapper;
import com.example.cinema.data.sales.MovieMarkMapper;
import com.example.cinema.data.sales.TicketMapper;
import com.example.cinema.data.statistics.StatisticsMapper;
import com.example.cinema.po.*;
import com.example.cinema.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author fjj
 * @date 2019/4/16 1:34 PM
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private StatisticsMapper statisticsMapper;
    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private TicketMapper ticketMapper;
    @Autowired
    private HallMapper hallMapper;
    @Autowired
    private MovieMarkMapper movieMarkMapper;
    
    @Override
    public ResponseVO getScheduleRateByDate(Date date) {
        try{
            Date requireDate = date;
            if(requireDate == null){
                requireDate = new Date();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            requireDate = simpleDateFormat.parse(simpleDateFormat.format(requireDate));

            Date nextDate = getNumDayAfterDate(requireDate, 1);
            return ResponseVO.buildSuccess(movieScheduleTimeList2MovieScheduleTimeVOList(statisticsMapper.selectMovieScheduleTimes(requireDate, nextDate)));

        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getTotalBoxOffice() {
        try {
            return ResponseVO.buildSuccess(movieTotalBoxOfficeList2MovieTotalBoxOfficeVOList(statisticsMapper.selectMovieTotalBoxOffice()));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getAudiencePriceSevenDays() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            Date startDate = getNumDayAfterDate(today, -6);
            List<AudiencePriceVO> audiencePriceVOList = new ArrayList<>();
            for(int i = 0; i < 7; i++){
                AudiencePriceVO audiencePriceVO = new AudiencePriceVO();
                Date date = getNumDayAfterDate(startDate, i);
                audiencePriceVO.setDate(date);
                List<AudiencePrice> audiencePriceList = statisticsMapper.selectAudiencePrice(date, getNumDayAfterDate(date, 1));
                double totalPrice = audiencePriceList.stream().mapToDouble(item -> item.getTotalPrice()).sum();
                audiencePriceVO.setPrice(Double.parseDouble(String.format("%.2f", audiencePriceList.size() == 0 ? 0 : totalPrice / audiencePriceList.size())));
                audiencePriceVOList.add(audiencePriceVO);
            }
            return ResponseVO.buildSuccess(audiencePriceVOList);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getMoviePlacingRateByDate(Date date) {
    	try {
            int i,j,ticketNum,seatNum;
            List<Movie> movies=movieMapper.selectAllMovie();
            double[] rates=new double[movies.size()];
            RelativeRatesVO relativeRatesVO=new RelativeRatesVO();
            relativeRatesVO.setMovies(movies);
            List<ScheduleItem> scheduleItems;
            List<Hall> halls;
            Date requireDate;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = simpleDateFormat.parse(simpleDateFormat.format(date));
            int size;
            i=0;
            seatNum=0;
            halls=hallMapper.selectAllHall();
            while(i<halls.size()) {
            	seatNum+=(halls.get(i).getColumn()*halls.get(i).getRow());
            	i+=1;
            }
            
            i=0;
            while(i<movies.size()) {
            	scheduleItems=scheduleMapper.selectScheduleByMovieId(movies.get(i).getId());
            	j=0;
            	ticketNum=0;
            	size=0;
            	while(j<scheduleItems.size()) {
                    requireDate=simpleDateFormat.parse(simpleDateFormat.format(scheduleItems.get(j).getStartTime()));
            	    if(date.equals(requireDate)) {
                        ticketNum += ticketMapper.selectTicketsBySchedule(scheduleItems.get(j).getId()).size();
                        size++;
                    }
            		j+=1;
            	}
            	if(seatNum*size!=0)
            	    rates[i]=(double)ticketNum/(seatNum*size);
            	else
            	    rates[i]=0;
            	i+=1;
            }
            relativeRatesVO.setRates(rates);
            
            return ResponseVO.buildSuccess(relativeRatesVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getPopularMovies(int days, int movieNum) {
        try {
            // 处理查询表单的起止时间
            Date endDate=new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            endDate = simpleDateFormat.parse(simpleDateFormat.format(getNumDayAfterDate(endDate,1)));
            Date startDate = getNumDayBeforeDate(endDate, days);
            List<MovieTotalBoxOffice>  PopularMovies= statisticsMapper.selectMovieTotalBoxOfficeByDate(startDate,endDate);
            List<MovieTotalBoxOffice>  PopularMoviesRank=  new ArrayList<>();
            int i=0;
            if(movieNum>PopularMovies.size()){
                movieNum=PopularMovies.size();
            }
                for(MovieTotalBoxOffice movie : PopularMovies) {
                    if (i < movieNum){
                        PopularMoviesRank.add( movie );
                    i++;}
                }
            return  ResponseVO.buildSuccess(PopularMoviesRank);
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }


    /**
     * 获得num天后的日期
     * @param oldDate
     * @param num
     * @return
     */
    Date getNumDayAfterDate(Date oldDate, int num){
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(oldDate);
        calendarTime.add(Calendar.DAY_OF_YEAR, num);
        return calendarTime.getTime();
    }
    /**
     * 获得num天前的日期
     * @param oldDate
     * @param num
     * @return
     */
    Date getNumDayBeforeDate(Date oldDate, int num){
        num=(num)*-1;
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(oldDate);
        calendarTime.add(Calendar.DAY_OF_YEAR, num);
        return calendarTime.getTime();
    }


    private List<MovieScheduleTimeVO> movieScheduleTimeList2MovieScheduleTimeVOList(List<MovieScheduleTime> movieScheduleTimeList){
        List<MovieScheduleTimeVO> movieScheduleTimeVOList = new ArrayList<>();
        for(MovieScheduleTime movieScheduleTime : movieScheduleTimeList){
            movieScheduleTimeVOList.add(new MovieScheduleTimeVO(movieScheduleTime));
        }
        return movieScheduleTimeVOList;
    }


    private List<MovieTotalBoxOfficeVO> movieTotalBoxOfficeList2MovieTotalBoxOfficeVOList(List<MovieTotalBoxOffice> movieTotalBoxOfficeList){
        List<MovieTotalBoxOfficeVO> movieTotalBoxOfficeVOList = new ArrayList<>();
        for(MovieTotalBoxOffice movieTotalBoxOffice : movieTotalBoxOfficeList){
            movieTotalBoxOfficeVOList.add(new MovieTotalBoxOfficeVO(movieTotalBoxOffice));
        }
        return movieTotalBoxOfficeVOList;
    }

    /**
     * 求某部电影评分
     * @param movieId
     * @return
     */
    @Override
    public ResponseVO getMarkRecord(int movieId){
        try{
            List <MarkRecord> markRecords =movieMarkMapper.selectRecordsByMovieId(movieId);
            double sumOfMark=0.0;
            double averageMark=0.0;
            for(int i=0;i<markRecords.size();i++){
                sumOfMark=sumOfMark+markRecords.get(i).getMark();
            }
            if(markRecords.size()!=0)
                averageMark=(double)sumOfMark/markRecords.size();
            else
                averageMark=0;
            DecimalFormat decimalFormat = new DecimalFormat(".00");
            String result=decimalFormat.format(averageMark);
            return ResponseVO.buildSuccess(result);
        }catch (Exception e){
            return ResponseVO.buildFailure("求电影评分失败");
        }
    }

    @Override
    public ResponseVO getAllComments(int movieId){
        try{
            List <MarkRecord> markRecords =movieMarkMapper.selectRecordsByMovieId(movieId);
//            ArrayList <String> comments=new ArrayList<String>();
//            for (int i=0;i<markRecords.size();i++){
//                if(markRecords.get(i).getComment().length()!=0){
//                    comments.add(markRecords.get(i).getComment());
//                }
//            }
//            for (int i=0;i<comments.size();i++){
//                if (comments.get(i)!=null)
//                    System.out.println(comments.get(i));
//            }
            return ResponseVO.buildSuccess(markRecords);
        }catch (Exception e){
            return ResponseVO.buildFailure("求电影评论失败");
        }
    }

    @Override
    public ResponseVO getCommentsByUserId(int userId){
        try{
            List <MarkRecord> markRecords =movieMarkMapper.selectRecordsByUserId(userId);
            ArrayList <String> comments=new ArrayList<String>();
            ArrayList <String> commentsFormovie=new ArrayList<String>();
            List<MovieComment> movieComments=new ArrayList<MovieComment>();
            MovieComment movieComment;
            for (int i=0;i<markRecords.size();i++){
                if(markRecords.get(i).getComment().length()!=0){
                    movieComment=new MovieComment();
                    movieComment.setMovieId(markRecords.get(i).getMovieId());
                    movieComment.setComment(markRecords.get(i).getComment());

                    movieComments.add(movieComment);
                }
            }
//            for (int i=0;i<movieComments.size();i++){
//               System.out.print(movieComments.get(i).getComment());
//               System.out.println(movieComments.get(i).getMovieId());
//            }
            return ResponseVO.buildSuccess(markRecords);
        }catch (Exception e){
            return ResponseVO.buildFailure("查询用户评论失败");
        }
    }
}
