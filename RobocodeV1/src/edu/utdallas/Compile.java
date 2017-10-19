package edu.utdallas;
//
//import com.sun.istack.internal.NotNull;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.tools.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.jar.JarOutputStream;

/**
 * Created by danei on 5/26/2016.
 */
@WebServlet("/CompileServlet")
public class Compile extends HttpServlet{
	static Map<String, byte[]> compiledClass=new HashMap<>();
	static Map<String, Map> path=new HashMap<>();

	protected void doPost(javax.servlet.http.HttpServletRequest request,javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException{
		doGet(request,response);
	}

	void addToJar(File src,JarOutputStream jar) throws IOException{
		BufferedInputStream in=null;
		try{
			if(src.isDirectory()){

			}
		}finally{
			if(in!=null) in.close();
		}
	}

	protected void doGet(javax.servlet.http.HttpServletRequest request,javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException{
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		PrintStream ps=new PrintStream(baos);
		try{
			JavaCompiler compiler=ToolProvider.getSystemJavaCompiler();
			DiagnosticCollector<JavaFileObject> diagnosticCollector=new DiagnosticCollector<>();
			InMemoryFileManager fileManager=new InMemoryFileManager(compiler.getStandardFileManager(diagnosticCollector,null,null));
			String java_src=request.getParameter("src");
			String java_name=request.getParameter("name");
			List<String> options=new ArrayList<String>();    // add -cp classpath option, etc here
			options.addAll(Arrays.asList("-classpath",getServletContext().getRealPath("/")+"robocode.jar"));
			JavaFileObject java_file=new SimpleJavaFileObj(java_name,java_src);
			Iterable<? extends JavaFileObject> unit=Arrays.asList(java_file);
			JavaCompiler.CompilationTask task=compiler.getTask(null,fileManager,diagnosticCollector,options,null,unit);
			boolean success=task.call();
			ps.println("Compilation "+(success ? "is successful." : "failed!"));
			for(Diagnostic d : diagnosticCollector.getDiagnostics()){
				ps.println(d.getCode());
				ps.println(d.getKind());
				ps.println(d.getPosition());
				ps.println(d.getStartPosition());
				ps.println(d.getEndPosition());
				ps.println(d.getSource());
				ps.println(d.getMessage(null));
			}
			if(success){
				compiledClass.put(java_name,fileManager.getClassBytes());
				String[] full_path=java_name.split("\\.");
				Map<String, Map> current=path;
				for(int i=0;i<full_path.length-1;++i){
					if(!current.containsKey(full_path[i]))
						current=current.get(full_path[i]);
					else{
						Map<String, Map> next=new HashMap<>();
						current.put(full_path[i],next);
						current=next;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace(ps);
		}
		request.setAttribute("log",baos.toString(Charset.defaultCharset().name()));
		RequestDispatcher dispatcher=getServletContext().getRequestDispatcher("/result.jsp");
		dispatcher.forward(request,response);
	}
}
