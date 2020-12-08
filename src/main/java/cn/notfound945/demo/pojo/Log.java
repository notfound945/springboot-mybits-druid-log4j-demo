package cn.notfound945.demo.pojo;

public class Log {
    private int id;
    private String userName;
    private String action;
    private String ipAddr;
    private String doTime;
    private String remarks;

    public Log() {
    }

    public Log(int id, String userName, String action, String ipAddr, String doTime, String remarks) {
        this.id = id;
        this.userName = userName;
        this.action = action;
        this.ipAddr = ipAddr;
        this.doTime = doTime;
        this.remarks = remarks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getDoTime() {
        return doTime;
    }

    public void setDoTime(String doTime) {
        this.doTime = doTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", action='" + action + '\'' +
                ", ipAddr='" + ipAddr + '\'' +
                ", doTime='" + doTime + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
