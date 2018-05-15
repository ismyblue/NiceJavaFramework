package com.ismyblue.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class JspToHtmlUtil {

	public JspToHtmlUtil() {

	}

	/**
	 * 使jsp文件输出的内容成为一个新的html文件
	 * 
	 * @param jspUrl
	 *            webapp下jsp的绝对路径
	 * @param htmlUrl
	 *            webapp下html新文件的绝对路径
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean convert(String jspUrl, String htmlUrl, HttpServletRequest request, HttpServletResponse response) {

		File htmlFile = new File(request.getServletContext().getRealPath("") + htmlUrl);
		String htmlFileAbPath = htmlFile.getAbsolutePath();
		File pareDir = new File(htmlFileAbPath.substring(0, htmlFileAbPath.lastIndexOf(File.separator)));
	
		// 创建父级目录
		if (!pareDir.exists()) {
			pareDir.mkdirs();
		}
		// 创建html文件
		if (!htmlFile.exists()) {
			try {
				htmlFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		final FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(htmlFile);
			final ServletOutputStream stream = new ServletOutputStream() {
				@Override
				public void write(int b) throws IOException {
					fileOutputStream.write(b);
				}

				@Override
				public void setWriteListener(WriteListener listener) {
				}

				@Override
				public boolean isReady() {
					return true;
				}
			};
			final PrintWriter out = new PrintWriter(fileOutputStream);

			HttpServletResponse myResponse = new HttpServletResponseWrapper(response) {
				public ServletOutputStream getOutputStream() {
					return stream;
				}

				public PrintWriter getWriter() {
					return out;
				}
			};
			request.getRequestDispatcher(jspUrl).include(request, myResponse);	
			out.close();
			stream.close();
			fileOutputStream.close();
			
		} catch (ServletException | IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
