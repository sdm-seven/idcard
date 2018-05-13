package com.ych.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.ych.base.BaseServlet;
import com.ych.base.DBOperator;
import com.ych.base.Tools;

/**
 * Servlet implementation class HelloWorld
 */
@WebServlet("/hello.jspx")
public class HelloServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().print("helloworld");
		JSONArray array = dbo.get("select * from Category limit 2", null);
		response.getWriter().print(array);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(request, response);
		response.getWriter().print("helloworld");
	}
}
