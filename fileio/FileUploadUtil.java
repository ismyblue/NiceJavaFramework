package com.ismyblue.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
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
	
	private Map<String, String[]> parameterMap;
	private HttpServletRequest request; 
	private DiskFileItemFactory diskFileItemFactory;
	private ServletFileUpload fileUpload;
	private List<FileItem> fileItems;
	
	
	public FileUploadUtil(HttpServletRequest request) throws Exception{
		diskFileItemFactory = new DiskFileItemFactory();
		fileUpload = new ServletFileUpload(diskFileItemFactory);
		fileUpload.setHeaderEncoding("UTF-8");
		this.request = request;		
		parameterMap = new HashMap<String, String[]>();
		
		if(!ServletFileUpload.isMultipartContent(request)){
			throw new Exception("你的form表单不是multipart/form-data!");			
		}
		
		fileItems = fileUpload.parseRequest(request);
		//遍历FileItem
		for(FileItem fileItem : fileItems){
			
			//如果fileItem是表单项
			if(fileItem.isFormField()){
				processFormField(fileItem);
				fileItem.delete();
			}
			//如果是文件，先不存，把文件存入磁盘再存入parametermap			
			
		}		
	}
	
	/*
	 * 获得参数map
	 */
	public Map<String, String[]> getParameterMap(){
		return parameterMap;
	}
	
	/**
	 * 获取参数的value,如果没有则返回null
	 * @param key
	 * @return
	 */
	public String getParamter(String key){
		if(parameterMap.get(key) != null){
			return parameterMap.get(key)[0];
		}
		return null;		
	}
	
	/**
	 * 获得参数的values,如何没有则返回null
	 * @param key
	 * @return
	 */
	public String[] getParamterValues(String key){
		if(parameterMap.get(key) != null){
			return parameterMap.get(key);
		}
		return null;
	}
	
	/**
	 * 获得所有参数的name,如果没有则返回null
	 * @param key
	 * @return
	 */
	public String[] getParamterNames(Object key){
		String[] names = new String[parameterMap.size()];
		names = parameterMap.keySet().toArray(names);		
		return names;
	}
	
	/**
	 * 把上传的文件数据存到指定的路径下
	 * @param dirPath  网站app根目录下的相对路径
	 * @throws FileUploadException
	 */
	public void saveInToDir(String dirPath) throws FileUploadException {		
		
		//遍历FileItem
		for(FileItem fileItem : fileItems){
			//如果fileItem不是是表单项，是文件			
			if(!fileItem.isFormField()){			
				processFile(fileItem, dirPath);
			}
			fileItem.delete();
		}
	}
	

	/**
	 * 处理表单字段,把name 和 value 添加到表单字段map中Map<String, String[]>
	 * @param fileItem
	 */
	private void processFormField(FileItem fileItem) {			
		//字段名
		String name = fileItem.getFieldName();
		//字段值 
		String value;
		try {
			value = fileItem.getString("UTF-8");
			addToFieldMap(name, value);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} 		
	}

	/**
	 * 把key 和value添加到字段的map中，HashMap<String,String[]>	 
	 * @param key
	 * @param value
	 */
	private void addToFieldMap(String key, String value){
		LinkedList<String> list = new LinkedList<String>();	
		
		String[] values = parameterMap.get(key);
		if(values != null){
			for(int i = 0;i < values.length;i++){
				list.add(values[i]);
			}
		}
		list.add(value);
		values = new String[list.size()];
		parameterMap.put(key, list.toArray(values));			
	}
	
	
	/**
	 * 处理上传的文件，把上传的文件改名，存到指定的应用目录中，往字段map（map<String, String[]>）中存入name = 文件路径名
	 * @param fileItem
	 * @param dirPath
	 */
	private void processFile(FileItem fileItem,String dirPath) {
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
		File relativePath = getRelativePath(dirPath, fileName);		
		File absPareDir = new File(request.getServletContext().getRealPath("") + relativePath.getPath());
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
		addToFieldMap(fieldName, relativePath.getPath().replace("\\", "/") + "/" + fileName);
	}
	

	/**
	 * 通过一个文件名得到这个文件在这个网站下应该存放的相对目录
	 * @param dirPath 网站下的相对路径
	 * @param fileName 文件名
	 * @return "dirpath/x/x/fileName"
	 */
	private File getRelativePath(String dirPath, String fileName){
		//得到文件名的哈希值的字符串
		int hashcode = fileName.hashCode();				
		hashcode = hashcode<0?-hashcode:hashcode;
		String hashCodeString = String.valueOf(hashcode);		
		//创建双层目录，防止文件重名
		File relativePath = new File(dirPath + File.separator + hashCodeString.substring(0, 1)
								+ File.separator + hashCodeString.substring(1, 2));		
		if(!relativePath.exists()){
			relativePath.mkdirs();
		}		
		return relativePath;
	}
}
