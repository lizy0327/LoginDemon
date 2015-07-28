package lzy.login.com.loginclient.service;

//import org.apache.commons.logging.Log;


import android.content.Entity;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

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
        Thread.sleep(3000);

        /**
         *
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

        /**
         *创建HttpClient对象
         * 在这里不能直接new HttpClient，因为这是一个借口而不是对象
         * 这里的DefaultHttpClient是AbstractHttpClient的子类，而
         * AbstractHttpClient又是HttpClient的子类
         */
        HttpClient client  = new DefaultHttpClient();

        /**
         * url:URL地址-http://localhost:8080/lzy/login
         *
         * get传参实质：URL?参数名=参数值&参数名=参数值
         * 这里的参数名，要和servlet里request.getParameter这个参数
         * 的名字一致。
         *
         */
        String url = "http://172.31.1.13:8080/lzy/login?LoginName=" + loginName +
                "&LoginPass=" + loginPass;
        HttpGet get = new HttpGet(url);

        //通过httpclient执行get请求，这里在执行以后会返回一个请求
        HttpResponse response = client.execute(get);

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
