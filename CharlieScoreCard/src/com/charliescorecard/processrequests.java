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
//				System.out.println("get-tracked-routes");
		        try {
				    String jsonRoutes = new dbaccess().getTrackedRoutes();
				   	out.println( jsonRoutes );
//		        	System.out.println( jsonRoutes );
//		        	System.out.println("Succeeded in: get-tracked-routes" );
		        } catch( Exception e) {
		        	System.out.println( e );
		        	System.out.println("Failed in: get-tracked-routes" );
		        }
				break;

		    case "get-transportation-modes":
//				System.out.println("get-transportation-modes");
		        try {
				    String jsonRoutes = new dbaccess().getTransportationModes();
				   	out.println( jsonRoutes );
//		        	System.out.println( jsonRoutes );
//		        	System.out.println("Succeeded in: get-transportation-modes" );
		        } catch( Exception e) {
		        	System.out.println( e );
		        	System.out.println("Failed in: get-transportation-modes" );
		        }
				break;

		    case "get-routes-by-type":
//				System.out.println("get-routes-by-type");
				try {
			    	String routeType = request.getParameter("route-type");
				    String jsonRoutes = new dbaccess().getRoutesByType( routeType );
				   	out.println( jsonRoutes );
//		        	System.out.println( jsonRoutes );
//		        	System.out.println("Succeeded in: get-routes-by-type" );
		        } catch( Exception e) {
		        	System.out.println( e );
		        	System.out.println("Failed in: get-routes-by-type" );
		        }
				break;
				
		    case "get-directions-by-route":
//				System.out.println("get-directions-by-route");
				try {
			    	String routeID = request.getParameter("route-id");
				    String jsonShape = new dbaccess().getDirectionsByRoute( routeID );
				   	out.println( jsonShape );
//		        	System.out.println( jsonShape );
//		        	System.out.println("Succeeded in: get-directions-by-route" );
		        } catch( Exception e) {
		        	System.out.println( e );
		        	System.out.println("Failed in: get-directions-by-route" );
		        }
				break;
				
		    case "get-service-by-route-and-direction":
//				System.out.println("get-service-by-route-and-direction");
				try {
			    	String routeID = request.getParameter("route-id");
			    	String directionID = request.getParameter("direction-id");
				    String jsonShape = new dbaccess().getServiceByRouteAndDirection( routeID, directionID );
				   	out.println( jsonShape );
//		        	System.out.println( jsonShape );
//		        	System.out.println("Succeeded in: get-service-by-route-and-direction" );
		        } catch( Exception e) {
		        	System.out.println( e );
		        	System.out.println("Failed in: get-service-by-route-and-direction" );
		        }
				break;

		    case "get-shape-bounds":
//				System.out.println("get-shape-bounds");
				try {
			    	String shapeID = request.getParameter("shape-id");
				    String jsonBounds = new dbaccess().getShapeBounds( shapeID );
				   	out.println( jsonBounds );
//		        	System.out.println( jsonBounds );
//		        	System.out.println("Succeeded in: get-shape-bounds" );
		        } catch( Exception e) {
		        	System.out.println( e );
		        	System.out.println("Failed in: get-shape-bounds" );
		        }
				break;

		    case "get-route-shape":
//				System.out.println("get-route-shape");
				try {
			    	String shapeID = request.getParameter("shape-id");
				    String jsonShape = new dbaccess().getRouteShape( shapeID );
				   	out.println( jsonShape );
//		        	System.out.println( jsonShape );
//		        	System.out.println("Succeeded in: get-route-shape" );
		        } catch( Exception e) {
		        	System.out.println( e );
		        	System.out.println("Failed in: get-route-shape" );
		        }
				break;

		    case "get-shape-stops":
//				System.out.println("get-shape-stops");
				try {
			    	String shapeID = request.getParameter("shape-id");
				    String jsonStops = new dbaccess().getShapeStops( shapeID );
				   	out.println( jsonStops );
//		        	System.out.println( jsonStops );
//		        	System.out.println("Succeeded in: get-shape-stops" );
		        } catch( Exception e) {
		        	System.out.println( e );
		        	System.out.println("Failed in: get-shape-stops" );
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
//		doGet(request, response);

		System.out.println("In doPost.");

	    try {
	    	String requestID = request.getParameter("request-id");
			System.out.println("doGet request-id recieved:" + requestID );
			
			response.setContentType(CONTENT_TYPE);
		    PrintWriter out = response.getWriter();
		    
		    switch( requestID ) {
		    case "add-route-to-scheduler":
				System.out.println("add-route-to-scheduler");
		    	String modeID = request.getParameter("mode-id");
		    	String routeID = request.getParameter("route-id");
		    	
		    	if ( new dbaccess().addRouteToScheduler( modeID, routeID ) )
		    	{
					JSONObject json = new JSONObject();
					json.put("Added routeID = ", routeID );
					out.print(json.toString());
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

		
}
