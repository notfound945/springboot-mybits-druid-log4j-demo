package cn.notfound945.demo.controller;

import cn.notfound945.demo.dao.LogDao;
import cn.notfound945.demo.pojo.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class LogController {

    @Autowired
    private LogDao logDao;

    @RequestMapping(value = "/log")
    public List<Log> getAllLog() {
        return logDao.getAllLog();
    }

    @RequestMapping(value = "/getlogbyusername")
    public List<Log> getLogByUserName(HttpServletRequest request) {
        String userName  = request.getParameter("username");
        return logDao.getLogByUserName(userName);
    }
    @RequestMapping(value = "/getlogbyid/{id}")
    public Log getLogById(@PathVariable("id") int id) {
        return logDao.getLogById(id);
    }
    @RequestMapping(value = "/deletelogbyid/{id}")
    public String deleteLogById(@PathVariable("id") int id) {
        int affectedNum = logDao.deleteLogById(id);
        return affectedNum > 0 ? "Delete Successful. " + affectedNum + " row(s) affected." :  "Delete Fail. " + affectedNum + " row affected.";
    }
}
