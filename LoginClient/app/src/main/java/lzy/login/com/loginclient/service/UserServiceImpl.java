package lzy.login.com.loginclient.service;
import android.content.Entity;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

import lzy.login.com.loginclient.MainActivity;

/**
 * Created by zhaoyang.li on 2015-07-06.
 */
public class UserServiceImpl implements UserService {

    private static final String TAG = "UserServerImpl";

    @Override
    public void userLogin(String loginName, String loginPass) throws Exception {
        Log.d(TAG, loginName);
        Log.d(TAG, loginPass);

        //停顿3秒钟
        Thread.sleep(1500);

        /**
         * 下面这段代码可以模拟程序异常
         * String str = null;
         *if(str.equals("")){
         *{
         */


         /*//判断输入的值是否正确
         if (loginName.equals("ddd")&&loginPass.equals("eee")) {
         }else{
         throw new ServiceRuleException(MainActivity.msg_Login_Faild);
         }*/


//        /**
//         *创建HttpClient对象
//         * 在这里不能直接new HttpClient，因为这是一个借口而不是对象
//         * 这里的DefaultHttpClient是AbstractHttpClient的子类，而
//         * AbstractHttpClient又是HttpClient的子类
//         */
//        HttpClient client  = new DefaultHttpClient();
//
//        /**
//         * url:URL地址-http://localhost:8080/lzy/login
//         * get传参实质：URL?参数名=参数值&参数名=参数值
//         * 这里的参数名，要和servlet里request.getParameter这个参数
//         * 的名字一致。
//         */
//        String url = "http://192.168.1.4:8080/lzy/login?LoginName=" + loginName +
//                "&LoginPass=" + loginPass;
//        HttpGet get = new HttpGet(url);
//
//        //通过httpclient执行get请求，这里在执行以后会返回一个请求
//        HttpResponse response = client.execute(get);

        /**
         * 上面注释的代码是通过HttpGet方法来进行传参的，下面代码是用HttpPost方法来实现
         * 下面这些代码实现的基本思路是
         * NameValuePair-->List<NameValuePair>-->HttpEntity-->HttpPost-->HttpClient
         * 2015/07/28
         */

        HttpParams params = new BasicHttpParams();
        //通过params设置请求时的字符集
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        //设置客户端和服务器连接的超时时间-->ConnectionTimeoutException
        HttpConnectionParams.setConnectionTimeout(params,5000);
        //设置服务器响应时长-->SocketTimeoutException
        HttpConnectionParams.setSoTimeout(params, 3000);

        SchemeRegistry schreg = new SchemeRegistry();
        schreg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(),80));
        schreg.register(new Scheme("https",PlainSocketFactory.getSocketFactory(),433));
        ClientConnectionManager conma = new ThreadSafeClientConnManager(params,schreg);
        HttpClient client = new DefaultHttpClient(conma,params);


        String url = "http://192.168.1.4:8080/lzy/login";
        HttpPost post = new HttpPost(url);
        NameValuePair paramLoginName = new BasicNameValuePair("LoginName",loginName);
        NameValuePair paramLoginPass = new BasicNameValuePair("LoginPass",loginPass);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(paramLoginName);
        parameters.add(paramLoginPass);
        post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
        HttpResponse response = client.execute(post);



        /**
         * 拿到响应信息以后我们需要知道两个事情
         * 1.我的请求是否到达servlet服务器
         * 2.我的登录是否成功
         */


        /**
         * 得到请求状态码，常用的状态码有以下几种
         * 200：OK
         * 404：找不到页面
         * 500：代码执行出错
         * 406：参数异常
         */
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK){
            //如果登录出错，直接抛出异常
            throw new ServiceRuleException(MainActivity.msg_statusCode_Error);
        }

        //apache会把所有的返回值信息以string的形式封装到entity里面，我们去entity里取值
        String result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
        if(result.equals("success\r\n")){
            //登陆成功
        }else{
            //登录用户名和密码错误
            throw new ServiceRuleException(MainActivity.msg_Login_Faild);
        }
    }
}
