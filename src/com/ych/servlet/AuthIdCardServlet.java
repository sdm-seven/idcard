package com.ych.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.qcloud.image.ImageClient;
import com.qcloud.image.ImgUtils;
import com.qcloud.image.http.HttpRequest;
import com.ych.base.BaseServlet;
import com.ych.base.DBOperator;
import com.ych.base.Tools;

/**
 * Servlet implementation class HelloWorld
 */
@WebServlet("/face/AuthIdCard.jspx")
public class AuthIdCardServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private static final String appid="wx9f8671c3c6a5c71b"; 
	private static final String secret="fd6c390f2502903b6e1541dd0dfbd72d"; 
	
	private static final String  qcloudappId = "1253648361";
	private static final String qcloudsecretId = "AKIDlU6Rjw6iliP6nNqnJzXu59ugMsJWbeVB";
	private static final String qcloudsecretKey = "oo4GIBxduFfJ3hRk6R7OT1Us8Ugnoruq";
	private static final String qcloudbucketName = "checkidbucketname-1";
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doGet(request, response);
		JSONObject result=new JSONObject();
		
		  result.put("success", false);
		
		  String  name=request.getParameter("name");
		  String  cardnumber=request.getParameter("cardnumber");
		  String  imgurl=request.getParameter("imgurl");
		  
		  
		  
		  ImageClient imageClient = new ImageClient(qcloudappId, qcloudsecretId, qcloudsecretKey);
		  String ret= ImgUtils.AuthIdCardCompare(imageClient, qcloudbucketName,name, cardnumber,imgurl); 
		  
		  
	
       
		  System.out.println(ret);
		  response.getWriter().print(ret);
	}
	
}
