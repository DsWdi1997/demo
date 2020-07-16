package com.demo;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Connection {

    @Test
    public void httptool() {
        String url = "http://172.20.52.54:9000";
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        BasicNameValuePair start = new BasicNameValuePair("id","PG20200701160940");
        params.add(start);
        post(url,params);
    }
    /**
     * POST请求
     *
     * @param requestUrl 请求地址
     * @param param 请求数据
     * @return
     */
    public static String post(String requestUrl, List<BasicNameValuePair> param){
        HttpURLConnection connection = null ;
        InputStream is = null ;
        OutputStream os = null ;
        BufferedReader br = null ;
        String result = null ;
        try {
            //创建远程URL连接对象
            URL url = new URL(requestUrl);
            SslUtils.ignoreSsl();
            //通过远程URL对象打开一个连接，强制转换为HttpUrlConnection类型
            connection = (HttpURLConnection) url.openConnection();
            //设置连接方式:POST
            connection.setRequestMethod("POST");
            //设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            //设置读取远程返回的数据时间:60000毫秒
            connection.setReadTimeout(60000);
            //设置是否想httpUrlConnection输出，设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
            //默认值为:false，当想远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            //默认值为:true，当前向远程服务器读数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
            //设置通用的请求属性
            connection.setRequestProperty("accept","*/*");
            connection.setRequestProperty("connection","Keep-Alive");
            connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Cookie","JSESSIONID=0AAC777401F8AC646782EDC50CD15EC6; userName=%E6%93%8D%E4%BD%9C%E7%AE%A1%E7%90%86%E5%91%98; loginName=secadmin; tokenId=14393809-37ff-4559-86bf-13d2929b4b43; logged=true; id=BDDAEA71A72C44FCA22934BFD0DA4F41");
            //设置传入参数的格式:请求参数应该是name1=Value&name2=Value2的形式
            //connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            //通过连接对象获取一个输出流
            os = connection.getOutputStream();
            //通过输出流对象将参数写出去/传输出去，它是通过字节数组写出的
            // 若使用os.print(param);则需要释放缓存：os.flush();即使用字符流输出需要释放缓存，字节流则不需要
            if (param !=null&&param.toString().length()>0){
                os.write(param.toString().getBytes());
                System.out.println("请求体："+os);
            }
            //请求成功:返回码为200
            if (connection.getResponseCode()==200){
                //通过连接对象获取一个输入流，向远程读取
                is = connection.getInputStream();
                //封装输入流is,并指定字符集
                br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                //存放数据
                StringBuffer sbf =new StringBuffer();
                String line = null ;
                while ((line = br.readLine()) != null){
                    sbf.append(line);
                    sbf.append("\r\n");
                    System.out.println(sbf);
                }
                result = sbf.toString();
                //System.out.println(result);
            }else {
                System.out.println("请求失败");
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭资源
            try {
                if (null != br) {
                    br.close();
                }
                if (null != is) {
                    is.close();
                }
                if (null != os) {
                    os.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            //关闭远程连接
            // 断开连接，最好写上，disconnect是在底层tcp socket链接空闲时才切断。如果正在被其他线程使用就不切断。
            // 固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些
            connection.disconnect();
            System.out.println("--------->>> POST request end <<<----------");
        }
        return result;
    }


}
