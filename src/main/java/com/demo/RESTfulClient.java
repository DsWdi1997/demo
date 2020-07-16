package com.demo;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
//import org.json.JSONException;
//import org.json.JSONObject;
public class RESTfulClient {
    public static String SERVER_URL = "https://172.20.52.143";
    public static String REQUEST_PATH = "";
    public static String REQUEST_URL = SERVER_URL + REQUEST_PATH;

    public static String GET_CONTENT_TYPE = "application/json";
    public static String GET_ACCEPT = "application/json";
    public static String GET_CHARSET = "UTF-8";

    public static String PUT_CONTENT_TYPE = "text/plain";
    public static String PUT_ACCEPT = "application/json";
    public static String PUT_CHARSET = "UTF-8";

    public static String POST_CONTENT_TYPE = "application/json";
    public static String POST_ACCEPT = "application/json";
    public static String POST_CHARSET = "UTF-8";
    public static String POST_COOKIE = "JSESSIONID=0AAC777401F8AC646782EDC50CD15EC6; userName=%E6%93%8D%E4%BD%9C%E7%AE%A1%E7%90%86%E5%91%98; loginName=secadmin; tokenId=14393809-37ff-4559-86bf-13d2929b4b43; logged=true; id=BDDAEA71A72C44FCA22934BFD0DA4F41";
    public static String executeGet(String path)
            throws HttpHostConnectException, IOException {
        String fullPath = SERVER_URL + path;
        HttpGet httpQuery = new HttpGet(fullPath);
        httpQuery.addHeader("Accept", "text/plain;charset=UTF-8");// );
        httpQuery.addHeader("Accept", "application/json");
        return doRequest(httpQuery);
    }

    public static String executePost(String path, String body) throws IOException {
        HttpPost httpQuery = new HttpPost(SERVER_URL + path);
        System.out.println("request Path : " + SERVER_URL + path);
        httpQuery.addHeader("Content-Type", POST_CONTENT_TYPE);
        httpQuery.addHeader("Accept", POST_ACCEPT);
        httpQuery.addHeader("charset", POST_CHARSET);
        if (null != body) {
            StringEntity se = new StringEntity(body);
            httpQuery.setEntity(se);
        }
        return doRequest(httpQuery);
    }
    public static String executePost1(String path, List<BasicNameValuePair> params) throws Exception {
        HttpPost httpQuery = new HttpPost(SERVER_URL + path);
        System.out.println("request Path : " + SERVER_URL + path);
        SslUtils.ignoreSsl();
        httpQuery.addHeader("Content-Type", POST_CONTENT_TYPE);
        httpQuery.addHeader("Accept", POST_ACCEPT);
        httpQuery.addHeader("charset", POST_CHARSET);
        httpQuery.addHeader("Cookie",POST_COOKIE);
        if (null != params) {
            httpQuery.setEntity(new UrlEncodedFormEntity(params));
        }
        return doRequest(httpQuery);
    }
    public static String executePut(String path, String body)
            throws IOException {
        HttpPut httpQuery = new HttpPut(SERVER_URL + path);
        httpQuery.addHeader("Content-Type", PUT_CONTENT_TYPE);
        httpQuery.addHeader("charset", PUT_CHARSET);
        if (null != body) {
            StringEntity se = new StringEntity(body, PUT_CHARSET);
            httpQuery.setEntity(se);
        }
        return doRequest(httpQuery);
    }

    public static String executePutTwo(String path, String body)
            throws IOException {
        HttpPut httpQuery = new HttpPut(SERVER_URL + path);
        httpQuery.addHeader("Content-Type", PUT_ACCEPT);
        httpQuery.addHeader("charset", PUT_CHARSET);
        if (null != body) {
            StringEntity se = new StringEntity(body, PUT_CHARSET);
            httpQuery.setEntity(se);
        }
        return doRequest(httpQuery);
    }


