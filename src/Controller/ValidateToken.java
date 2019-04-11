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

@WebServlet(name = "ValidateToken")
public class ValidateToken extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");


        JSONObject outJson = new JSONObject();//输出的token
        String tokenString = request.getParameter("token");//获取前台参数token
        JSONObject jsonObject = new JSONObject(tokenString);//转为json对象的token
        Long beforeTime = Long.parseLong(jsonObject.get("time").toString());//时间转换
        String usernameString = (String) jsonObject.get("username");//token中加密的username
        String username = RSAUtils.decryptBase64(usernameString);//将username解密

        UserDao userDao = new UserDao();
        String loginTime = userDao.getLoginTime(username);//获取该username的上次登陆时间
        String quitTime = userDao.getQuitTime(username);//获取该username的上次退出时间

System.out.println("username:"+username);
System.out.println("quittime:"+quitTime);
System.out.println("beforetime:"+beforeTime);
System.out.println("logintime:"+loginTime);
System.out.println("beforetime-logintime:"+(beforeTime-Long.parseLong(loginTime)));

        if (beforeTime == null || username == null) {
            outJson.put("msg", "传来的token的time或者username为空");
        } else {
            if (quitTime == null || (quitTime != null && beforeTime > Long.parseLong(quitTime))) {
                if (loginTime != null && (beforeTime >= Long.parseLong(loginTime))) {
                    Long currentTime = System.currentTimeMillis();//当前时间,判断token是否过期
                    long timeDifference = currentTime - beforeTime;
                    if (timeDifference > 5 * 60 * 1000) {
                        outJson.put("expire", "token已过期，请重新登陆");
                    } else {
                        try {
                            //解开传来的token的username查看是否与数据库的username一致，若一致则为合法token
//                            boolean isRightToken= userDao.isLegalUser(username);
//                            if(isRightToken){
                            JSONObject jsonObject1 = new JSONObject();
                            if (currentTime - beforeTime > 5 * 1000) {//5秒一更新
                                userDao.setLoginTime(currentTime.toString(), username);
                                jsonObject.put("time", currentTime);
                                jsonObject.put("username", usernameString);
                                outJson.put("token", jsonObject.toString());
                            }
                            outJson.put("href", "/View/Welcome.html");
//                            }
//                            else {
//                                outJson.put("msg","token验证失败");
//                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("token验证失败");
                    outJson.put("msg", "token验证失败");
                }
            } else {
                System.out.println("token验证失败2");
                outJson.put("msg", "token验证失败2");
            }
        }
        PrintWriter out = response.getWriter();
        out.write(outJson.toString());
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
