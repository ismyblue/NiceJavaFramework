package com.ismyblue.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;

import com.ismyblue.util.FileUploadUtil;


public class UploadServlet extends HttpServlet {


	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
	
		
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Map<String, String[]> fieldMap = new HashMap<String, String[]>();
		try {
			FileUploadUtil fileUploadUtil = new FileUploadUtil();
			fileUploadUtil.parseRequset(request, fieldMap, "/WEB-INF/upload");
		} catch (FileUploadException e1) {
			System.out.println("文件上传失败");
			e1.printStackTrace();
		}

		for(Entry<String, String[]> e : fieldMap.entrySet()){
			System.out.println(e.getKey() + " " + e.getValue()[0]);
		}
	}
	
	
}
