package com.example.cinema.data.sales;
import com.example.cinema.vo.ConsumeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
@Mapper
public interface ConsumeMapper {
    void insertConsume(ConsumeRecord consumerecord);
    List<ConsumeRecord> searchConsumeByuserId(int userid);

}
