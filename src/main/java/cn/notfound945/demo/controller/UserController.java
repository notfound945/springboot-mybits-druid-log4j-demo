package cn.notfound945.demo.controller;

import cn.notfound945.demo.pojo.User;
import cn.notfound945.demo.service.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @RequestMapping(value = "/user/all")
    public List<User> getAllUser() {
        return userService.getAllUser();
    }

    @RequestMapping(value = "/user/getuserbyusername")
    public List<User> getLogByUserName(HttpServletRequest request) {
        String userName  = request.getParameter("username");
        return userService.getUserByUserName(userName);
    }
    @RequestMapping(value = "/user/getuserbyid/{id}")
    public User getUserById(@PathVariable("id") int id) {
        return userService.getUserById(id);
    }
    @RequestMapping(value = "/user/deleteuserbyid/{id}")
    public String deleteUserById(@PathVariable("id") int id) {
        int affectedNum = userService.deleteUserById(id);
        return affectedNum > 0 ? "Delete Successful. " + affectedNum + " row(s) affected." :  "Delete Fail. " + affectedNum + " row affected.";
    }

}
