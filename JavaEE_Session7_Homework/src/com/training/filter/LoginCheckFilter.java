package com.training.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.training.model.Account;

public class LoginCheckFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException { }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpSession session = httpRequest.getSession();
		Account account = (Account)session.getAttribute("account");
		
		if(account != null){
            chain.doFilter(request,response);
        } else {
        	String requestURI = httpRequest.getRequestURI();
        	String action = request.getParameter("action");
            if(requestURI.endsWith("LoginAction.do") && "login".equals(action)) {
            	chain.doFilter(request,response);	
            } else {
                // 不是登入驗證的請求或是SessionTimeOut,轉向到 "/login.html" 要求重新登入.            	
                RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
                dispatcher.forward(request,response);
            }            
        }
	}

	@Override
	public void destroy() { }

}
