package com.ismyblue.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ismyblue.util.JspToHtmlUtil;

public class GetMyHtml extends HttpServlet {


	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = request.getParameter("user");
		request.setAttribute("user", user);
		JspToHtmlUtil htmlUtil = new JspToHtmlUtil();
		if(user != null && htmlUtil.convert("/index.jsp", "/html/"+user+".html", request, response)){
			response.sendRedirect(request.getContextPath()+"/html/"+user+".html");			
		}else {
			response.getWriter().println("<h1>failed . pelase get a user parameter <br> eg. http://localhost:8080/JspToHtml/getMyHtml?user=JoyLee<h1>");	
			
		}		
		
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	
	}

}
