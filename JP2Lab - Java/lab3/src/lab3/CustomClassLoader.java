package lab3;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by DZONI on 19.10.2016.
 */
public class CustomClassLoader extends ClassLoader {

	public CustomClassLoader(ClassLoader parent) {
		super(parent);
	}

	public CustomClassLoader() {
	}

	/** Fully Classified name of class, for example com.journaldev.Foo */
	@Override
	public Class loadClass(String name) throws ClassNotFoundException {
		System.out.println("Laduje klase'" + name + "'");
		if(name.startsWith("java")){
			System.out.println("Laduje klase uzywajac loadera rodzica");
			return super.loadClass(name);
		}
		System.out.println("Laduje klase uzywajac CustomClassLoader");

		String file = name.replace('.', File.separatorChar) + ".class";
		byte[] bytes = null;
		try {
			bytes = loadClassFileData(file);
			Class c = defineClass(name, bytes, 0, bytes.length);
			resolveClass(c);
			return c;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private byte[] loadClassFileData(String name) throws IOException {
		InputStream stream = getClass().getClassLoader().getResourceAsStream(name);
		int size = stream.available();
		byte buff[] = new byte[size];
		DataInputStream in = new DataInputStream(stream);
		in.readFully(buff);
		in.close();
		return buff;
	}

	public File[] wylistujZasobyWPakiecie(String pakiet) {
		try {
			File[] file = (new File(getClass().getResource("../" + pakiet).toURI())).listFiles();
			return file;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void wyswietlZawartoscSloja(String jarPath) {
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(jarPath);
			Enumeration allEntries = jarFile.entries();
			while (allEntries.hasMoreElements()) {
				JarEntry entry = (JarEntry) allEntries.nextElement();
				String name = entry.getName();
				System.out.println(name);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
