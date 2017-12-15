package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.Statement;;

/**
 * Servlet implementation class EditServlet
 */
public class ShareRobotServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection connection = null;
	PreparedStatement ptmt = null;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ShareRobotServlet() {
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
	
		HttpSession session = request.getSession();
		String robotid = (String)request.getParameter("robotname");
		String userid = (String)request.getParameter("username");
		
		String url = "jdbc:mysql://localhost:3306/robocode";
		String user = "root";
		String password = "root";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(url, user, password);

			String newstmt = "Insert into relationship (robotid,userid) values('"+robotid+"','"+userid+"')";
			PreparedStatement statement = conn.prepareStatement(newstmt);
		
			statement.executeUpdate(newstmt);
			request.setAttribute("success", true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
