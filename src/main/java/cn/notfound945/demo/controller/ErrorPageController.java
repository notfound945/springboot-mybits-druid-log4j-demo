package cn.notfound945.demo.controller;

import cn.notfound945.demo.pojo.ResponseMsg;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@RestController
public class ErrorPageController implements org.springframework.boot.web.servlet.error.ErrorController {

    @GetMapping(value = "/400")
    public ResponseMsg badRequest() {
        return new ResponseMsg(400,"400: Bad Request", "", "服务器不能理解请求");
    }

    @GetMapping(value = "/401")
    public ResponseMsg noAuthor() {
        return new ResponseMsg(401,"401: Unauthorized Error", "", "请求未经授权");
    }

    @GetMapping(value = "/403")
    public ResponseMsg forbidden() {
        return new ResponseMsg(403,"403: Forbidden", "", "服务器拒绝提供服务");
    }

    @GetMapping(value = "/404")
    public ResponseMsg notFound() {
        return new ResponseMsg(404,"404: Not Found", "", "请求资源不存在");
    }

    @GetMapping(value = "/500")
    public ResponseMsg serverError() {
        return new ResponseMsg(500,"500: Internal Server Error", "", "服务器内部出现问题");
    }

    @GetMapping(value = "/503")
    public ResponseMsg serverUnavailable() {
        return new ResponseMsg(503,"503: Server Unavailable", "", "当前服务器无法完成你的请求");
    }

    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {

        String scriptF = "<script type=\"text/javascript\"> window.location.replace('";
        String scriptL = "')</script>";

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return scriptF + "/404" + scriptL;
            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                return scriptF + "/400" + scriptL;
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return scriptF + "/403" + scriptL;
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                return scriptF + "/401" + scriptL;
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return scriptF + "/500" + scriptL;
            } else if (statusCode == HttpStatus.SERVICE_UNAVAILABLE.value()) {
                return scriptF + "/503" + scriptL;
            }
            // 这里后期可以扩展其他错误页面
        }
        return scriptF + "/403" + scriptL;
    }

    @Override
    public String getErrorPath() {
        return null;
    }

}
