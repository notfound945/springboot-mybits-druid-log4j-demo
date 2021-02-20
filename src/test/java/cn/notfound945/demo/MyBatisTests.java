package cn.notfound945.demo;

import cn.notfound945.demo.pojo.User;
import cn.notfound945.demo.service.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Controller;

import java.util.List;

@SpringBootTest
public class MyBatisTests {
    @Autowired
    UserServiceImpl userService;

    @Test
    public void getPasswordByUsername() {

        User bean = userService.getPasswordByUserName("hello");
        System.out.println(bean);
    }

    @Test
    public void getAllUser() {
        List<User> allUser = userService.getAllUser();
        System.out.println(allUser);
    }
}
