package edu.utdallas;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateJarFile {
	public static void createJar(String inputDirectory, String jarName) throws IOException {
		Manifest manifest = new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
		JarOutputStream target = new JarOutputStream(new FileOutputStream(jarName), manifest);
		add(new File(inputDirectory), target);
		target.close();
	}

	private static void add(File source, JarOutputStream target) throws IOException {
		BufferedInputStream in = null;
		try {
			if (source.isDirectory()) {
				String name = source.getPath().replace("\\", "/");
				Pattern r = Pattern.compile("(?<=/roboclasses/)(.*)");
				// int index = name.indexOf("/roboclasses");
				// name = name.substring(index);
				Matcher m = r.matcher(name);
				if (m.find()) {
					String newname = m.group(0);
					if (!name.endsWith("/"))
						name += "/";
					JarEntry entry = new JarEntry(name);
					entry.setTime(source.lastModified());
					target.putNextEntry(entry);
					target.closeEntry();
				}
				for (File nestedFile : source.listFiles())
					add(nestedFile, target);
				return;
			}

			String name = source.getPath().replace("\\", "/");
			Pattern r = Pattern.compile("(?<=/roboclasses/)(.*)");
			Matcher m = r.matcher(name);
			if (m.find()) {
				String newname = m.group(0);
				JarEntry entry = new JarEntry(newname);
				entry.setTime(source.lastModified());
				target.putNextEntry(entry);
				in = new BufferedInputStream(new FileInputStream(source));

				byte[] buffer = new byte[1024];
				while (true) {
					int count = in.read(buffer);
					if (count == -1)
						break;
					target.write(buffer, 0, count);
				}
				target.closeEntry();
			}
		} finally {
			if (in != null)
				in.close();
		}
	}
}
