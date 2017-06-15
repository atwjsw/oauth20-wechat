package com.atwjsw.wx.auth.util;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by wenda on 6/15/2017.
 */
public class AuthUtil {

    public static final String APPID = "wx6d42155f34fa5ba1";
    public static final String APPSECRET = "412770c757ceb5443bf76514a3b053b4";

    /**
     * send http request and convert resposne into json object
     * @param url
     * @return
     * @throws IOException
     */
    public static JSONObject doGetJson(String url) throws IOException {
        JSONObject jsonObject = null;
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if(entity !=null) {
            String result = EntityUtils.toString(entity, "UTF-8");
            System.out.println(result);
            jsonObject = JSONObject.fromObject(result);
        }
        return jsonObject;
    }
}
