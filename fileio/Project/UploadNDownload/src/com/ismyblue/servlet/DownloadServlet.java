package com.ismyblue.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ismyblue.util.FIleDownloadUtil;

public class DownloadServlet extends HttpServlet {

	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
			
		String filePath = request.getParameter("filePath");	
//		String filePath = "\\upload\\高清白2.jpg";
		System.out.println(filePath);		
		FIleDownloadUtil downloadUtil = new FIleDownloadUtil();
		if(downloadUtil.Download(request, response, this.getServletContext().getRealPath("") + filePath)){
			response.setHeader("refresh", "1;url=/upload.jsp");
		}else {
			response.getWriter().print("下载失败");
		}
////		System.out.println(new String(request.getParameter("filename").getBytes("ISO-8859-1"),"UTF-8"));		
//		System.out.println("filedName : " + filename);		
//
//		File file = new File(this.getServletContext().getRealPath("WEB-INF/upload") + File.separator + filename);
//		System.out.println("FileName:" + file.getAbsolutePath());
//		
//		String userAgent = request.getHeader("user-agent");
//		// 针对IE或者以IE为内核的浏览器：		
//        if (userAgent.toLowerCase().contains("msie")) {  
//        	filename = URLEncoder.encode(filename, "UTF-8");  
//        	System.out.println("fsafasdfs");
//        } else {  
//            // 非IE浏览器的处理：  
//        	filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");  
//        }    
//			
//		//告知浏览器http数据包用UTF-8编码填写
//		response.setCharacterEncoding("UTF-8");
//				
//		//告知浏览器下载文件，内容处理方式，附件，文件名=
//		response.setHeader("content-disposition", "attachment;filename=" + filename);
//		//response.setHeader("content-type","image/jpeg");告知浏览器下载的附件的文件类型
//		response.setContentType(this.getServletContext().getMimeType(filename));
//		
//		FileInputStream fis = new FileInputStream(file);
//		ServletOutputStream out = response.getOutputStream();
//		byte[] b = new byte[1024];
//		int len = 0;
//		while ((len = fis.read(b)) != -1) {
//			out.write(b);			
//		}
		
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
