package com.ismyblue.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.io.output.WriterOutputStream;

/**
 * 
*    
* 项目名称：Blog   
* 类名称：CaptchaUtil   
* 类描述：   验证码工具，
* 创建人：ismyblue@163.com   
* 创建时间：2018年5月17日 下午7:41:30   
* 修改人：ismyblue@163.com   
* 修改时间：2018年5月17日 下午7:41:30   
* 修改备注：   
* @version    
*
 */
public class CaptchaUtil {	
	
	private String captcha = null;//验证码不带空格
	private int width; //图片高度
	private int height; //图片宽度
	private int wordCount; //验证码字母数字的个数
	private int lineCount; //干扰线的数量	
	private BufferedImage bfimage; //生成的在内存的图片
	private String imageFormatName = "png";//图片的格式名/默认为png
	private static char[] charMap;//随机码 的字典
	
	//字符和边框的距离 与图片高度比值
//	private float margin_top = 1/10;
	//字符和边框的距离 与图片宽度的比值
	private float margin_left = 1f/10;
	//字符重叠的距离 与字符宽度的比值
	private float overlap = 1f/5; 
	//字符的宽度  不要怀疑这个公式，我和女朋友想讨论出来的
//	private int charSizeWidth =	(int) ( ((width - 2*margin_left*width)/(wordCount - wordCount*overlap + overlap)) );
	private float charSizeWidth;
//	int charSizeHeight = (int) ( height - 2*margin_top*height );
	
	/**
	 * 获得当前图片的格式
	 * @return
	 */
	public String getImageFormatName() {
		return imageFormatName;
	}

	/**
	 * 设置图片格式 默认为png
	 * @param imageFormatName
	 */
	public void setImageFormatName(String imageFormatName) {
		this.imageFormatName = imageFormatName;
	}
	
	static{
		charMap = new char[62];//26个大小写字母和10个数字
		for(int i = 0;i < 26;i++){
			if(i<10)
				charMap[i+52] = (char)(48 + i);
			charMap[i] = (char) (65 + i);
			charMap[i+26] = (char) (97 + i);
		}
	}
	
	

	/**
	 * 创建一个随机的验证码，指定高度，宽度，字母数量，干扰线的数量
	 * @param width 图片高度
	 * @param height 图片宽度
	 * @param wordCount 字母数量
	 * @param lineCount 干扰线的数量
	 */
	public CaptchaUtil(int width, int height, int wordCount, int lineCount){
		this.width = width;
		this.height = height;
		this.wordCount = wordCount;
		this.lineCount = lineCount;
//		//得到随机验证码
//		this.captcha = createCaptcha();//先不获得captcha，创建burreredIamge的使用要判读是否是用户指定了captcha		
		this.charSizeWidth =  (float)((width - 2*margin_left*width)/(wordCount - wordCount*overlap + overlap)) ;
		createBufferedImage();
	}
	
	/**
	 * 创建一个指定的的验证码，并且指定高度，宽度，干扰线的数量
	 * @param width
	 * @param height
	 * @param captcha
	 * @param lineCount
	 */
	public CaptchaUtil(int width, int height,String captcha, int lineCount){
		this.width = width;
		this.height = height;
		this.captcha = captcha;
		this.lineCount = lineCount;
		//计算到的验证码的长度
		this.wordCount = captcha.length();
		this.charSizeWidth =  ((width - 2.0f*margin_left*width)/(wordCount - wordCount*overlap + overlap)) ;
		createBufferedImage();
	}
	
	
	/**
	 * 获得验证码，返回随机创建或者是指定的验证码，如果没有验证码，返回空
	 * @return
	 */
	public String getCaptcha(){
		return this.captcha;
	}
		
	/**
	 * 将生成的验证码输出到输出字节流中
	 * @param out
	 */
	public void writeTo(OutputStream out){
		try {
			ImageIO.write(bfimage, imageFormatName, out);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("图片写入错误");
		}
		
	}
	
