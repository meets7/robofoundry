package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.DTO.PackagePermissionDTO;

import DTO.RobotDTO;
import DTO.UserDTO;
import Service.GetRobotRestClientService;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.DAO.ConnectionFactory;
import com.mysql.jdbc.Statement;

/**
 * Servlet implementation class EditServlet
 */
@WebServlet("/EditServlet")
public class EditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection connection = null;
	PreparedStatement ptmt = null;
	ResultSet resultSet = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		RobotDTO robotDTO = new RobotDTO();
		String selectedItem = null;
		System.out.println(request.getParameter("domain_name"));
		if (request.getParameter("domain_name") != null) {
			selectedItem = request.getParameter("domain_name");
		}
		System.out.println(selectedItem);
		String[] words = selectedItem.split("-");
		String userid = words[0];
		String packageid = words[1];
		String robotid = words[2];
		robotid = robotid.trim();
		session.setAttribute("tenant_name", userid);
		RobotDTO robotAccessDTO = new RobotDTO();
		robotAccessDTO.setUserId("User");
		robotAccessDTO.setRobotName(robotid);
		robotAccessDTO.setPackageId(packageid);

		System.out.println("tenentid " + userid + "packageid " + packageid + "robotName " + robotid);
		String url = "jdbc:mysql://localhost:3306/robocode";
		String user = "root";
		String password = "root";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(url, user, password);

			Statement statement = (Statement) conn.createStatement();
			String newstmt = "SELECT Distinct RobotCode,id from robot where robotID='" + robotid + "' and packageID='"
					+ packageid + "' and userID = '" + userid + "'";
			resultSet = statement.executeQuery(newstmt);

			String Robocode = "";

			while (resultSet.next()) {
				Robocode = resultSet.getString("RobotCode");
			}
			session.setAttribute("RobObj", robotAccessDTO);
			session.setAttribute("robocode", Robocode);
			request.setAttribute("robocode", Robocode);
			PrintWriter out = response.getWriter();
			out.println(Robocode);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
