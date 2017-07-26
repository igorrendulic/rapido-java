package com.igor.servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Igor Rendulic
 */
public class MyServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(MyServlet.class.getCanonicalName());

	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		log.log(Level.INFO, "MyServlet called");
		
		response.setContentType("text/html");
		response.getWriter().println("<h1>Welcome to my servlet</h1>");
		
	}
}
