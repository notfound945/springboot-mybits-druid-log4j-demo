package cn.notfound945.demo.pojo;

public class User {
    private int id;
    private String username;
    private String password;
    private String avatar;
    private String realName;
    private int authority;
    private int groups;
    private String registrationDate;

    public User() {
    }

    public User(int id, String username, String password, String avatar, String realName, int authority, int groups, String registrationDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.realName = realName;
        this.authority = authority;
        this.groups = groups;
        this.registrationDate = registrationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public int getGroups() {
        return groups;
    }

    public void setGroups(int groups) {
        this.groups = groups;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", realName='" + realName + '\'' +
                ", authority=" + authority +
                ", groups=" + groups +
                ", registrationDate='" + registrationDate + '\'' +
                '}';
    }
}
