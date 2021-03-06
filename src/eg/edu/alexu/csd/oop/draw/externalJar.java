package eg.edu.alexu.csd.oop.draw;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class externalJar {

	// singelton pattern.

	private static externalJar instance = new externalJar();

	private externalJar() {
	}

	public static externalJar getInstance() {
		return instance;
	}

	/**
	 * gets external classes jars implementing shape interface.
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public List<Class<? extends Shape>> supportedShape() throws ClassNotFoundException, IOException {
		@SuppressWarnings("rawtypes")
		List list = new ArrayList<Class<?>>();
		String jarPath = System.getProperty("java.class.path");
		String x = System.getProperty("path.separator");
		String[] parts = jarPath.split(x);
		for (int i = parts.length - 1; i >= 0; i--) {
			if (parts[i].contains("E:\\") || parts[i].contains(".cp")) {
				continue;
			}
			@SuppressWarnings("resource")
			JarFile jarFile = new JarFile(parts[i]);
			@SuppressWarnings("rawtypes")
			Enumeration e = jarFile.entries();

			//URL[] urls = { new URL("jar:file:" + System.getProperty("java.class.path") + "!/") };

			ClassLoader cl = getClass().getClassLoader();

			while (e.hasMoreElements()) {
				JarEntry je = (JarEntry) e.nextElement();
				if (je.isDirectory() || !je.getName().endsWith(".class")) {
					continue;
				}
				if (je.getName().contains("oop") && !je.getName().contains("DummyShape")) {
					String className = je.getName().substring(0, je.getName().length() - 6);
					className = className.replace('/', '.');
					@SuppressWarnings("rawtypes")
					Class c = null;
					c = cl.loadClass(className);
					if (Shape.class.isAssignableFrom(c)) {
						list.add((Class<? extends Shape>) c);
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 
	 * @param jarPath
	 * @return
	 */
	public Class<?> installPluginShape(String jarPath) {
		// TODO Auto-generated method stub
		try {
			@SuppressWarnings("resource")
			JarFile jarFile = new JarFile(jarPath);
			Enumeration<JarEntry> e = jarFile.entries();
			URL[] urls = {new URL("jar:file:" + jarPath+"!/")};
			URLClassLoader classLoader = URLClassLoader.newInstance(urls);
			while(e.hasMoreElements()){
				JarEntry je = e.nextElement();
				if(je.isDirectory()||!je.getName().endsWith(".class")){
					continue;
				}
			    String className = je.getName().substring(0,je.getName().length()-6);
				 className = className.replace('/', '.');
				    try {
						Class<?> c = classLoader.loadClass(className);
						return c;
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
//	private static Class<? extends Shape> classloading(File pathToJar, String packagename)
//			throws MalformedURLException, ClassNotFoundException {
//		URL[] urls = { new URL("jar:file:" + pathToJar + "!/") };
//		URLClassLoader cl = URLClassLoader.newInstance(urls);
//		String classname = pathToJar.getName().substring(0, pathToJar.getName().length() - 4);
//		@SuppressWarnings("unchecked")
//		Class<? extends Shape> s = (Class<? extends Shape>) cl.loadClass(packagename + "." + classname);
//		return s;
//	}
}