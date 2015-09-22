package com.charliescorecard;

import com.charliescorecard.dbaccess;

//import java.io.ByteArrayOutputStream;
import java.io.IOException;
//import java.io.OutputStream;
import java.io.PrintWriter;
//import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class processrequests
 */
@WebServlet("/processrequests")
public class processrequests extends HttpServlet {
	private static final long serialVersionUID = 1L;
//	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
	
	/**
     * @see HttpServlet#HttpServlet()
     */
    public processrequests() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		System.out.println("In doGet.");

	    try {
	    	String requestID = request.getParameter("request-id");
			System.out.println("doGet request-id recieved:" + requestID );
			
			response.setContentType(CONTENT_TYPE);
		    PrintWriter out = response.getWriter();
		    
		    switch( requestID ) {
		    case "get-tracked-routes":
				System.out.println("Inside updated-routes");
		        try {

				    String jsonRoutes = new dbaccess().getTrackedRoutes();
				    
				   	out.println( jsonRoutes );
		        	System.out.println( jsonRoutes );
		        	
		        	System.out.println("Succeeded in: get-tracked-routes" );

		        } catch( Exception e) {
		        	System.out.println( e );
		        	System.out.println("Failed in: get-tracked-routes" );
		        }
				break;

			default:
				System.out.println("Nothing done for request-id: " + requestID);
				break;
			}
		    
	    }
	    catch(Exception e) {
	      e.printStackTrace();
			System.out.println("request-id NOT recieved.");
	    }		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
