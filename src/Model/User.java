package Model;

public class User {
    private String username;
    private String password;
    private String token;
    private String loginTime;
    private String quitTime;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getQuitTime() {
        return quitTime;
    }

    public void setQuitTime(String quitTime) {
        this.quitTime = quitTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
