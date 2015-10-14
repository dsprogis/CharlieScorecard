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
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//		System.out.println("In managefetcher:doPost.");

	    try {
	    	String requestID = request.getParameter("request-id");
//			System.out.println("managefetcher:doGet request-id recieved:" + requestID );
			
			response.setContentType(CONTENT_TYPE);
		    PrintWriter out = response.getWriter();
			JSONObject jsonResponse = new JSONObject();
		    
		    switch( requestID ) {
		    case "manager-start":
				System.out.println("case: manager-start");
				// TODO : check FetchPool still alive - when was last time thread pinged DB? 

				if ( null == fetch_manager ) {				// If Fetch thread exists, don't start again.
					fetch_manager = new FetchManager();
					fetch_manager.start();
					System.out.println("Fetch Manager launched");
				} else {
					// TODO : This may or may not be the case - when did it last ping?
					System.out.println("Fetch Manager not null and assumed running");
				}
				break;

		    case "manager-stop":
				System.out.println("case: manager-stop");
				// TODO : to really see whether fetch manager is running - check thread alive
				
				// Stop the fetchers (no harm in stopping even if already stopped
				new dbaccess().setSetting( "fetchers", "stopped" );				
				
				if ( null != fetch_manager ) {
					fetch_manager.interrupt();
					fetch_manager = null;
					System.out.println("Fetch Manager interupted");
				} else {
					System.out.println("Fetch Manager is null and assumed not alive");
				}
				break;

		    case "get-manager-state":
				System.out.println("case: get-manager-state");
				
				if( null == fetch_manager ) {
					jsonResponse.put("manager-state", "dead" );					
				} else {
					jsonResponse.put("manager-state", "alive" );
				}
				out.print(jsonResponse.toString());
				break;
				
		    case "fetchers-start":
				System.out.println("case: fetchers-start");
				// TODO : check FetchPool still alive - when was last time thread pinged DB? 
				// If Fetch thread exists, don't start again.
				if ( null != fetch_manager ) {
					new dbaccess().setSetting( "fetchers", "running" );
					System.out.println("Fetchers set to begin fetching");
					jsonResponse.put("fetchers-state", "running" );
				} else {
					System.out.println("FetcherManager not running, can't start Fetchers");
					jsonResponse.put("fetchers-state", "unable to manage fetchers while manager stopped" );					
				}
				out.print(jsonResponse.toString());
				break;

		    case "fetchers-stop":
				System.out.println("case: fetchers-stop");
				if( null != fetch_manager ) {
					new dbaccess().setSetting( "fetchers", "stopped" );
					System.out.println("Fetchers set to stop fetching");
					jsonResponse.put("fetchers-state", "stopped" );
				} else {
					System.out.println("FetcherManager not running, can't stop Fetchers");
					jsonResponse.put("fetchers-state", "unable to manage fetchers while manager stopped" );					
				}
				out.print(jsonResponse.toString());
				break;

		    case "get-fetchers-state":
				System.out.println("case: get-fetchers-state");
				String runstate = new dbaccess().getSetting( "fetchers" );
				if( runstate.equals("running") ) {
					jsonResponse.put("fetchers-state", "running" );					
				} else {
					jsonResponse.put("fetchers-state", "stopped" );					
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
