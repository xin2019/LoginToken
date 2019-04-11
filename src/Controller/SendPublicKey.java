package Controller;


import Model.RSAUtils;
import org.json.JSONObject;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
//import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPublicKey;


@WebServlet(name = "SendPublicKey")
public class SendPublicKey extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        try {
            String rsaPublicKey=RSAUtils.generateBase64PublicKey();
            PrintWriter out = response.getWriter();
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("PK",rsaPublicKey);
            out.append(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        System.out.println("get");
    }
}
