package edu.utdallas;

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
//@WebServlet("/CompileServlet")
public class Compile{
	static Map<String, byte[]> compiledClass=new HashMap<>();
	static Map<String, Map> path=new HashMap<>();

	void addToJar(File src,JarOutputStream jar) throws IOException{
		BufferedInputStream in=null;
		try{
			if(src.isDirectory()){

			}
		}finally{
			if(in!=null) in.close();
		}
	}

	protected void CompileSource(String fileName, String java_src) throws IOException{
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		PrintStream ps=new PrintStream(baos);
		try{
			JavaCompiler compiler=ToolProvider.getSystemJavaCompiler();
			DiagnosticCollector<JavaFileObject> diagnosticCollector=new DiagnosticCollector<>();
			InMemoryFileManager fileManager=new InMemoryFileManager(compiler.getStandardFileManager(diagnosticCollector,null,null));
//			@NotNull String java_src=request.getParameter("src");
//			@NotNull String java_name=request.getParameter("name");
			List<String> options=new ArrayList<String>();    // add -cp classpath option, etc here
			options.addAll(Arrays.asList("-classpath","tempBattleArea"));
			JavaFileObject java_file=new SimpleJavaFileObj(fileName,java_src);
			Iterable<? extends JavaFileObject> unit=Arrays.asList(java_file);
			JavaCompiler.CompilationTask task=compiler.getTask(null,fileManager,diagnosticCollector,options,null,unit);
			boolean success=task.call();

			if(success){
				compiledClass.put(fileName,fileManager.getClassBytes());
				String[] full_path=fileName.split("\\.");
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
