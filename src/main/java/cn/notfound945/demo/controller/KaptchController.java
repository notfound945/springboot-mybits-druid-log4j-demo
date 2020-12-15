package cn.notfound945.demo.controller;

import cn.notfound945.demo.pojo.ValidateCode;
import io.swagger.annotations.ApiModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class KaptchController {

    // 生成验证码图片
    @RequestMapping("/getCaptchaImage")
    @ResponseBody
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {

        try {
            response.setContentType("image/png");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Expire", "0");
            response.setHeader("Pragma", "no-cache");
            ValidateCode validateCode = new ValidateCode();

            // 直接返回图片
            validateCode.getRandomCodeImage(request, response);

        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
