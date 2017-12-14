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
		String roles = request.getParameter("user_roles");

		Map<String, Object> cfroles, cfuserinfo;
		cfuserinfo = new ObjectMapper().readValue(userinfo, HashMap.class);
		cfroles = new ObjectMapper().readValue(roles, HashMap.class);
		session.setAttribute("cfaccess_token", cfaccess_token);
		session.setAttribute("cfuserinfo", cfuserinfo);
		session.setAttribute("cfroles", cfroles);

		// String userDTO = session.getAttribute("userx").toString();
		// if (request.getParameter("domain_name") != null) {
		// session.setAttribute("robotList", robot_DTO);

		RequestDispatcher requestDispatcher = request.getRequestDispatcher("welcome.jsp");
		requestDispatcher.forward(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
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