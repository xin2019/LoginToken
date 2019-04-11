package Controller;

import Model.RSAUtils;
import Model.UserDao;
import org.json.JSONObject;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;

@WebServlet(name = "CreateToken")
public class CreateToken extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null) {
            PrintWriter out = response.getWriter();
            JSONObject jsonObject = new JSONObject();
            jsonObject.append("msg", "Pleace check out username or password is not null");
            out.write(jsonObject.toString());
            out.close();
        } else {
            try {
                String decyptUsername = RSAUtils.decryptBase64(username);
                String decryptPassword = RSAUtils.decryptBase64(password);
                UserDao userDao = new UserDao();
                boolean isValidateUser = userDao.isUser(decyptUsername, decryptPassword);//验证是合法用户
                if (isValidateUser) {
                    //生成token
                    PrintWriter out = response.getWriter();
                    //更新username的logintime信息
                    Long time=System.currentTimeMillis();
                    System.out.println("time1:"+time);
                    boolean flag = userDao.setLoginTime(Long.toString(time), decyptUsername);
                    JSONObject jsonObject = new JSONObject();
                    if (flag) {
                        JSONObject tokenObject = new JSONObject();
                        System.out.println("time2:"+time);
                        tokenObject.put("time",time.toString() );
                        tokenObject.put("username", username);
                        jsonObject.put("token", tokenObject.toString());
                        jsonObject.put("href", "/View/Welcome.html");
                    } else {
                        jsonObject.put("msg", "读写数据库失败");
                    }
                    out.write(jsonObject.toString());
                    out.close();

                } else {
                    PrintWriter out = response.getWriter();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.append("msg", "invalidate user account!");
                    out.write(jsonObject.toString());
                    out.close();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
