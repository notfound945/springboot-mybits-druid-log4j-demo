package cn.notfound945.demo.service;

import cn.notfound945.demo.pojo.User;

import java.util.List;

public interface UserService {
    User getUserById(int id);
    User getPasswordByUserName(String userName);
    List<User> getAllUser();
    List<User> getUserByUserName(String userName);
    int deleteUserById(int id);
}
