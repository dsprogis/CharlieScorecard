package com.charliescorecard;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * Servlet implementation class managefetcher
 */
@WebServlet("/managefetcher")
public class managefetcher extends HttpServlet {
	private static final long serialVersionUID = 1L;
//	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
	private Fetcher fetch = new Fetcher();

	/**
     * @see HttpServlet#HttpServlet()
     */
    public managefetcher() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("In managefetcher:doPost.");

	    try {
	    	String requestID = request.getParameter("request-id");
			System.out.println("managefetcher:doGet request-id recieved:" + requestID );
			
			response.setContentType(CONTENT_TYPE);
		    PrintWriter out = response.getWriter();
			JSONObject jsonResponse = new JSONObject();
		    
		    switch( requestID ) {
		    case "scheduler-on":
				System.out.println("managefetcher:scheduler-on");
				if( true == fetch.startFetching()) {
					jsonResponse.put("fetcher-state", "run" );
				} else {
					jsonResponse.put("fetcher-state", "unknown" );					
				}
				out.print(jsonResponse.toString());
				break;

		    case "scheduler-off":
				System.out.println("managefetcher:scheduler-off");
				if( true == fetch.stopFetching()) {
					jsonResponse.put("fetcher-state", "stop" );
				} else {
					jsonResponse.put("fetcher-state", "unknown" );					
				}
				out.print(jsonResponse.toString());
				break;

		    case "get-scheduler-runstate":
				System.out.println("managefetcher:get-scheduler-runstate");
				if( true == fetch.isFetching()) {
					jsonResponse.put("fetcher-state", "run" );
				} else {
					jsonResponse.put("fetcher-state", "stop" );					
				}
				out.print(jsonResponse.toString());
				break;

			default:
				System.out.println("managefetcher:Nothing done for request-id: " + requestID);
				break;
			}
		    
	    }
	    catch(Exception e) {
	      e.printStackTrace();
			System.out.println("managefetcher:request-id NOT recieved.");
	    }	
		
	}

}
