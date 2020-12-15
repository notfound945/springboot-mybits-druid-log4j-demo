package cn.notfound945.demo.controller;

import cn.notfound945.demo.pojo.ResponseMsg;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@RestController
public class RootController {
    /**
     * 未登录返回
     *
     * @return 响应
     */
    @GetMapping(value = "/noLogin")
    public ResponseMsg responseNoLogin() {
        String generatedString = RandomStringUtils.random(8, true, true);
        ResponseMsg responseMsg = new ResponseMsg(401, "No login", "", "Login", "");
        responseMsg.setRequestId(generatedString);
        return responseMsg;
    }

    /**
     * 未得到授权
     *
     * @return 响应
     */
    @GetMapping(value = "/noAuth")
    public ResponseMsg responseMsg() {
        String generatedString = RandomStringUtils.random(8, true, true);
        ResponseMsg responseMsg = new ResponseMsg(401, "No authorization", "", "Authorization", "Authorization");
        responseMsg.setRequestId(generatedString);
        return responseMsg;
    }

    /**
     * 用户登录接口
     *
     * @param request 请求参数
     * @return 响应
     */
    @PostMapping(value = "/login")
    public ResponseMsg responseLogin(HttpServletRequest request) {
        String generatedString = RandomStringUtils.random(8, true, true);
        ResponseMsg responseMsg = new ResponseMsg(200, "login", "", "Login", "");
        responseMsg.setRequestId(generatedString);
        try {
            JSONObject jsonParam = this.getJSONParam(request);
//            去除两端空格字符
            String validate = jsonParam.get("validate").toString().trim();
            if (!validate.equals(request.getSession().getAttribute("sessionKey"))) {
                responseMsg.setRequestCode(403);
                responseMsg.setResponseRemark("验证码不匹配!");
                return responseMsg;
            }
            String username = jsonParam.get("username").toString().trim();
            String password = jsonParam.get("password").toString().trim();
//            使用盐值加密
            String encodePassword = new SimpleHash("md5", password, "notfound945", 5).toString();
//            取得 Subject 对象
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, encodePassword);
            try {
                subject.login(token);
                responseMsg.setRequestCode(200);
                responseMsg.setRequestStatus("login success");
                responseMsg.setResponseRemark("登录成功");
                return responseMsg;
            } catch (UnknownAccountException | IncorrectCredentialsException e) {
                responseMsg.setRequestStatus("login fail");
                responseMsg.setResponseRemark("登录用户名或密码错误");
                responseMsg.setRequestCode(401);
                return responseMsg;
            }
        } catch (Exception e) {
            responseMsg.setRequestCode(401);
            responseMsg.setRequestStatus("No login");
            responseMsg.setResponseRemark("异常的登陆");
            return responseMsg;
        }
    }

    /**
     * 取出请求参数中的 json 数据
     *
     * @param request 请求
     * @return json 字符串
     */
    private JSONObject getJSONParam(HttpServletRequest request) {
        JSONObject jsonParam = null;
        try {
            // 获取输入流
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));

            // 写入数据到 StringBuilder
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = streamReader.readLine()) != null) {
                sb.append(line);
            }
            jsonParam = JSONObject.parseObject(sb.toString());
            // 直接将json信息打印出来
            System.out.println(jsonParam.toJSONString());
        } catch (Exception e) {
            System.out.println("在 getJSONParam 中捕获到异常.");
        }
        return jsonParam;
    }

}
