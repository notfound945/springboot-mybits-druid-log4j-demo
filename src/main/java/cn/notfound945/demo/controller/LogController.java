package cn.notfound945.demo.controller;

import cn.notfound945.demo.dao.LogDao;
import cn.notfound945.demo.pojo.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class LogController {

    @Autowired
    private LogDao logDao;

    @GetMapping(value = "/log")
    public List<Log> getAllLog() {
        return logDao.getAllLog();
    }

    @GetMapping(value = "/getlogbyusername")
    public List<Log> getLogByUserName(HttpServletRequest request) {
        String userName  = request.getParameter("username");
        return logDao.getLogByUserName(userName);
    }
    @GetMapping(value = "/getlogbyid/{id}")
    public Log getLogById(@PathVariable("id") int id) {
        return logDao.getLogById(id);
    }
    @GetMapping(value = "/deletelogbyid/{id}")
    public String deleteLogById(@PathVariable("id") int id) {
        int affectedNum = logDao.deleteLogById(id);
        return affectedNum > 0 ? "Delete Successful. " + affectedNum + " row(s) affected." :  "Delete Fail. " + affectedNum + " row affected.";
    }
}
