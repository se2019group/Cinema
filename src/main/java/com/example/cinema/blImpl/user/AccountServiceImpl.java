package com.example.cinema.blImpl.user;

import com.example.cinema.bl.promotion.VIPService;
import com.example.cinema.bl.sales.TicketService;
import com.example.cinema.bl.user.AccountService;
import com.example.cinema.data.promotion.VIPCardMapper;
import com.example.cinema.data.user.AccountMapper;
import com.example.cinema.data.user.CinemaMemberMapper;
import com.example.cinema.po.CinemaMember;
import com.example.cinema.po.RechargeForm;
import com.example.cinema.po.User;
import com.example.cinema.po.VIPCard;
import com.example.cinema.vo.UserForm;
import com.example.cinema.vo.ConsumeRecord;
import com.example.cinema.vo.PeopleMatched;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huwen
 * @date 2019/3/23
 */
@Service
public class AccountServiceImpl implements AccountService {
    private final static String ACCOUNT_EXIST = "账号已存在";
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private VIPCardMapper vipCardMapper;
    @Autowired
    private VIPService vipService;
	@Autowired
	private  CinemaMemberMapper cinemaMemberMapper;

    @Override
    public ResponseVO registerAccount(UserForm userForm) {
        try {
            accountMapper.createNewAccount(userForm.getUsername(), userForm.getPassword());
        } catch (Exception e) {
            return ResponseVO.buildFailure(ACCOUNT_EXIST);
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public UserVO login(UserForm userForm) {
        User user = accountMapper.getAccountByName(userForm.getUsername());
        if (null == user || !user.getPassword().equals(userForm.getPassword())) {
            return null;
        }
        return new UserVO(user);
    }

    @Override
    public ResponseVO deleteAccount(int userId){
        try{
            accountMapper.deleteAccount(userId);
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getAccountByName(String username){
        try{
			List<CinemaMember> c=cinemaMemberMapper.getAccountByName(username);
            return ResponseVO.buildSuccess(c);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getAllAccount() {
        try {
            return ResponseVO.buildSuccess(userList2UserVOList(accountMapper.selectAllUser()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }
    private List<UserVO> userList2UserVOList(List<User> userList){
        List<UserVO> userVOList = new ArrayList<>();
        for(User user : userList){
            userVOList.add(new UserVO(user));
        }
        return userVOList;
    }

    @Override
	public ResponseVO accountsMatched(int symbol, int target) {
		try {
			List<User> users=accountMapper.selectAllUser();
			List<ConsumeRecord> consumeRecords;
			int i,j;
			double sum;
						
			//满足消费金额门槛
			i=0;
			while(i<users.size()) {
				consumeRecords=(List<ConsumeRecord>)ticketService.getConsume_Record(users.get(i).getId()).getContent();
				sum=0;
				j=0;
				while(j<consumeRecords.size()) {
					sum+=consumeRecords.get(j).getamount();
					j+=1;
				}
				
				if(sum<target) {
					users.remove(i);
					i-=1;
				}
				i+=1;
			}
			
			//满足人群选择
			if(symbol==0) {
				return ResponseVO.buildSuccess(UsersToPeopleMatcheds(users));
			}
			else {
				List<VIPCard> vipCards=vipCardMapper.selectAllCard();
				
				i=0;
				while(i<users.size()) {
					j=0;
					while(j<vipCards.size()) {
						if(users.get(i).getId()==vipCards.get(j).getUserId()) {
							break;
						}
						j+=1;
					}
					if(j==users.size()) {
						users.remove(i);
						i-=1;
					}
					i+=1;
				}
				
				return ResponseVO.buildSuccess(UsersToPeopleMatcheds(users));
			}
        } catch (Exception e) {
            return ResponseVO.buildFailure(ACCOUNT_EXIST);
        }
	}

	private List<PeopleMatched> UsersToPeopleMatcheds(List<User> users){
		List<PeopleMatched> peopleMatcheds=new ArrayList<>();
		List<ConsumeRecord> consumeRecords;
		List<RechargeForm> rechargeForms;
		PeopleMatched peopleMatched;
		int i,j;
		double sum;
		
		i=0;
		while(i<users.size()) {
			peopleMatched=new PeopleMatched();
			peopleMatched.setUserName(users.get(i).getUsername());
			peopleMatched.setUserId(users.get(i).getId());
			
			//计算消费金额
			consumeRecords=(List<ConsumeRecord>)ticketService.getConsume_Record(users.get(i).getId()).getContent();
			sum=0;
			j=0;
			while(j<consumeRecords.size()) {
				sum+=consumeRecords.get(j).getamount();
				j+=1;
			}
			peopleMatched.setConsumeAmount(sum);
			
			//计算充值金额
			rechargeForms=(List<RechargeForm>)vipService.get_recharge_record(users.get(i).getId()).getContent();
			if(rechargeForms==null) {
				peopleMatched.setChargeAmount(0);
			}
			else {
				sum=0;
				j=0;
				while(j<rechargeForms.size()) {
					sum+=rechargeForms.get(j).getamount();
					j+=1;
				}
				peopleMatched.setChargeAmount(sum);
			}
			
			peopleMatcheds.add(peopleMatched);
			i+=1;
		}
		
		return peopleMatcheds;
	}
}
