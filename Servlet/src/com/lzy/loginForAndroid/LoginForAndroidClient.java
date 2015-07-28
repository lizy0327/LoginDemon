package com.lzy.loginForAndroid;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoginForAndroidClient
 */
@WebServlet("/login")
public class LoginForAndroidClient extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("---doGet---");
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("---doPost---");
		request.setCharacterEncoding("UTF-8");

		String loginName = request.getParameter("LoginName");
		String loginPass = request.getParameter("LoginPass");
		System.out.println("userName: "+loginName);
		System.out.println("pass: "+loginPass);

		response.setCharacterEncoding("UTF-8");

		/**
		 * 设置相应的文件类型，其实默认的文件类型就是text/html，
		 * 这里 主要是应用后面的Charset=UTF-8这个参数，防止乱码
		 */
		response.setContentType("text/html;charset=UTF-8");

		PrintWriter out = null;
		try {
			//out是response.getWriter方法返回客户端的值
			out = response.getWriter();
			//判断登录业务
			if (loginName.equals("lzy") && loginPass.equals("123")) {
				// 登录成功
				out.println("success");
				System.out.println("success");
			} else {
				// 登录失败
				out.println("faild");
				System.out.println("faild");
			}
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
