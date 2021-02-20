package cn.notfound945.demo.controller;

import cn.notfound945.demo.pojo.LoginInfo;
import cn.notfound945.demo.pojo.ResponseMsg;
import cn.notfound945.demo.pojo.User;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @GetMapping(value = "/exception")
    public void getException() throws Exception {
        throw new Exception();
    }

    @ApiOperation(value = "用户登录", notes = "用户登录API接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", defaultValue = "admin"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", defaultValue = "admin"),
            @ApiImplicitParam(name = "validate", value = "验证码", required = true, dataType = "String", defaultValue = "admin")
    })
    @PostMapping(value = "/login")
    @ResponseBody
    public ResponseMsg loginResponseMsg(@RequestBody LoginInfo loginInfo) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        ResponseMsg responseMsg = new ResponseMsg(403, "login", "Login", "");

        try {
            String username = loginInfo.getUsername().trim();
            String password = loginInfo.getPassword().trim();
            String validate = loginInfo.getValidate().trim();

            if (!validate.equals(session.getAttribute("sessionKey").toString().trim())) {
                responseMsg.setRequestCode(403);
                responseMsg.setResponseRemark("验证码不匹配!");
                return responseMsg;
            }

//            使用盐值加密
            String encodePassword = new SimpleHash("md5", password, "notfound945", 5).toString();
            UsernamePasswordToken token = new UsernamePasswordToken(username, encodePassword);
            try {
                subject.login(token);
                User currentUser = (User) subject.getPrincipal();
                System.out.println("当前登录用户: " + currentUser.getUserName());
                responseMsg.setRequestCode(200);
                responseMsg.setRequestStatus("login success");
                responseMsg.setRequestUser(currentUser.getUserName());
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
            responseMsg.setResponseRemark("异常的登陆（请重新获取验证码图片后再尝试）");
            return responseMsg;
        }
    }

    /**
     * 未登录返回
     *
     * @return 响应
     */
    @GetMapping(value = "/noLogin")
    public ResponseMsg noLoginResponseMsg() {
        ResponseMsg responseMsg = new ResponseMsg(401, "No login", "Login", "");
        return responseMsg;
    }

    /**
     * 未得到授权
     *
     * @return 响应
     */
    @GetMapping(value = "/noAuth")
    public ResponseMsg noAuthResponseMsg() {
        ResponseMsg responseMsg = new ResponseMsg(401, "No authorization", "Authorization", "Authorization");
        return responseMsg;
    }

    @GetMapping(value = "/logout")
    public ResponseMsg logoutResponseMsg() {
        ResponseMsg responseMsg = new ResponseMsg(200, "Logout", "Logout", "异常的登出");
        try {
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getPrincipal();
            if (subject.isAuthenticated()) {
                subject.logout();
                System.out.println(user.getUserName() + " 用户登出.");
                responseMsg.setRequestUser(user.getUserName());
                responseMsg.setResponseRemark("登出成功");
            }
        } catch (Exception e) {
            System.out.println("登出异常");
        }
        return responseMsg;
    }
}
