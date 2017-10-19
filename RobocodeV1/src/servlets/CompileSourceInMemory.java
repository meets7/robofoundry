package servlets;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import javax.tools.JavaCompiler.CompilationTask;

public class CompileSourceInMemory {
  public static boolean CompileSource(String classname, String src, String classpath) throws IOException {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    
    JavaFileObject file = new JavaSourceFromString(classname, src);
       
    Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
    List<String> options=new ArrayList<String>();    // add -cp classpath option, etc here
	options.addAll(Arrays.asList("-classpath", classpath));
	System.out.println(classpath);
	options.addAll(Arrays.asList("-d", classpath));
    CompilationTask task = compiler.getTask(null, null, null, options, null, compilationUnits);
	boolean success = task.call();
    return success;
  }
}

class JavaSourceFromString extends SimpleJavaFileObject {
  final String code;

  JavaSourceFromString(String name, String code) {
    super(URI.create("string:///" + name.replace('.','/') + Kind.SOURCE.extension),Kind.SOURCE);
    this.code = code;
  }

  @Override
  public CharSequence getCharContent(boolean ignoreEncodingErrors) {
    return code;
  }
}