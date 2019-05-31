package com.example.cinema.bl.user;

import com.example.cinema.vo.CinemaMemberForm;
import com.example.cinema.vo.ResponseVO;
import org.springframework.stereotype.Service;

@Service
public interface CinemaMemberService {
    /**
     * 更改用户类型
     * @param cinemaMemberForm
     * @return
     */
    ResponseVO addMember(CinemaMemberForm cinemaMemberForm);

    /**
     * 得到所有员工
     * @return
     */
    ResponseVO getAllMember();

    /**
     * 解雇员工
     * @param userId
     * @return
     */
    ResponseVO fireMember(int userId);

    /**
     * 修改员工信息
     * @param cinemaMemberForm
     * @return
     */
    ResponseVO updateMember(CinemaMemberForm cinemaMemberForm);
}
