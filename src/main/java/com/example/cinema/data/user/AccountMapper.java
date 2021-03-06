package com.example.cinema.data.user;

import com.example.cinema.po.User;
import com.example.cinema.vo.UserForm;
import com.example.cinema.po.CinemaMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huwen
 * @date 2019/3/23
 */
@Mapper
public interface AccountMapper {

    /**
     * 创建一个新的账号
     * @param username
     * @param password
     * @return
     */
    public int createNewAccount(@Param("username") String username, @Param("password") String password);

    /**
     * 根据用户名查找账号
     * @param username
     * @return
     */
    public User getAccountByName(@Param("username") String username);

    /**
     * 根据用户id查找账号
     * @param username
     * @return
     */
    public User getAccountById(@Param("userId") int userId);

    /**
     * 根据用户名查找账号
     * @param username
     * @return
     */
    public CinemaMember getMemberByName(@Param("username") String username);
    /**
     * 根据用户id删除账号
     * @param userId
     */
    public void deleteAccount(@Param("userId")int userId);

    /**
     * 查询所有账户
     * @return
     */
    List<User> selectAllUser();
}
