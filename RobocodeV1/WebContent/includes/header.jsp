
<head>
<link href="../css/bootstrap.css" rel="stylesheet">
<link
	href="//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">

<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>

<script
	src="http://netdna.bootstrapcdn.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!-- Bootstrap Core CSS - Uses Bootswatch Flatly Theme: http://bootswatch.com/flatly/ -->
<link href="../css/bootstrap.min.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="../css/freelancer.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="../font-awesome/css/font-awesome.min.css" rel="stylesheet"
	type="text/css">
<link
	href='https://fonts.googleapis.com/css?family=Righteous|Fredoka+One'
	rel='stylesheet' type='text/css'>

<link
	href="http://fonts.googleapis.com/css?family=Lato:400,700,400italic,700italic"
	rel="stylesheet" type="text/css">

<script src="http://code.jquery.com/jquery.min.js"></script>
</head>
<body id="page-top" class="index">
	<!-- Navigation -->
	<nav class="navbar navbar-default navbar-fixed-top">
		<div class="container">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header page-scroll">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target="#bs-example-navbar-collapse-1">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="welcome">Robocode</a>
			</div>

			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav navbar-right">
					<li class="hidden"><a href="#page-top"></a></li>
					<li class="dropdown"><a href="#new">New</a>
						<ul class="dropdown-menu">
							<li><a href="NewRobot.jsp">Robot</a></li>
							<li><a href="NewPackage.jsp">Package</a></li>
							<li><a href="#CDomain">Domain</a></li>
							<li><a href="#CRole">Role</a></li>
						</ul></li>

					<li class="dropdown"><a href="#edit">Edit</a>
						<ul class="dropdown-menu">
							<li><a href="Edit_Robot3.jsp">Robot</a></li>
							<li><a href="#EUser">User</a></li>
							<li><a href="#EDomain">Domain</a></li>
							<li><a href="#ERole">Role</a></li>
						</ul></li>
					<li class="dropdown"><a href="#view">View Robots</a>
						<ul class="dropdown-menu">
							<li><a href="ViewRobot.jsp">My Robots</a></li>
							<li><a href="ViewSharedRobots.jsp">Shared with me</a></li>
							<li><a href="#VDomain">Domain</a></li>
							<li><a href="#VRole">Role</a></li>
						</ul></li>
					
					<li class="page-scroll"><a href="ShareRobots.jsp">Share Robots</a></li>
							
					<li class="page-scroll"><a href="PlayBattle.jsp">Play
							Battle!</a></li>
					
					<%String tempname = (String)session.getAttribute("username");
					  String temprole = (String)session.getAttribute("userrole");%>
					<li class="dropdown"><a href="#view">Profile</a>
						<ul class="dropdown-menu">
							<li>&nbsp Hi, My name is &nbsp <%=tempname %></li>
							<li>&nbsp I am a <%=temprole %></li>
							<li><a href="Logout.jsp"">Logout</a></li>
						</ul></li>
						
					<li class="page-scroll"><a href="Logout.jsp">Logout</a></li>
				</ul>
			</div>
			<!-- /.navbar-collapse -->
		</div>
	</nav>
</body>
<script>
	function submit() {
		$("#role").submit();
	}

	function submit1() {
		$("#heirarchy").submit();
	}
	function submit2() {
		$("#heirarchyc").submit();
	}
	function submit3() {
		$("#editrole").submit();
	}
	function submit4() {
		$("#heirarchyEdit").submit();
	}
	function submit5() {
		$("#packageEdit").submit();
	}
	function submit6() {
		$("#maph").submit();
	}
	function submit8() {
		$("#Ppermissionedit").submit();
	}
</script>


</html>