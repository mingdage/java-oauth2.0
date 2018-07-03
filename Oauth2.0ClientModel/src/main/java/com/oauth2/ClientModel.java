package com.oauth2;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


//oauth2.0  客户端授权模式
public class ClientModel {

	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// 运行方法
		String token = getAuth();
		
        System.out.println(token);
		//System.out.println(getAuth());
	}

	// 获取token
	public static  String getAuth() throws Exception {
		// 接口地址
//		 String url = "https://transfer.lab.supwin.com/login/connect/token";
		String url = "http://192.168.199.236:6001/connect/token";
		PrintWriter out = null;
		BufferedReader in = null;
		String access_token = null;
		
		//base64加密
		String client_id = "ro.client:secret";
		String encode = BASE64(client_id);
		//System.out.println( encode.substring(0, encode.length()-2));

		try {
			// 将url转变URL类对象
			URL authUrl = new URL(url);
			// 打开URL连接
			HttpURLConnection conn = (HttpURLConnection) authUrl.openConnection();
			// 不缓存
			conn.setUseCaches(false);
			// 设置HHTP请求的请求头header
			//conn.setRequestProperty("Authorization", "Basic cm8uY2xpZW50OnNlY3JldA==");
			conn.setRequestProperty("Authorization", encode.substring(0, encode.length()-2));
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);

			//设置HTTP请求的请求体BODY
			String params = "grant_type=" + "client_credentials" + "&scope=" + "api1";

			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(params);
			// flush输出流的缓冲
			out.flush();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String result = "";
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}

			//System.err.println("result:" + result);
			//System.out.println(result);
			// 序列化对象
			JSONObject jsonObject = JSON.parseObject(result);
			
			access_token = jsonObject.getString("access_token");
			//System.out.println(access_token);
			// // 返回access_token
			return access_token;
		} catch (Exception e) {
			System.err.printf("获取token失败！");
			e.printStackTrace(System.err);
		} finally {// 使用finally块来关闭输出流、输入流
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				System.err.printf("总体的获取token失败！");
			}
			return access_token;
		}
	}

	
	   //base64加密
		public static String BASE64(String key) {
	        byte[] bt = key.getBytes();
	        return "Basic "+(new BASE64Encoder()).encodeBuffer(bt);
	    }

}
