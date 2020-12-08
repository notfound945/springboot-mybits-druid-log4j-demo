package cn.notfound945.demo.dao;

import cn.notfound945.demo.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {
    List<User> getAllUser();
    List<User> getUserByUserName(String userName);
    User getUserById(int id);
    int deleteUserById(int id);
}
