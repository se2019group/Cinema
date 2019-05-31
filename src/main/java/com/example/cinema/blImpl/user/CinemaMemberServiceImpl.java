package com.example.cinema.blImpl.user;

import com.example.cinema.bl.user.CinemaMemberService;
import com.example.cinema.data.user.CinemaMemberMapper;
import com.example.cinema.po.CinemaMember;
import com.example.cinema.vo.CinemaMemberForm;
import com.example.cinema.vo.CinemaMemberVO;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CinemaMemberServiceImpl implements CinemaMemberService {

    @Autowired
    CinemaMemberMapper cinemaMemberMapper;

    @Override
    public ResponseVO addMember(CinemaMemberForm cinemaMemberForm){
        try{
            cinemaMemberMapper.insertCinemaMember(cinemaMemberForm);
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }
    @Override
    public ResponseVO getAllMember() {
        try {
            return ResponseVO.buildSuccess(cinemaMemberList2CinemaMemberVOList(cinemaMemberMapper.selectAllMember()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }
    private List<CinemaMemberVO> cinemaMemberList2CinemaMemberVOList(List<CinemaMember> cinemaMemberList){
        List<CinemaMemberVO> CinemaMemberVOList = new ArrayList<>();
        for(CinemaMember cinemaMember : cinemaMemberList){
            CinemaMemberVOList.add(new CinemaMemberVO(cinemaMember));
        }
        return CinemaMemberVOList;
    }

    @Override
    public ResponseVO fireMember(int userId){
        try{
            cinemaMemberMapper.deleteMember(userId);
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO updateMember(CinemaMemberForm cinemaMemberForm){
        try{
            cinemaMemberMapper.updateMember(cinemaMemberForm);
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }
}
