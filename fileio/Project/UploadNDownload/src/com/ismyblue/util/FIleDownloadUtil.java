package com.ismyblue.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FIleDownloadUtil {

	/**
	 * 从服务器下载一个文件到浏览器
	 * @param request 请求包
	 * @param response 响应包
	 * @param filePath 要下载的文件的真实路径，绝对路径
	 */
	public boolean Download(HttpServletRequest request, HttpServletResponse response, String filePath){
		//获得文件对象
		File file = new File(filePath);		
		if(!file.exists()){
			return false;
		}
		//获得文件名
		String fileName = file.getName();
		
		String userAgent = request.getHeader("user-agent");
		// 针对IE或者以IE为内核的浏览器：		
        if (userAgent.toLowerCase().contains("msie")) {  
        	try {
				fileName = URLEncoder.encode(fileName, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}  
        	System.out.println("fsafasdfs");
        } else {  
            // 非IE浏览器的处理：  
        	try {
				fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}  
        }    
			
		//告知浏览器http数据包用UTF-8编码填写
		response.setCharacterEncoding("UTF-8");				
		//告知浏览器下载文件，内容处理方式，附件，文件名=
		response.setHeader("content-disposition", "attachment;filename=" + fileName);
		//response.setHeader("content-type","image/jpeg");告知浏览器下载的附件的文件类型
		response.setContentType(request.getServletContext().getMimeType(fileName));
		
		try {
			BufferedInputStream bfis = new BufferedInputStream(new FileInputStream(file));
			ServletOutputStream out = response.getOutputStream();
			byte[] b = new byte[1024];
			while ((bfis.read(b)) != -1) {
				out.write(b);			
			}
			bfis.close();
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return false;
	}
	
}
