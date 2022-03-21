package com.wondersgroup.test1.testspringboot.interceptor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;



public class LoginInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object)  {
		//User user= (User) request.getSession().getAttribute("User");
		Object user= (Object) request.getSession().getAttribute("User");
		if (user == null) {
			//System.out.println("尚未登录，调到登录页面");
			
			String nextURL = request.getRequestURL().toString();
			request.getSession().setAttribute("nextURL", nextURL);//上次访问的URL转到下一个页面
			System.out.println("即将访问的URL： "+nextURL);
    		//response.sendRedirect("/WEB-INF/admin/login/login.jsp");//重定向到登录页
    		try {
				request.getRequestDispatcher("/views/logins/login.jsp").forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}//跳转到登录页
    		
    		return false;
    	}
    	
       	return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView view)
			throws Exception {
		//System.out.println("postHandle");
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception)
			throws Exception {
		//System.out.println("afterCompletion");
	}
}
