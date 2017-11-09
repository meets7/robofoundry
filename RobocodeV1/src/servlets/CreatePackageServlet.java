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
 * Servlet implementation class CreatePackageServlet
 */
@WebServlet("/CreatePackage")
public class CreatePackageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection connection = null;
	PreparedStatement ptmt = null;
	ResultSet resultSet = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreatePackageServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String packagename = "";
		String userid = "1";
		if (request.getParameter("packagename") != null) {
			packagename =request.getParameter("packagename");
		}
		String url = "jdbc:mysql://localhost:3306/robocode";
		String user = "root";
		String password = "root";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(url, user, password);
			
			String sql = "Insert into userpackages (userid,packagename) values (?,?)";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userid);
			statement.setString(2, packagename);
			int count = statement.executeUpdate();
			conn.close();
	        request.setAttribute("message", count);
//	        request.getRequestDispatcher("NewPackage.jsp").forward(request, response);
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
