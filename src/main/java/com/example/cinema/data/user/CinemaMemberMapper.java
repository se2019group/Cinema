package com.example.cinema.data.user;

import com.example.cinema.po.CinemaMember;
import com.example.cinema.vo.CinemaMemberForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper

public interface CinemaMemberMapper {
    /**
     * 新增员工
     * @param cinemaMemberForm
     * @return
     */
    int insertCinemaMember(CinemaMemberForm cinemaMemberForm);

    /**
     * 查询所有账户
     * @return
     */
    List<CinemaMember> selectAllMember();

    /**
     * 根据用户id删除账号
     * @param userId
     */
    public void deleteMember(@Param("userId")int userId);

    /**
     * 修改影厅信息
     * @param cinemaMemberForm
     * @return
     */
    int updateMember(CinemaMemberForm cinemaMemberForm);

}
