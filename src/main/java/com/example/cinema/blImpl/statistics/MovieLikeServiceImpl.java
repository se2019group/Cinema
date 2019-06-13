package com.example.cinema.blImpl.statistics;
import java.util.ArrayList;
import java.util.List;

import com.example.cinema.bl.statistics.MovieLikeService;
import com.example.cinema.blImpl.management.schedule.MovieServiceForBl;
import com.example.cinema.data.management.MovieMapper;
import com.example.cinema.data.statistics.MovieLikeMapper;
import com.example.cinema.po.DateLike;
import com.example.cinema.po.Movie;
import com.example.cinema.vo.DateLikeVO;
import com.example.cinema.vo.LikeMovieVO;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fjj
 * @date 2019/4/28 3:07 PM
 */
@Service
public class MovieLikeServiceImpl implements MovieLikeService {
    private static final String ALREADY_LIKE_ERROR_MESSAGE = "用户已标记该电影为想看";
    private static final String UNLIKE_ERROR_MESSAGE = "用户未标记该电影为想看";
    private static final String MOVIE_NOT_EXIST_ERROR_MESSAGE = "电影不存在";

    @Autowired
    private MovieLikeMapper movieLikeMapper;
    @Autowired
    private MovieServiceForBl movieServiceForBl;
    @Autowired
    private MovieMapper movieMapper;

    @Override
    public ResponseVO likeMovie(int userId, int movieId) {

        //todo: user 判空
        if (userLikeTheMovie(userId, movieId)) {
            return ResponseVO.buildFailure(ALREADY_LIKE_ERROR_MESSAGE);
        } else if (movieServiceForBl.getMovieById(movieId) == null) {
            return ResponseVO.buildFailure(MOVIE_NOT_EXIST_ERROR_MESSAGE);
        }
        try {
            return ResponseVO.buildSuccess(movieLikeMapper.insertOneLike(movieId, userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO unLikeMovie(int userId, int movieId) {
        if (!userLikeTheMovie(userId, movieId)) {
            return ResponseVO.buildFailure(UNLIKE_ERROR_MESSAGE);
        } else if (movieServiceForBl.getMovieById(movieId) == null) {
            return ResponseVO.buildFailure(MOVIE_NOT_EXIST_ERROR_MESSAGE);
        }
        try {
            return ResponseVO.buildSuccess(movieLikeMapper.deleteOneLike(movieId, userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }

    }

    @Override
    public ResponseVO getCountOfLikes(int movieId) {
        try {
            return ResponseVO.buildSuccess(movieLikeMapper.selectLikeNums(movieId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getLikeNumsGroupByDate(int movieId) {
        try {
            return ResponseVO.buildSuccess(dateLikeList2DateLikeVOList(movieLikeMapper.getDateLikeNum(movieId)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getTop10Movie(){
        try {
            List<Movie> movies=movieMapper.selectAllMovie();
            List<Integer> likeNums=new ArrayList<Integer>();
            List<Movie> top10Movies=new ArrayList<Movie>();
            int i,max;

            i=0;
            while(i<movies.size()){
                likeNums.add(movieLikeMapper.selectLikeNums(movies.get(i).getId()));
                i+=1;
            }

            while(top10Movies.size()<likeNums.size() && top10Movies.size()<4){
                max=0;
                i=1;
                while(i<likeNums.size()){
                    if(likeNums.get(i)>likeNums.get(max)){
                        max=i;
                    }
                    i+=1;
                }
                likeNums.set(max, -1);
                top10Movies.add(movies.get(max));
            }

            return ResponseVO.buildSuccess(top10Movies);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    private boolean userLikeTheMovie(int userId, int movieId) {
        return movieLikeMapper.selectLikeMovie(movieId, userId) == 0 ? false : true;
    }
    

    private List<DateLikeVO> dateLikeList2DateLikeVOList(List<DateLike> dateLikeList){
        List<DateLikeVO> dateLikeVOList = new ArrayList<>();
        for(DateLike dateLike : dateLikeList){
            dateLikeVOList.add(new DateLikeVO(dateLike));
        }
        return dateLikeVOList;
    }

    private List<LikeMovieVO> moviesToLikeMovieVOs(List<Movie> movies){
        int i;
        LikeMovieVO likeMovieVO;
        List<LikeMovieVO> likeMovieVOs=new ArrayList<LikeMovieVO>();

        i=0;
        while(i<movies.size()){
            likeMovieVO=new LikeMovieVO();
            likeMovieVO.setLikeNum(movieLikeMapper.selectLikeNums(movies.get(i).getId()));
            likeMovieVO.setName(movies.get(i).getName());
            likeMovieVOs.add(likeMovieVO);
            i+=1;
        }

        return likeMovieVOs;
    }
}
