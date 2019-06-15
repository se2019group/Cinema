package com.example.cinema.controller.user;

import com.example.cinema.bl.user.AccountService;
import com.example.cinema.blImpl.user.AccountServiceImpl;
import com.example.cinema.config.InterceptorConfiguration;
import com.example.cinema.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author huwen
 * @date 2019/3/23
 */
@RestController()
public class AccountController {
    private final static String ACCOUNT_INFO_ERROR="用户名或密码错误";
    @Autowired
    private AccountServiceImpl accountService;
    @PostMapping("/login")
    public ResponseVO login(@RequestBody UserForm userForm, HttpSession session){
        UserVO user = accountService.login(userForm);
        if(user==null) {
            CinemaMemberVO member = accountService.memberlogin(userForm);
            if (member == null) {
                return ResponseVO.buildFailure(ACCOUNT_INFO_ERROR);
            }
            return ResponseVO.buildSuccess(member);
        }
        //注册session
        session.setAttribute(InterceptorConfiguration.SESSION_KEY,userForm);
        return ResponseVO.buildSuccess(user);
    }
    @PostMapping("/register")
    public ResponseVO registerAccount(@RequestBody UserForm userForm){
        return accountService.registerAccount(userForm);
    }

    @PostMapping("/logout")
    public ResponseVO logOut(HttpSession session){
        session.removeAttribute(InterceptorConfiguration.SESSION_KEY);
        return ResponseVO.buildSuccess("index");
    }

    @RequestMapping(value = "/search/member",method = RequestMethod.GET)
    public ResponseVO getAccountByName(@RequestParam String username){
        return accountService.getAccountByName(username);
    }

    @PostMapping("/account/delete")
    public ResponseVO deleteAccount(@RequestParam int userId){
        return accountService.deleteAccount(userId);
    }

    @RequestMapping(value = "/account/all", method = RequestMethod.GET)
    public ResponseVO getAllAccount(){
        return accountService.getAllAccount();
    }
    
    @GetMapping("/peopleMatched")
    public ResponseVO accountsMatched(@RequestParam int symbol,@RequestParam int target) {
    	return accountService.accountsMatched(symbol, target);
    }
}
