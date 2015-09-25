package com.charliescorecard;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.application.Application;

import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/welcomecontroller")
public class welcomecontroller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected Application getApplication(HttpServletRequest req) {
        return (Application)req.getAttribute(Application.class.getName());
    }

    protected Account getAccount(HttpServletRequest req) {
        return (Account)req.getAttribute(Account.class.getName());
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Application app = getApplication(request);
        Account acc = getAccount(request);

        StringBuffer sb = new StringBuffer();
       
        if (acc == null) {			// not logged in
            sb.append("<ul class=\"nav nav-pills navbar-right\">");
            sb.append("<li><a href=\"login\"><span class=\"glyphicon glyphicon-log-in\"></span> login</a></li>");
            sb.append("<li><a href=\"register\"><span class=\"glyphicon glyphicon-user\"></span> register</a></li>");
            sb.append("</ul>");
        } else {					// logged in
            sb.append("<ul class=\"nav nav-pills navbar-right\">");
            sb.append("<li><a><span class=\"glyphicon glyphicon-user\"></span> Welcome, " + acc.getFullName() + "!</a></li>");
            sb.append("<li><a href=\"logout?next=/\"><span class=\"glyphicon glyphicon-log-out\"></span> logout</a></li>");
            sb.append("</ul>");
        }

        PrintWriter out = response.getWriter();
        out.println(sb.toString());

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
