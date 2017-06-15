package com.atwjsw.wx.auth.servlet;

import com.atwjsw.wx.auth.util.AuthUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by wenda on 6/15/2017.
 */
@WebServlet("/WxAuth/wxLogin")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.printf("in /WxAuth/wxLogin");

//        String backUrl = "http://atwjsw.s1.natapp.cc/WxAuth/callBack";
        String backUrl = "http://atwjsw.s1.natapp.cc/callBack";

        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?"
                + "appid=" + AuthUtil.APPID
                + "&redirect_uri=" + URLEncoder.encode(backUrl)
                + "&response_type=code"
                + "&scope=snsapi_userinfo"
                + "&state=STATE"
                + "#wechat_redirect";
//        重定向用户请求到微信授权URL
        resp.sendRedirect(url);
//        super.doGet(req, resp);
    }
}