    private static String doRequest(HttpRequestBase httpQuery)
            throws ParseException, IOException, HttpHostConnectException {
        String content = null;
        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpResponse httpResponse = httpClient.execute(httpQuery);
        HttpEntity entity = null;
        entity = httpResponse.getEntity();

        if (null != entity) {
            content = EntityUtils.toString(entity, HTTP.UTF_8);
        }

        return content;
    }
    public static String executePostNew(String type, File file, String url, String username) throws Exception {
        //new一個MultipartEntityBuilder封裝類的對象，并設定模式及編碼類型
        MultipartEntityBuilder mulEnBuilder = MultipartEntityBuilder.create();
        mulEnBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        mulEnBuilder.setCharset(Charset.forName("UTF-8"));
        //封裝文本內容

        mulEnBuilder.addTextBody("username", username, ContentType.create(type, Charset.forName("UTF-8")));
        //封裝二進制文件
        mulEnBuilder.addBinaryBody("file", file, ContentType.create("application/octet-stream", Charset.forName("UTF-8")), file.getPath());
        //new一個HttpEntity封裝類的對象，并將上面的MultipartEntityBuilder封裝類的對象封裝進該HttpEntity對象
        HttpEntity httpEntity = mulEnBuilder.build();
        //new一個指定URL的HttpPost對象，并將上面的MultipartEntityBuilder封裝類封裝進該HttpPost對象
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(httpEntity);
        return doRequest(httpPost);
    }

    public static String executePostNew1(String type, File file, String url, String username,String user) throws Exception {
        //new一個MultipartEntityBuilder封裝類的對象，并設定模式及編碼類型
        MultipartEntityBuilder mulEnBuilder = MultipartEntityBuilder.create();
        mulEnBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        mulEnBuilder.setCharset(Charset.forName("UTF-8"));
        //封裝文本內容
        mulEnBuilder.addTextBody("username", username, ContentType.create(type, Charset.forName("UTF-8")));
        mulEnBuilder.addTextBody("admin", user, ContentType.create(type, Charset.forName("UTF-8")));

        //封裝二進制文件
        mulEnBuilder.addBinaryBody("file", file, ContentType.create("application/octet-stream", Charset.forName("UTF-8")), file.getPath());
        //new一個HttpEntity封裝類的對象，并將上面的MultipartEntityBuilder封裝類的對象封裝進該HttpEntity對象
        HttpEntity httpEntity = mulEnBuilder.build();
        //new一個指定URL的HttpPost對象，并將上面的MultipartEntityBuilder封裝類封裝進該HttpPost對象
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(httpEntity);
        return doRequest(httpPost);
    }

    public static String executePostTwo(String url, String username, String imgPath) throws Exception {

        HttpPost httpPost = new HttpPost(url);
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        //传递2个参数  name和password
        nvps.add(new BasicNameValuePair("username", username));
        nvps.add(new BasicNameValuePair("imgPath", imgPath));
        HttpEntity reqEntity = new UrlEncodedFormEntity(nvps,Consts.UTF_8);
        httpPost.setEntity(reqEntity);
        return doRequest(httpPost);
    }

//  public static String executePostTwo(String type, String url, String username, String imgPath) throws Exception {
//      //new一個MultipartEntityBuilder封裝類的對象，并設定模式及編碼類型
//      MultipartEntityBuilder mulEnBuilder = MultipartEntityBuilder.create();
//      mulEnBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//      mulEnBuilder.setCharset(Charset.forName("UTF-8"));
//      //封裝文本內容
//
//      mulEnBuilder.addTextBody("username", username, ContentType.create(type, Charset.forName("UTF-8")));
//      mulEnBuilder.addTextBody("imgPath", imgPath, ContentType.create(type, Charset.forName("UTF-8")));
//
//      //new一個HttpEntity封裝類的對象，并將上面的MultipartEntityBuilder封裝類的對象封裝進該HttpEntity對象
//      HttpEntity httpEntity = mulEnBuilder.build();
//      //new一個指定URL的HttpPost對象，并將上面的MultipartEntityBuilder封裝類封裝進該HttpPost對象
//      HttpPost httpPost = new HttpPost(url);
//      httpPost.setEntity(httpEntity);
//      return doRequest(httpPost);
//  }
    public static void main(String[] args) throws Exception {

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        BasicNameValuePair start = new BasicNameValuePair("id","PG20200701160940");
        params.add(start);
        executePost1("/spg_ws/program/gateway/start",params);
        SslUtils.ignoreSsl();
}

}
