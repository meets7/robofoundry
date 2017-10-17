package edu.utdallas;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * Created by danei on 5/26/2016.
 */
@WebServlet("/robots.jar")
public class FileGetter extends HttpServlet{
	protected void doPost(javax.servlet.http.HttpServletRequest request,javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException{
		doGet(request,response);
	}

	void insert(JarOutputStream jar,Map<String, Map> path,String parent,long time) throws IOException{
		for(String dir : path.keySet()){
			String full_path=parent+dir+"/";
			JarEntry entry=new JarEntry(full_path);
			entry.setTime(time);
			jar.putNextEntry(entry);
			jar.closeEntry();
			insert(jar,path.get(dir),full_path,time);
		}
	}

	protected void doGet(javax.servlet.http.HttpServletRequest request,javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException{
		Manifest manifest=new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION,"1.0");
		JarOutputStream jos=new JarOutputStream(response.getOutputStream(),manifest);
		long time=System.currentTimeMillis();
		insert(jos,Compile.path,"",time);
		for(String name : Compile.compiledClass.keySet()){
			String full_path=name.replace(".","/")+".class";
			JarEntry entry=new JarEntry(full_path);
			entry.setTime(time);
			jos.putNextEntry(entry);
			jos.write(Compile.compiledClass.get(name));
			jos.closeEntry();
		}
		jos.flush();
		jos.close();
	}
}
