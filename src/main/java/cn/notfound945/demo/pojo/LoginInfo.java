package cn.notfound945.demo.pojo;

public class LoginInfo {
    private String username;
    private String password;
    private String validate;

    public LoginInfo(String username, String password, String validate) {
        this.username = username;
        this.password = password;
        this.validate = validate;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getValidate() {
        return validate;
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", validate='" + validate + '\'' +
                '}';
    }
}
