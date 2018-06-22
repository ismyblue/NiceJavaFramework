package com.ismyblue.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import com.ismyblue.field.path.ConfigPathField;

public class SendEmailUtil {

	private static char[] code;
	private int wordCount;
	private String emailCaptcha;
	
	private static String hostName,smtpPort,mailUsername,mailPassword,from;
	
	/**
	 * 验证码的字符数
	 * @param wordCount
	 */
	public SendEmailUtil(int wordCount){
		this.wordCount = wordCount;
		createEmailCaptcha();
	}
	
	static{
		code = new char[62];//26个大小写字母和10个数字
		for(int i = 0;i < 26;i++){
			if(i<10)
				code[i+52] = (char)(48 + i);
			code[i] = (char) (65 + i);
			code[i+26] = (char) (97 + i);
		}
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(ConfigPathField.APPPATH_STRING + ConfigPathField.EMAILCONFIG_STRING));
			hostName = properties.getProperty("hostName");
			smtpPort = properties.getProperty("smtpPort");
			mailUsername = properties.getProperty("mailUsername");
			mailPassword = properties.getProperty("mailPassword");
			from = properties.getProperty("from");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private String createEmailCaptcha(){
		StringBuffer sBuffer = new StringBuffer();
		Random random = new Random();	
		for(int i = 0;i < wordCount;i++){
			sBuffer.append(code[random.nextInt(62)]);
		}
		emailCaptcha = sBuffer.toString();
		return emailCaptcha;
	}
	
	
	/**
	 * 发送到目标邮件
	 * @param targetEmail
	 * @return
	 * @throws Exception
	 */
	public boolean send(String targetEmail,String message) throws Exception{
		Email email = new SimpleEmail();
		email.setHostName(hostName);		
		email.setSmtpPort(Integer.valueOf(smtpPort));
		email.setAuthenticator(new DefaultAuthenticator(mailUsername, mailPassword));
		email.setSSLOnConnect(true);
		try {
			email.setFrom(from);
			email.setSubject("验证码");
			email.setMsg("【 www.ismyblue.com 】:\n"+message+":" + emailCaptcha + "\n");
			email.addTo(targetEmail);
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
			throw new Exception("邮件验证码发送失败：" + e.getMessage());
		}		
		return true;
	}
	
	public String getEmailCaptcha(){
		return  this.emailCaptcha;
	}
		
	
}
