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
import java.util.Date;

@WebServlet(name = "ExitLogin")
public class ExitLogin extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     String tokenString=request.getParameter("token");
        JSONObject outJson=new JSONObject();
        if(tokenString!=null){
            JSONObject token=new JSONObject(tokenString);

            String usernameString= (String) token.get("username");
            String username= RSAUtils.decryptBase64(usernameString);//获取要更新退出时间的用户名


            Date quitTime=new Date();//获取退出时间
            UserDao userDao=new UserDao();
            userDao.setQuitTime(Long.toString(quitTime.getTime()),username);
            outJson.put("href","/View/Welcome.html");
            outJson.put("exitSuccess","退出成功");
        }
        else {
            outJson.put("exitFailure","退出失败,获取token信息失败");
        }
        PrintWriter out=response.getWriter();
        out.write(outJson.toString());
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
