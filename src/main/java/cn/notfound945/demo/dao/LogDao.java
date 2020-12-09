package cn.notfound945.demo.dao;

import cn.notfound945.demo.pojo.Log;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface LogDao {
    List<Log> getAllLog();
    List<Log> getLogByUserName(String userName);
    Log getLogById(int id);
    int deleteLogById(int id);
}
