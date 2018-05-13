package com.ismyblue.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

public class FileUploadUtil {	
	
	/**
	 * 解析请求，把请求中的表单数据提取出到Map集合中，把请求中的文件提取到指定路径中
	 * @param request
	 * @param m	表单字段map<String, String[]>
	 * @param dirPath 文件要存入webapp的绝对路径
	 * @throws FileUploadException 
	 * @throws UnsupportedEncodingException 
	 */
	public void parseRequset(HttpServletRequest request, Map<String, String[]> m,String dirPath) throws FileUploadException {
		if(!ServletFileUpload.isMultipartContent(request)){
			System.out.println("你的form表单不是multipart/form-data!");
			return ;
		}
		DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
		ServletFileUpload fileUpload = new ServletFileUpload(diskFileItemFactory);
		fileUpload.setHeaderEncoding("UTF-8");
		List<FileItem> fileItems = fileUpload.parseRequest(request);
		//遍历FileItem
		for(FileItem fileItem : fileItems){
			//如果fileItem是表单项
			if(fileItem.isFormField()){
				processFormField(fileItem, m);
			}else{//如果不是表单项，是一个文件
				processFile(fileItem, m, request.getServletContext().getRealPath(""), dirPath);
			}
			fileItem.delete();
		}		
	}

	/**
	 * 处理表单字段,把name 和 value 添加到表单字段map中Map<String, String[]>
	 * @param fileItem
	 * @throws UnsupportedEncodingException 
	 */
	private void processFormField(FileItem fileItem, Map<String, String[]> m) {
			
		//字段名
		String name = fileItem.getFieldName();
		//字段值 
		String value;
		try {
			value = fileItem.getString("UTF-8");
			addToFieldMap(m, name, value);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} 		
	}

	/**
	 * 把key 和value添加到字段的map中，HashMap<String,String[]>
	 * @param m
	 * @param key
	 * @param value
	 */
	private void addToFieldMap(Map<String, String[]> m, String key, String value){
		LinkedList<String> list = null;			
		if(m.get(key) == null){
			list = new LinkedList<String>();
		}		
		
		String[] values = m.get(key);
		if(values != null){
			for(int i = 0;i < values.length;i++){
				list.add(values[i]);
			}
		}
		list.add(value);
		values = new String[list.size()];
		m.put(key, list.toArray(values));			
	}
	
	
	/**
	 * 处理上传的文件，把上传的文件改名，存到指定的应用目录中，往字段map（map<String, String[]>）中存入name = 文件路径名
	 * @param fileItem
	 * @param m
	 * @param appPath 网站主目录
	 * @param dirPath 网站下存储的绝对路径
	 */
	private void processFile(FileItem fileItem, Map<String, String[]> m, String appPath, String dirPath) {
		//获得上传文件的表单字段名
		String fieldName = fileItem.getFieldName();
		//获得上传文件的完整路径或者是文件名
		String fileName = fileItem.getName();
		//这个文件没有上传
		if(fieldName == null)
			return ;
		//获得文件名
		if(fileName != null)
			fileName = FilenameUtils.getName(fileName);
		fileName = UUID.randomUUID() + "_" + fileName;		
		//得到这个文件在网站app存放的相对目录
		File pareDir = getPareDirByFileName(fileName, dirPath);		
		File absPareDir = new File(appPath + pareDir.getPath());
		if(!absPareDir.exists())absPareDir.mkdirs();		
		//创建这个文件的位置
		File newFile = new File(absPareDir, fileName);	
		
		try {			
			BufferedOutputStream otf = new BufferedOutputStream(new FileOutputStream(newFile));
			InputStream iff = fileItem.getInputStream();
			//从上传的文件流中读取流输出到新文件中
			byte[] b = new byte[1024];
			while((iff.read(b)) != -1){
				otf.write(b);
			}			
			iff.close();
			otf.close();			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//把文件的路径加入到表单字段Map<String, String[]>中
		String dir = pareDir + File.separator +  fileName;
		dir = dir.replace("\\", "/");
		addToFieldMap(m, fieldName, dir);	
		
	}
	

	/**
	 * 通过一个文件名得到这个文件在这个网站下应该存放的相对目录
	 * @param fileName 文件名
	 * @param dirPath 起始绝对路径
	 * @return
	 */
	private File getPareDirByFileName(String fileName, String dirPath){
		//得到文件名的哈希值的字符串
		int hashcode = fileName.hashCode();				
		hashcode = hashcode<0?-hashcode:hashcode;
		String hashCodeString = String.valueOf(hashcode);		
		//创建双层目录，防止文件重名
		File pareDir = new File(dirPath + File.separator + hashCodeString.substring(0, 1)
								+ File.separator + hashCodeString.substring(1, 2));		
		if(!pareDir.exists()){
			pareDir.mkdirs();
		}		
		return pareDir;
	}
}
