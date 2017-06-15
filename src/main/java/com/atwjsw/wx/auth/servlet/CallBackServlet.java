package com.atwjsw.wx.auth.servlet;

import com.atwjsw.wx.auth.util.AuthUtil;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;


/**
 * Created by wenda on 6/15/2017.
 */
@WebServlet("/callBack")
public class CallBackServlet extends HttpServlet {

    private String dbUrl = "jdbc:mysql://192.168.246.166:3306/auth";
    private String driverName = "com.mysql.jdbc.Driver";
    private String dbUserName = "root";
    private String dbPassword = "root";
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1. 获取微信回调请求中的code
        System.out.printf("in WxAuth/callBack");
        String code = req.getParameter("code");
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?"
                    + "appid=" + AuthUtil.APPID
                    + "&secret=" + AuthUtil.APPSECRET
                    + "&code=" + code
                    + "&grant_type=authorization_code";
        //2. 向微信发出请求，带上APPSCECRET和code，获取openid和access_toekn
        JSONObject jsonObject = AuthUtil.doGetJson(url);
        String openid = jsonObject.getString("openid");
        String token = jsonObject.getString("access_token");
        //4. 获取用户信息
        String infoUrl = "https://api.weixin.qq.com/sns/userinfo?"
                        + "access_token=" + token
                        + "&openid=" + openid
                        + "&lang=zh_CN";
        JSONObject userInfo = AuthUtil.doGetJson(infoUrl);
        System.out.println(userInfo);



        //1. 使用微信用户信息直接登录，无需注册和绑定
//        req.setAttribute("info", userInfo);
//        req.getRequestDispatcher("/index1.jsp").forward(req, resp);

        //2. 将微信与当前的系统账号绑定
        try {
            String nickName = getNickName(openid);
            if (!"".equals(nickName)){
                //已绑定。直接跳转登录成功页面
                req.setAttribute("nickname", nickName);
                req.getRequestDispatcher("/index2.jsp").forward(req, resp);
            } else {
//                未绑定。 跳转到绑定页面，要求用户输入账户密码
//                更新数据库openid
                req.setAttribute("openid", openid);
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String account =req.getParameter("account");
        String password =req.getParameter("password");
        String openid =req.getParameter("openid");
        try {
            int temp = updateUser(openid, account, password);
            if(temp>0) {
                System.out.println("账号绑定成功");
            } else {
                System.out.println("账号绑定失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String getNickName(String openid) throws SQLException {
        String nickName = "";
        conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
        String sql = "select nickname from user where openid=?";
        ps = conn.prepareStatement(sql);
        ps.setString(1, openid);
        rs = ps.executeQuery();
        while (rs.next()) {
            nickName = rs.getString("NICKNAME");
        }
        rs.close();
        ps.close();
        conn.close();
        return nickName;
    }

    public int updateUser(String openid, String account, String password) throws SQLException {

        conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
        String sql = "update user set OPENID=? where ACCOUNT=? and PASSWORD=?";
        ps = conn.prepareStatement(sql);
        ps.setString(1, openid);
        ps.setString(2, account);
        ps.setString(3, password);
        int temp = ps.executeUpdate();
        rs.close();
        ps.close();
        conn.close();
        return temp;
    }
}
