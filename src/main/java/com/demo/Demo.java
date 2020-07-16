package com.demo;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import sun.net.www.http.HttpClient;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.BasicPermission;
import java.util.ArrayList;
import java.util.List;

public class Demo {
    @Test(dataProvider = "A")
    public void httpclient(String url,String name,String pwd,String type) throws Exception {
        if ("post".equalsIgnoreCase(type)) {
            doPost(url, name, pwd);
        }else {
            doGet(url, name, pwd);
        }
    }
    @DataProvider(name = "A")
    public Object [][] dates(){
        Object [] [] date = {
                {"http://172.20.52.54:9000","scadmin","sailing","post"},
                {"http://172.20.52.54:9000","scadmin","sailing","post"},
                {"http://172.20.52.54:9000","scadmin","","get"}
        };
        return date;
    }
    /**
     * 实现Get接口的调用
     */
    private static void doGet(String url,String name,String pwd) {
        //创建Get对象
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        BasicNameValuePair loginName = new BasicNameValuePair("loginName",name);
        BasicNameValuePair password = new BasicNameValuePair("password",pwd);
        params.add(loginName);
        params.add(password);
        String paramsstring = URLEncodedUtils.format(params,"UTF-8");
        url+=("?"+paramsstring);
        HttpGet get = new HttpGet(url);
        try {
            CloseableHttpClient HttpClient = HttpClients.createDefault();
            CloseableHttpResponse httpResponse = HttpClient.execute(get);
            //获取响应状态码
            int statuscode  =  httpResponse.getStatusLine().getStatusCode();
            //获取响应数据
            String result = EntityUtils.toString( httpResponse.getEntity());
            System.out.println(statuscode);
            System.out.println(result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 实现Post接口的调用
     */
    private static void doPost(String url,String name,String pwd) throws Exception {
        //创建Post对象
        HttpPost post = new HttpPost(url);
        post.addHeader("accept","*/*");
        post.addHeader("connection","Keep-Alive");
        post.addHeader("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        post.addHeader("Cookie","");
        //准备参数
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        BasicNameValuePair loginName = new BasicNameValuePair("loginName",name);
        BasicNameValuePair password = new BasicNameValuePair("password",pwd);
        params.add(loginName);
        params.add(password);
        try {
            //将参数封装到请求体中
            post.setEntity(new UrlEncodedFormEntity(params));
            //准备客户端
            CloseableHttpClient httpClient = HttpClients.createDefault();
            //执行请求
            CloseableHttpResponse httpResponse = httpClient.execute(post);
            SslUtils.ignoreSsl();
            //获取响应状态码
            int statuscode  =  httpResponse.getStatusLine().getStatusCode();
            //获取响应数据
            String result = EntityUtils.toString( httpResponse.getEntity());
            System.out.println(statuscode);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
