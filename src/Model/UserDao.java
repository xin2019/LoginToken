package Model;

import javax.jws.soap.SOAPBinding;
import java.sql.*;
import java.util.ArrayList;

public class UserDao {
    private ArrayList userlist;

    public UserDao() {
    }

    public ArrayList getUserlist() {
        return userlist;
    }

    public void setUserlist(ArrayList userlist) {
        this.userlist = userlist;
    }

    public ArrayList queryAllUser() {
        userlist = new ArrayList();
        try {
            String path = this.getClass().getClassLoader().getResource("").getPath();
            path = path.substring(1, path.lastIndexOf("classes"));

            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + path + "/User.db";
//            System.out.println(url);
            Connection connection = DriverManager.getConnection(url);
            Statement statement = null;
            statement = connection.createStatement();
            String sql = "select* from user";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                User user = new User();
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));

                userlist.add(user);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return userlist;
    }

    public boolean isUser(String username, String password) {
        boolean flag = false;
        userlist = new UserDao().queryAllUser();
        for (int i = 0; i < userlist.size(); i++) {
            User user = (User) userlist.get(i);
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public boolean isLegalToken(String token){
        boolean flag=false;
        userlist=new UserDao().queryAllUser();
        for (int i=0;i<userlist.size();i++){
            User user=(User)userlist.get(i);
            if(user.getToken().equals(token)){
                flag=true;break;
            }
        }
        return flag;
    }

    public boolean isLegalUser(String username){
        boolean flag=false;
        userlist=new UserDao().queryAllUser();
        for (int i=0;i<userlist.size();i++){
            User user=(User)userlist.get(i);
            if(user.getUsername().equals(username)){
                flag=true;break;
            }
        }
        return flag;
    }
    public String getLoginTime(String username){
        String LoginTime=null;
        try {//可改进
            String path = this.getClass().getClassLoader().getResource("").getPath();
            path = path.substring(1, path.lastIndexOf("classes"));
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + path + "/User.db";
//            System.out.println(url);
            Connection connection = DriverManager.getConnection(url);
            String sql="select * from user where username=?";//这样的结果集包含什么select quitTime from user where username=?
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                LoginTime=resultSet.getString("loginTime");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return LoginTime;
    }

    public String getQuitTime(String username){
        String QuitTime=null;
        try {//可改进
            String path = this.getClass().getClassLoader().getResource("").getPath();
            path = path.substring(1, path.lastIndexOf("classes"));
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + path + "/User.db";
//            System.out.println(url);
            Connection connection = DriverManager.getConnection(url);
            String sql="select * from user where username=?";//这样的结果集包含什么select quitTime from user where username=?
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
               QuitTime=resultSet.getString("quitTime");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return QuitTime;
    }

    public boolean setQuitTime(String quitTime,String username){
        boolean flag=false;

        try {//可改进
            String path = this.getClass().getClassLoader().getResource("").getPath();
            path = path.substring(1, path.lastIndexOf("classes"));
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + path + "/User.db";
//            System.out.println(url);
            Connection connection = DriverManager.getConnection(url);
            String sql="update user set quitTime=? where username=?";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,quitTime);
            preparedStatement.setString(2,username);
            int line=preparedStatement.executeUpdate();
            System.out.println("line:"+line);
            if(line>0){
                flag=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    public boolean setLoginTime(String loginTime,String username){
        boolean flag=false;
        try {//可改进
            String path = this.getClass().getClassLoader().getResource("").getPath();
            path = path.substring(1, path.lastIndexOf("classes"));
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + path + "/User.db";
            Connection connection = DriverManager.getConnection(url);
            String sql="update user set loginTime=? where username=?";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,loginTime);
            preparedStatement.setString(2,username);
            int line=preparedStatement.executeUpdate();
            System.out.println("line:"+line);
            if(line>0){
                flag=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static void main(String[] args) {
        System.out.println(new UserDao().setLoginTime("123time","xin"));
    }
}
