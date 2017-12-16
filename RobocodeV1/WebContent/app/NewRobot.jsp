<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>

<%
ResultSet resultset = null;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Create New Robot</title>
	<!-- Bootstrap Core CSS - Uses Bootswatch Flatly Theme: http://bootswatch.com/flatly/ -->
	<link href="../css/bootstrap.min.css" rel="stylesheet">

	<!-- Custom CSS -->
	<link href="../css/freelancer.css" rel="stylesheet">

	<!-- Custom Fonts -->
	<link href="../font-awesome/css/font-awesome.min.css" rel="stylesheet"
	type="text/css">

	<link
	href="http://fonts.googleapis.com/css?family=Lato:400,700,400italic,700italic"
	rel="stylesheet" type="text/css">
	<style>
		#RobotCode {
			position: absolute;
			top: 200px;
			right: 0;
			bottom: 0;
			left: 0;
		}
	</style>
</head>

<body>

<%
String roles = (String)session.getAttribute("userrole");
String user = (String)session.getAttribute("username");

if(roles.equals("manager")||roles.equals("developer"))
{%>
	<%@include file="../includes/header.jsp"%>
	<body>
		<div class="container">
			<div id="page-wrapper">
				<div class="row">
					<div class="col-lg-12">
						<h1 class="page-header">
							<i class="fa fa-file-text"></i>	Create a New Robot,
							<%=session.getAttribute("userx")%></h1>
					</div>
					<div class="row">
						<div class="col-lg-6">
								<div class="input-group">
									<%
									
									Set<String> list_of_tenants = new HashSet<String>();
									Set<String> list_of_domains = new HashSet<String>();
									Set<String> list_of_robots = new HashSet<String>();
									HashMap<String, List<String>> map = new HashMap<String, List<String>>();
									HashMap<String, List<String>> packagemap = new HashMap<String, List<String>>();
									HashMap<String, List<String>> domain_robot_map = new HashMap<String, List<String>>();							
									try {
									String connectionURL = "jdbc:mysql://localhost:3306/robocode";
									Class.forName("com.mysql.jdbc.Driver").newInstance();
									Connection connection = DriverManager.getConnection(connectionURL, "root",
									"root");
									Statement statement = connection.createStatement();
									String selectString="SELECT userID, packageID, robotID from robot where userID ='"+user+"'";
									resultset = statement
									.executeQuery(selectString);
									
									Statement newstatement = connection.createStatement();
									String newString ="SELECT packagename from userpackages where userid='"+user+"'";
									ResultSet packageSet = newstatement
									.executeQuery(newString);
									
									%>
									<script type="text/javascript">
										function getDomains() {
											var x = document.getElementById("domain_name").value;
											x = '<%=user%>';
											$.ajax({
												url : "GetrobotDomain",
												data : "tenant_name=" + x + "",
												type : 'POST',
												async : false,
												success : function(html) {
													console.log("html:" + html);
													$("#package").html(html);
												},
												error : function(html) {
													console.log("error html:" + html);
												}
											});
										}	
									</script>
									<h1>Create a new robot</h1>
									<div class="row">
									<label>Select User</label> 
									<select name="domain_name" id="domain_name" class="form-control" onchange="getDomains()">
									<option value="" disabled selected>Select User</option>
									<option value="<%=user%>"><%=user%></option>
								<%
					
								packagemap.put(user,new ArrayList<String>());
								while(packageSet.next())
								{
									String pack = packageSet.getString(1);
									packagemap.get(user).add(pack);
								}
								session.setAttribute("tenantMap", packagemap);
								session.setAttribute("DomainMap", domain_robot_map);
								session.setAttribute("userx", "User");
									%>
									</select>
	<script type="text/javascript">
			function getRobots() {
			var x = document.getElementById("package").value;
			$.ajax({
				url : "Getrobots",
				data : "domain_name=" + x + "",
				type : 'POST',
				async : false,
				success : function(html) {
					console.log("html:" + html);
					$("#displayrobots").html(html);
				},
				error : function(html) {
					console.log("error html:" + html);
				}
			});
		}	
	</script>
	<label>Select Package</label> 
	/*<select name="package" id="package" class="form-control" onchange="getRobots()">
		<option value="" disabled selected>Select Package</option>
	</select>*/ <br />  
<%} catch (Exception e) {
out.println("wrong entry" + e);
}
%>

<label> Robot Name: </label>
<input width="200px" name="roboName" id="robo_name" type="text" class="fa-la" placeholder="eg. MyRobot" />

</div>
<div class="row">
	<button type="submit" class="btn btn-success" id="create" onclick="NewRobot();">Next</button>
	<%
	session.getAttribute("cfuserinfo");
	out.println(session.getAttribute("cfuserinfo"));
	%>
</div>
</div>


<br>
<script type="text/javascript">
	function NewRobot(){
		var robotPackage=document.getElementById("package").value;
		var name = document.getElementById("robo_name").value;
		var user = document.getElementById("domain_name").value;
		$.ajax({
			url: 'newRobot',
			type: 'POST',
			data: "RobotInfo="+robotPackage+"-"+name+"-"+user,
			async : false,
			success : function(html) {
				console.log(html);
				window.location.replace("NewRobot2.jsp"); 
			}
		});  
		event.preventDefault();
	}
</script>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

</form>

</div>
<!-- /.col-lg-6 (nested) -->
</div>
<!-- /.row (nested) -->
</div>
<!-- /.panel-body -->
</div>

</div>

<%}else{ 
		out.println("Not authorized to create a new  robot");
	}%>
</body>
</body>
</html>