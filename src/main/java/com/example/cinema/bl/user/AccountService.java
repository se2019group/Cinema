package com.example.cinema.bl.user;

import com.example.cinema.vo.UserForm;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.UserVO;

/**
 * @author huwen
 * @date 2019/3/23
 */
public interface AccountService {

    /**
     * 注册账号
     * @return
     */
    public ResponseVO registerAccount(UserForm userForm);

    /**
     * 用户登录，登录成功会将用户信息保存再session中
     * @return
     */
    public UserVO login(UserForm userForm);


    /**
     * 管理员可用，删除相应id的账号
     * @param userId
     * @return
     */
    public ResponseVO deleteAccount(int userId);

    /**
     * 管理员可用，查询账号
     * @param name
     * @return
     */
    public ResponseVO getAccountByName(String name);

    /**
     * 管理员可用，查看所有账号信息
     * @return
     */
    public ResponseVO getAllAccount();
    
    /**
     * symbol为0表示全体用户，为1表示会员
     * @param symbol
     * @param target
     * @return
     */
    public ResponseVO accountsMatched(int symbol,int target);

}
