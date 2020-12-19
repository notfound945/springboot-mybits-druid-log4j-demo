package cn.notfound945.demo.pojo;

import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.text.SimpleDateFormat;
import java.util.Date;

@ApiModel("响应消息类")
public class ResponseMsg {
    private int requestCode;
    private String requestStatus;
    private String requestId;
    private String requestTime;
    private String requestUser;
    private String requestAction;
    private String responseRemark;

    public ResponseMsg(int requestCode, String requestStatus, String requestAction, String responseRemark) {
        String generatedString = RandomStringUtils.random(8, true, true);
        Date originDate = new Date();
        SimpleDateFormat time = new SimpleDateFormat("HH:MM:ss");
        String timeStr = time.format(originDate);

        this.requestCode = requestCode;
        this.requestStatus = requestStatus;
        this.requestId = generatedString;
        this.requestUser = "unknown";
        try {
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getPrincipal();
            if (user != null) {
                this.requestUser = user.getUserName();
            }
        } catch (Exception e) {
            System.out.println("无会话内容");
        }
        this.requestAction = requestAction;
        this.responseRemark = responseRemark;
        this.requestTime = timeStr;
    }



    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getRequestUser() {
        return requestUser;
    }

    public void setRequestUser(String requestUser) {
        this.requestUser = requestUser;
    }

    public String getRequestAction() {
        return requestAction;
    }

    public void setRequestAction(String requestAction) {
        this.requestAction = requestAction;
    }

    public String getResponseRemark() {
        return responseRemark;
    }

    public void setResponseRemark(String responseRemark) {
        this.responseRemark = responseRemark;
    }

    @Override
    public String toString() {
        return "{" +
                "requestCode:" + requestCode +
                ", requestStatus:'" + requestStatus + '\'' +
                ", requestId:'" + requestId + '\'' +
                ", requestTime:'" + requestTime + '\'' +
                ", requestUser:'" + requestUser + '\'' +
                ", requestAction:'" + requestAction + '\'' +
                ", responseRemark:'" + responseRemark + '\'' +
                '}';
    }
}
