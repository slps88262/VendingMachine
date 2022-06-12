package com.training.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.training.model.Account;
import com.training.dao.FrontEndDao;

public class LoginAction extends DispatchAction {

	private FrontEndDao frontEndDao = FrontEndDao.getInstance();
	
	 public ActionForward login(ActionMapping mapping, ActionForm form, 
	            HttpServletRequest request, HttpServletResponse response) throws Exception {
		 	// 登入請求
	    	ActionForward actFwd = null;
	    	String loginMsg = null;
	    	HttpSession session = request.getSession();
	    	String inputID = request.getParameter("identificationNo");
	        String inputPwd = request.getParameter("password");
	        // Step2:依使用者所輸入的帳戶名稱取得 Member
	        Account account = frontEndDao.queryMemberByIdentificationNo(inputID);
	    	if(account != null) {
	    		// Step3:取得帳戶後進行帳號、密碼比對
	    		String id = account.getIdentificationNo();    		
	    		String pwd = account.getPassword();
	    		if(id.equals(inputID) && pwd.equals(inputPwd)) {
	    			System.out.println(account.getCustomerName() + " 先生/小姐您好!");
	    			loginMsg = account.getCustomerName() + " 先生/小姐您好!";
	    			// 將account存入session scope 以供LoginCheckFilter之後使用!
	    			session.setAttribute("account", account);
	    			actFwd = mapping.findForward("success");        			
	    		} else {
	                // Step4:帳號、密碼錯誤,轉向到 "/Login.jsp" 要求重新登入.
	    			System.out.println("帳號或密碼錯誤");
	    			loginMsg = "帳號或密碼錯誤";
	    			actFwd = mapping.findForward("fail");
	    		}
	    	} else {
	            // Step5:無此帳戶名稱,轉向到 "/BankLogin.html" 要求重新登入.
	    		System.out.println("無此帳戶名稱,請重新輸入!");   
	    		loginMsg = "無此帳戶名稱,請重新輸入!";
	    		actFwd = mapping.findForward("fail");
	    	}
	    	request.setAttribute("loginMsg", loginMsg);
	    	return actFwd;
	 }
	
	 public ActionForward logout(ActionMapping mapping, ActionForm form, 
	            HttpServletRequest request, HttpServletResponse response) throws Exception {
	    	// 登出請求
	    	HttpSession session = request.getSession();
			System.out.println("謝謝您的光臨!");
			session.removeAttribute("account");		
			request.setAttribute("loginMsg", "謝謝您的光臨!");
			
	    	return mapping.findForward("fail");
	    }
	 
	
}
