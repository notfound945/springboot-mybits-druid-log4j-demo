package cn.notfound945.demo.dao;

import cn.notfound945.demo.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserDao {
    List<User> getAllUser();
    List<User> getUserByUserName(String userName);
    User getUserById(int id);
    User getPasswordByUserName(String userName);
    int deleteUserById(int id);
}
