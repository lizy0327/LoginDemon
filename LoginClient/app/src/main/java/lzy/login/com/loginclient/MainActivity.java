package lzy.login.com.loginclient;

import android.app.Activity;
//import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.os.Handler;
//import java.util.logging.Handler;
//import java.util.logging.LogRecord;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;

import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;
import java.util.WeakHashMap;

import lzy.login.com.loginclient.service.ServiceRuleException;
import lzy.login.com.loginclient.service.UserService;
import lzy.login.com.loginclient.service.UserServiceImpl;

public class MainActivity extends Activity {

    private EditText txtLogingName;
    private EditText txtLoginPass;
    private Button btnLoging;
    private Button btnReset;

    //声明业务对象
    private UserService userService = new UserServiceImpl();
    private static final int msg_Login_SUCCESS = 1;
    private static final String msg_Login_Error = "登录失败";
    private static final String msg_Login_Success = "登录成功";
    public static final String msg_Login_Faild = "用户名或者密码错误";
    public static final String msg_statusCode_Error = "服务器请求出错";
    public static final String msg_request_TimeOut = "请求服务超时";
    public static final String msg_response_TimeOut = "服务器响应超时";
    private static ProgressDialog dialog;

    //初始化参数的方法
    private void init() {
        txtLogingName = (EditText) this.findViewById(R.id.logingName);
        txtLoginPass = (EditText) this.findViewById(R.id.loginPass);
        btnLoging = (Button) findViewById(R.id.btn_loging);
        btnReset = (Button) this.findViewById(R.id.btn_reset);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //调用初始化参数
        this.init();

        //点击登录按钮
        this.btnLoging.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {
                /**
                 * String是引用变量，加上final的意思是这个loginName的引用变量只能用于这个方法的引用变量
                 * 否则的话会在下面副线程里面的try报错
                 */
                final String loginName = txtLogingName.getText().toString();
                final String loginPass = txtLoginPass.getText().toString();

                Toast.makeText(view.getContext(), "用户名：" + loginName, Toast.LENGTH_LONG).show();
                Toast.makeText(view.getContext(), "密码：" + loginPass, Toast.LENGTH_LONG).show();

                System.out.print("dddddddddddddddddddddddd" + loginName);
                Log.i("ln", loginName);
                /**
                 * 登陆值验证
                 */

                /**
                 * loadding
                 */
                if (dialog == null) {
                    dialog = new ProgressDialog(MainActivity.this);
                }
                dialog.setTitle("请等待");
                dialog.setMessage("登录中...");
                dialog.setCancelable(false);
                dialog.show();

                /**
                 * 副线程
                 */
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            /**
                             *要使用logiName和loginPass这两个引用变量，要在上面的onCreate方法里给
                             *loginName和loginPass加上final声明
                             */
                            userService.userLogin(loginName, loginPass);
                            handler.sendEmptyMessage(1);
                        //服务器响应超时异常
                        }catch (SocketTimeoutException e) {
                            e.printStackTrace();
                            Message msg = new Message();
                            Bundle date = new Bundle();
                            date.putSerializable("ErrorMsg", msg_response_TimeOut);
                            msg.setData(date);
                            handler.sendMessage(msg);
                        //客户端连接服务器超时异常
                        }catch (ConnectTimeoutException e) {
                            e.printStackTrace();
                            Message msg = new Message();
                            Bundle date = new Bundle();
                            date.putSerializable("ErrorMsg", msg_request_TimeOut);
                            msg.setData(date);
                            handler.sendMessage(msg);
                        }catch (ServiceRuleException e) {
                            e.printStackTrace();
                            Message msg = new Message();
                            Bundle date = new Bundle();
                            date.putSerializable("ErrorMsg", e.getMessage());
                            msg.setData(date);
                            handler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Message msg = new Message();
                            Bundle date = new Bundle();
                            date.putSerializable("ErrorMsg", msg_Login_Error);
                            msg.setData(date);
                            handler.sendMessage(msg);
                        }
                    }
                });
                thread.start();
            }

        });

        //点击重置按钮
        this.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtLogingName.setText("");
                txtLoginPass.setText("");
            }
        });
    }

    //调用UI子线程的方法
    private void showTip(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    /**
     * 调用Handler方法来实现副线程和主线程之间的数据传递
     */
    private static class IHandler extends Handler {
        private final WeakReference<Activity> mActivity;
        public IHandler(MainActivity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (dialog != null) {
                dialog.dismiss();
            }
            int flag = msg.what;
            switch (flag) {
                case 0:
                    String errMsg = (String) msg.getData().getSerializable("ErrorMsg");
                    ((MainActivity) mActivity.get()).showTip(errMsg);
                    break;
                case (msg_Login_SUCCESS):
                    ((MainActivity) mActivity.get()).showTip(msg_Login_Success);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 自定义Handler对象，后面加上this后，说明这个handler这个对象就拥有了
     * MainActivity这个对象了
     */
    private IHandler handler = new IHandler(this);

    /*public void handleMessage(Message msg) {
        int flag = msg.what;
        switch (flag) {
            case 0:
                String errMsg = (String) msg.getData().getSerializable("ErrorMsg");
                ((MainActivity) mActivity.get()).showTip(errMsg);
                break;
            case (msg_Login_SUCCESS):
                ((MainActivity) mActivity.get()).showTip(msg_Login_Success);
                break;
            default:

                break;

        }
    }*/



    /*private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };*/


    //    @Override
    //    public boolean onCreateOptionsMenu(Menu menu) {
    //        // Inflate the menu; this adds items to the action bar if it is present.
    //        getMenuInflater().inflate(R.menu.menu_main, menu);
    //        return true;
    //    }

    //    @Override
    //    public boolean onOptionsItemSelected(MenuItem item) {
    //        // Handle action bar item clicks here. The action bar will
    //        // automatically handle clicks on the Home/Up button, so long
    //        // as you specify a parent activity in AndroidManifest.xml.
    //        int id = item.getItemId();
    //
    //        //noinspection SimplifiableIfStatement
    //        if (id == R.id.action_settings) {
    //            return true;
    //        }
    //
    //        return super.onOptionsItemSelected(item);
    //    }
}
