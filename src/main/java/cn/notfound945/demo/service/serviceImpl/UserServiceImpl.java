package cn.notfound945.demo.service.serviceImpl;

import cn.notfound945.demo.dao.UserDao;
import cn.notfound945.demo.pojo.User;
import cn.notfound945.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public List<User> getUserByUserName(String userName) {
        return userDao.getUserByUserName(userName);
    }

    @Override
    public int deleteUserById(int id) {
        return userDao.deleteUserById(id);
    }

    @Override
    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    @Override
    public List<User> getAllUser() {
        return  userDao.getAllUser();
    }
}
