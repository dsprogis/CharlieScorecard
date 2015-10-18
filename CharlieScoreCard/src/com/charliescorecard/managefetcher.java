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
	private Thread fetch_manager = null;

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

		System.out.println("In managefetcher:doGet.");

	    try {
	    	String requestID = request.getParameter("request-id");
			System.out.println("doGet request-id recieved:" + requestID );
			
			response.setContentType(CONTENT_TYPE);
		    PrintWriter out = response.getWriter();
		    
		    switch( requestID ) {
		    case "get-fetcher-status":
				System.out.println("case: managefetcher - get-fetcher-status");
		        try {
				    String jsonRoutes = new dbaccess().getFetcherStatus();
				   	out.println( jsonRoutes );
//		        	System.out.println( jsonRoutes );
		        	System.out.println("Succeeded in: get-fetcher-status" );
		        } catch( Exception e) {
		        	System.out.println( e );
		        	System.out.println("Failed in: get-fetcher-status" );
		        }
				break;
				
			default:
				System.out.println("case: managefetcher - nothing done for request-id: " + requestID);
				break;
			}
		    
	    }
	    catch(Exception e) {
	      e.printStackTrace();
			System.out.println("case: managefetcher - request-id NOT recieved.");
	    }		

	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//		System.out.println("In managefetcher:doPost.");

	    try {
	    	String requestID = request.getParameter("request-id");
//			System.out.println("managefetcher:doGet request-id received:" + requestID );
			
			response.setContentType(CONTENT_TYPE);
		    PrintWriter out = response.getWriter();
			JSONObject jsonResponse = new JSONObject();
		    
		    switch( requestID ) {
		    case "manager-start":
				System.out.println("case: managefetcher - manager-start");
				// TODO : check FetchPool still alive - when was last time thread pinged DB? 

				new dbaccess().setSetting( "fetchers", "running" );

				if ( null == fetch_manager ) {				// If Fetch thread exists, don't start again.
					fetch_manager = new FetchManager();
					fetch_manager.start();
//					System.out.println("Fetch Manager launched");
				} else {
					// TODO : This may or may not be the case - when did it last ping?
//					System.out.println("Fetch Manager not null and assumed running");
				}
				break;

		    case "manager-stop":
				System.out.println("case: managefetcher - manager-stop");
				// TODO : to really see whether fetch manager is running - check thread alive
				
				// Stop the fetchers (no harm in stopping even if already stopped
				new dbaccess().setSetting( "fetchers", "stopped" );				
				
				if ( null != fetch_manager ) {
					fetch_manager.interrupt();
					fetch_manager = null;
//					System.out.println("Fetch Manager interupted");
				} else {
//					System.out.println("Fetch Manager is null and assumed not alive");
				}
				break;

		    case "get-manager-state":
				System.out.println("case: managefetcher - get-manager-state");
				
				if( null == fetch_manager ) {
					jsonResponse.put("manager-state", "dead" );					
				} else {
					jsonResponse.put("manager-state", "alive" );
				}
				out.print(jsonResponse.toString());
				break;
				

		    default:
				System.out.println("case: managefetcher - nothing done for request-id: " + requestID);
				break;
			}
		    
	    }
	    catch(Exception e) {
	      e.printStackTrace();
			System.out.println("case: managefetcher - request-id NOT recieved.");
	    }	
		
	}

}