	/**
	 * 将生成的验证码图片输出到输出字符流中
	 * @param writer
	 */
	public void writeTo(Writer writer){
		try {			
			ImageIO.write(bfimage, imageFormatName, new WriterOutputStream(writer, "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("图片写入错误");
		}
		
	}
	
	public Image getCaptchaImage(){		
		return bfimage;
	}
	
	/**
	 * 生成验证码图片BufferedImage
	 * @return
	 */
	private BufferedImage createBufferedImage() {		
		//在内存中创建一张图片
		bfimage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		//获得图片的的画笔
		Graphics graphics = bfimage.getGraphics();
		//设置画笔颜色		
		graphics.setColor(getColor(150));
		//给图片画背景颜色
		graphics.fillRect(0, 0, width, height);
		//  绘制边框
		graphics.setColor(Color.WHITE);
		graphics.drawRect(0, 0, width - 1, height - 1);			
		//把画笔cast成2d画笔
		Graphics2D graphics2d = (Graphics2D) graphics;
		//画笔设置字体
		Random random = new Random();
		setFont(graphics2d);
		
		//画出验证码
		int x = (int) (margin_left*width);
		int y = (int) (height/2.0f + charSizeWidth/2.0f);		
		for (int i = 0; i < wordCount; i++) {			
			// 随机颜色
			graphics2d.setColor(getColor(255));
			// 旋转 45度到-45度
			int angle = random.nextInt(60) - 30;
			// 换算弧度
			double theta = angle * Math.PI / 180;
			// 获得验证码的单个码
			char c = captcha.charAt(i);			
			// 将c输出到图片
			graphics2d.rotate(theta, x, y);
			graphics2d.drawString(String.valueOf(c), x, y);			
			graphics2d.rotate(-theta, x, y);		
			x += (charSizeWidth - charSizeWidth*overlap);
			
		}	
		// 步骤五 绘制干扰线		
		int x1,x2,y1,y2;
		for (int i = 0; i < lineCount; i++) {
			graphics2d.setColor(getColor(255));
			graphics2d.setStroke(new BasicStroke(1.0f));
			x1 = random.nextInt(width);
			x2 = random.nextInt(width);
			y1 = random.nextInt(height);
			y2 = random.nextInt(height);
			graphics2d.drawLine(x1, y1, x2, y2);			
		}
		
		return bfimage;
	}
	
	/**
	 * 获得一个随机的颜色，可以指定透明度0-255
	 * @param alpha
	 * @return
	 */
	private Color getColor(int alpha){		
		int r,g,b;
		Random random = new Random();		
		r = random.nextInt(255);
		g = random.nextInt(255);
		b = random.nextInt(255);	
		Color color = new Color(r, g, b, alpha);		
		return color;
	}
	
	/**
	 * 创建一个随机验证码
	 * @return String 的验证码
	 */
	private String createCaptcha(){
		StringBuffer sBuffer = new StringBuffer();
		Random random = new Random();	
		for(int i = 0;i < wordCount;i++){
			sBuffer.append(charMap[random.nextInt(62)]);
		}
		return sBuffer.toString();
	}
	
	/**
	 * 给画笔设置字体
	 * @param graphics2d
	 */
	private void setFont(Graphics2D graphics2d) {
		Random random = new Random();
		String[] fonts; 		
		if(this.captcha != null){//如果是用户指定验证码，可能出现中文，所以指定中文字体格式
			fonts = new String[]{"宋体","楷体","行书","PMingLiU","Monospaced","微软雅黑","MingLiU_HKSCS","DialogInput","Serif"};
		}else {
			fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
			this.captcha = createCaptcha();
		}		

		graphics2d.setFont(new Font(fonts[random.nextInt(fonts.length)], Font.BOLD, (int) (charSizeWidth*2)));

	}
	
}

