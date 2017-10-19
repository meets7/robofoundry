<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

    <title>Robocode Battle</title>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    <script language="javascript">
   
    function writeSummary(summary) {
        summaryElem =
            document.getElementById("summary");
        summaryElem.innerHTML += "<br>";
        summaryElem.innerHTML += summary;
    }
    </script>

</head>
<body>
  <body> 
    <script src ="http://www.java.com/js/deployJava.js"></script>
    <%
    	String robots = (String)session.getAttribute("robots");
    	String classpath = (String)session.getAttribute("classpath");
    %>
    <noscript>A browser with JavaScript enabled is required for this page to operate properly.</noscript>
    <h1>Dynamic Tree Applet Demo</h1>
    <h2>This applet has been deployed with the applet tag <em>without</em> using JNLP</h2>
    <applet alt = "Dynamic Tree Applet Demo" 
        code = "robocode.Robocode"
        archive = "myrobocode.jar"
        width = 800,
        height = 600 >
        <param name="robots" value="${robots}">
        </applet>
     <br>
     <p id="summary">  </p>
  </body> 
</body>
</html>
