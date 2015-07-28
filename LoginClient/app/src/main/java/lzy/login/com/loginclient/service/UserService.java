package lzy.login.com.loginclient.service;

/**
 * Created by zhaoyang.li on 2015-07-06.
 * 这个类只要是用来处理异常使用
 */
public interface UserService {
    public void userLogin(String loginName,String loginPass) throws Exception;
}
