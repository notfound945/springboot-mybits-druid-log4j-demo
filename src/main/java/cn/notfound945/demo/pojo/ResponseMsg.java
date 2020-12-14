package cn.notfound945.demo.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ResponseMsg {
    private int requestCode;
    private String requestStatus;
    private String requestId;
    private String requestTime;
    private String requestUser;
    private String requestAction;
    private String responseRemark;

    public ResponseMsg(int requestCode, String requestStatus, String requestUser, String requestAction, String responseRemark) {
        Date originDate = new Date();
        SimpleDateFormat time = new SimpleDateFormat("HH:MM:ss");
        String timeStr = time.format(originDate);

        this.requestCode = requestCode;
        this.requestStatus = requestStatus;
        this.requestUser = requestUser;
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

}
