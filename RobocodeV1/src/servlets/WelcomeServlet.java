package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;;

/**
 * Servlet implementation class EditServlet
 */
public class WelcomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WelcomeServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		HttpSession session = request.getSession(true);

		String cfaccess_token = request.getParameter("access_token");
		String userinfo = request.getParameter("user_info");
		String org_users = request.getParameter("org_users");
		String cfrole = request.getParameter("user_role");
		String spaceguid = request.getParameter("space_guid");
		String org_id = request.getParameter("org_id");

		Map<String, Object> cfuserinfo;
		cfuserinfo = new ObjectMapper().readValue(userinfo, HashMap.class);
		HashMap<String,String> cforgusers = new ObjectMapper().readValue(org_users, HashMap.class);
		
		session.setAttribute("cfaccess_token", cfaccess_token);
		session.setAttribute("cfuserinfo", cfuserinfo);
		session.setAttribute("userrole", cfrole);
		session.setAttribute("cfspace_guid", spaceguid);
		
		session.setAttribute("org_id", org_id);
		session.setAttribute("cforgusers", cforgusers);
		
		HashMap<String,String>orgusers = (HashMap)session.getAttribute("cforgusers");
		session.setAttribute("orgusers", orgusers);
		
		String username = cfuserinfo.get("user_name").toString(); 
        session.setAttribute("username", username);

		RequestDispatcher requestDispatcher = request.getRequestDispatcher("welcome.jsp");
		requestDispatcher.forward(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		if (session.getAttribute("cfaccess_token") == null) {
			response.sendRedirect("https://localhost:5000");
			return;
		}
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("welcome.jsp");
		requestDispatcher.forward(request, response);
	}

	public static HashMap<String, String> jsonToMap(String t) throws JSONException {

		HashMap<String, String> map = new HashMap<String, String>();
		JSONObject jObject = new JSONObject(t);
		Iterator<?> keys = jObject.keys();

		while (keys.hasNext()) {
			String key = (String) keys.next();
			String value = jObject.optString(key);
			map.put(key, value);

		}

		return map;
	}
}
