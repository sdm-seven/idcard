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

import com.qcloud.image.http.HttpRequest;
import com.ych.base.BaseServlet;
import com.ych.base.DBOperator;
import com.ych.base.Tools;

/**
 * Servlet implementation class HelloWorld
 */
@WebServlet("/normal/login.jspx")
public class LoginServlet2 extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private static final String appid="wx9f8671c3c6a5c71b"; 
	private static final String secret="fd6c390f2502903b6e1541dd0dfbd72d"; 
	
	
	// HTTP GET request
		private static JSONObject sendGet(String jscode) throws Exception {
			try
			{
				 String url = "https://api.weixin.qq.com/sns/jscode2session";
			        String httpUrl = url + "?appid=" + appid + "&secret=" + secret + "&js_code=" + jscode
			            + "&grant_type=authorization_code";
				
				URL obj = new URL(httpUrl);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();

				// optional default is GET
				con.setRequestMethod("GET");

				//add request header
				con.setRequestProperty("User-Agent", "Mozilla/5.0");

				int responseCode = con.getResponseCode();
				System.out.println("\nSending 'GET' request to URL : " + url);
				System.out.println("Response Code : " + responseCode);

				BufferedReader in = new BufferedReader(
				        new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				//print result
				System.out.println(response.toString());	
				return new JSONObject(response.toString());
			}
			catch(Exception e)
			{
				return new JSONObject("");
			}
			  

		}


	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doGet(request, response);
		  JSONObject result=new JSONObject();
		  result.put("success", false);
		
		  try
          {
			  String  jssession=request.getParameter("jssession");
			  String  code=request.getParameter("code");
			  String  openid="";
			  JSONObject currentsession=null;
			  
			  System.out.print(jssession);
			  System.out.print(code);
			  System.out.print(jssession.equals(""));
			  System.out.print(code.equals(""));
			  System.out.print(jssession.equals("null"));
			  System.out.print(jssession != null);
			  System.out.print(jssession == null);
			  System.out.print(jssession != null &&!jssession.equals(""));
			  
			 
		
             //用户已经存在于小程序中
              if(jssession != null&&!jssession.equals("null") &&!jssession.equals(""))
              {
            	JSONArray sessionsbyFindId = dbo.get("select * from Session  where SessionID ='"+jssession+"'"+" limit 1", null);
            	if(sessionsbyFindId!=null&& sessionsbyFindId.length()>0)
            	{///说明已经登录
            		currentsession=sessionsbyFindId.getJSONObject(0);
            		openid=currentsession.optString("OpenId");
            		System.out.println("openid:"+openid);
            	}
            	else
            	{///说明没有登录
            		//获取session
            		JSONObject code2sessionresult = sendGet(code);
            		System.out.println(code2sessionresult);
            		openid=  code2sessionresult.optString("openid");
              	   String session_key=  code2sessionresult.optString("session_key");
              	  if(openid != null &&!openid.equals("null")&&!openid.equals(""))
              	  {
              		
            		  String jssessionnew = java.util.UUID.randomUUID().toString();
            		  //获取ssession信息
            		 JSONArray sessionsbyFindopenid = dbo.get("select * from Session where OpenId ='"+openid+"'"+" limit 1" , null);
            		  if(sessionsbyFindopenid==null||sessionsbyFindopenid.length()<=0)
            		  {
            			  currentsession=new JSONObject();
            			  currentsession.put("SessionID", jssessionnew);
            			  currentsession.put("JsSession", jssessionnew);
            			  currentsession.put("OpenId", openid);
            			  currentsession.put("Session_Key", session_key);
            			  dbo.exec("INSERT INTO Session ( SessionID, JsSession,OpenId,Session_Key )  VALUES  ( ?,?,?,? )",new Object[]{jssessionnew,jssessionnew,openid,session_key});
            		  }
            		  else
            		  {
            			  currentsession=sessionsbyFindopenid.getJSONObject(0);
            		  }
              		  
              	  }
              	  else
              	  {
              		result.put("errmsg", "获取微信登录信息失败！openid不能为空");
              	  }
              }
          }
          else  //用户第一次登录
              {
        	  
        	  System.out.print(code);
			  System.out.print(code!=null&&!code.equals(""));
        	     if(code!=null&&!code.equals("null")&&!code.equals(""))
        	     { 
        	    	 //入参正常有值
        	    		JSONObject code2sessionresult = sendGet(code);
                		System.out.println(code2sessionresult);
                		openid=  code2sessionresult.optString("openid");
                   	   String session_key=  code2sessionresult.optString("session_key");
                   	  if(openid != null &&!openid.equals("null")&&!openid.equals(""))
                 	   {//获取到微信账号信息
                 		
               		       String jssessionnew = java.util.UUID.randomUUID().toString();
               		       //获取ssession信息
               		       JSONArray sessionsbyFindopenid = dbo.get("select * from Session where OpenId ='"+openid+"'"+" limit 1", null);
	               		  if(sessionsbyFindopenid==null||sessionsbyFindopenid.length()<=0)
	               		  {
	               			currentsession=new JSONObject();
	               			  currentsession.put("SessionID", jssessionnew);
	               			  currentsession.put("JsSession", jssessionnew);
	               			  currentsession.put("OpenId", openid);
	               			  currentsession.put("Session_Key", session_key);
	               			  dbo.exec("INSERT INTO Session ( SessionID, JsSession,OpenId,Session_Key )  VALUES  ( ?,?,?,? )",new Object[]{jssessionnew,jssessionnew,openid,session_key});
	               		  }
	               		  else
	               		  {
	               			  currentsession=sessionsbyFindopenid.getJSONObject(0);
	               		  }
                 		  
                 	   }
                 	  else
                 	  {
                 		 result.put("errmsg", "获取微信登录信息失败！openid不能为空");
                 	  }
        	     }
        	     else
        	     {
        	    	 result.put("errmsg", "获取登录信息失败！");
        	     }

              }
              
              
              
              
              JSONObject currentuser=null;
              if(currentsession!=null)
              {
            	  JSONArray usersbyFindopenid=dbo.get("select * from User  where OpenId ='"+openid+"' "+"limit 1" , null);
            	  System.out.println(usersbyFindopenid);
            	  if(usersbyFindopenid!=null&&usersbyFindopenid.length()>0)
            	  {
            		  currentuser=usersbyFindopenid.getJSONObject(0);
            	  }
            	  else
            	  {
            		  currentuser=new JSONObject();
            		  currentuser.put("OpenId", currentsession.optString("OpenId"));
            		  currentuser.put("JsCode", code);
            		  currentuser.put("JsSession", currentsession.optString("JsSession"));
            		  currentuser.put("Session_Key", currentsession.optString("Session_Key"));
           			  dbo.exec("INSERT INTO User ( OpenId, JsCode,JsSession,Session_Key )  VALUES  ( ?,?,?,? )",new Object[]{currentsession.optString("OpenId"),code,currentsession.optString("JsSession"),currentsession.optString("Session_Key")});
            	  }
            	  
            	  
            	  
            	  currentuser.put("success", true);
            	  result=currentuser;
           
              }
              
              
              
              
              
        
              
              
              
          }
		  catch(Exception e)
		  {
			  result.put("errmsg", e.getMessage());
		  }
		  
		  System.out.println(result);
		  response.getWriter().print(result);
	}

	
	
	public static void main(String[] args) {
		try {
		sendGet("11");
		}catch(Exception ex) {
			
		}
	}
}
