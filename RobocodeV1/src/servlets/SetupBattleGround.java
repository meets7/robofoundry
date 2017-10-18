package servlets;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.Statement;

import DTO.RobotDTO;
import DTO.UserDTO;
import Service.UpdateRobotRestClientService;

//@WebServlet("/setupbattleground")
public class SetupBattleGround extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection connection = null;
	PreparedStatement ptmt = null;
	ResultSet resultSet = null;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SetupBattleGround() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession session = request.getSession();
		String url = "jdbc:mysql://localhost:3306/robocode";
		String user = "root";
		String password = "root";

//		RobotDTO robotDTO = null;
//		if (request.getParameter("RobotId") != null) {
//		request.setAttribute("User",robotDTO.getUserId());
//
//		UpdateRobotRestClientService updateRobot = new UpdateRobotRestClientService();
//		String message = updateRobot.updateRobot(robotDTO);
//
//		System.out.println("updated message:"+message);
//		request.setAttribute("message", message);
//		request.setAttribute("date", robotDTO.getUpdatedDate());
		try {
			Connection conn = DriverManager.getConnection(url, user, password);
			
			String robotIds = request.getParameter("RobotId");
			
			
			Object[] robots = robotIds.split(",");

			robotIds = "";
			for(int i=0;i<robots.length;i++)
				robotIds = robotIds + "'" + robots[i] + "',";
			robotIds = robotIds.substring(0, robotIds.length()-1);
			
//			PreparedStatement stmt = conn.prepareStatement("select RobotID, RobotCode from robot where robotId in ( ? )");
			String sql = "select RobotID, RobotCode from robot where robotId in ( %s )";
			sql = String.format(sql, robotIds);
//			stmt.setArray(1, array);
			Statement stmt = (Statement) conn.createStatement();
			resultSet = stmt.executeQuery(sql);
			
			while(resultSet.next()){
				String robocode =resultSet.getString("RobotCode");
				String robotid = resultSet.getString("RobotId");
				CompileSourceInMemory.CompileSource(robotid, robocode, getServletContext().getRealPath("/") + "robocode.jar");
				
			}
			
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		
		RequestDispatcher rd=request.getRequestDispatcher("Edit_Robot2.jsp");    
		rd.forward(request, response);
	}
}

