package lab3;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {
	private static final Scanner scanner = new Scanner(System.in);

	public static int wybierzPlik(File[] files) {
		for (int i = 0; i < files.length; i++) {
			System.out.println(i + "." + files[i].getName());
		}
		System.out.println("Podaj numer");
		if (files.length != 0)
			return scanner.nextInt();
		else
			return -1;
	}

	public static void pokazPolaKlasy(Class klasa) {
		for (Field x : klasa.getDeclaredFields()) {
			System.out.println(x.toGenericString());
		}
	}

	public static String wybierzMetodyKlasy(Class klasa) {
		Method metody[] = klasa.getDeclaredMethods();
		for (int i = 0; i < metody.length; i++) {
			System.out.println(i + "." + metody[i]);
		}
		System.out.println("Podaj numer");
		if (metody.length != 0)
			return metody[scanner.nextInt()].toGenericString();
		else
			return "";
	}

	public static Method stworzMetode(Class klasa, String metoda) throws Exception {
		String nazwaMetody = metoda.split("\\(")[0];
		nazwaMetody = nazwaMetody.split("\\.")[nazwaMetody.split("\\.").length - 1];
		String argumentyMetody = metoda.split("\\(")[1].replace(")", "");
		System.out.println("Nazwa metody: " + nazwaMetody + ", argumenty: " + argumentyMetody);
		Class argumenty[] = zwrocListeArgumentow(argumentyMetody);
		return klasa.getDeclaredMethod(nazwaMetody, argumenty);
	}

	private static Class[] zwrocListeArgumentow(String argumentyMetodyString) throws Exception {
		String listaArgumentowString[] = argumentyMetodyString.split(",");
		List<Class> listaTypowArgumentow = new ArrayList<Class>();

		for (int i = 0; i < listaArgumentowString.length; i++) {

			switch (listaArgumentowString[i]) {
			case "int":
				listaTypowArgumentow.add(int.class);
				break;
			case "double":
				listaTypowArgumentow.add(double.class);
				break;
			case "byte":
				listaTypowArgumentow.add(byte.class);
				break;
			case "short":
				listaTypowArgumentow.add(short.class);
				break;
			case "long":
				listaTypowArgumentow.add(long.class);
				break;
			case "float":
				listaTypowArgumentow.add(float.class);
				break;
			case "boolean":
				listaTypowArgumentow.add(boolean.class);
				break;
			case "char":
				listaTypowArgumentow.add(char.class);
				break;
			case "java.lang.String":
				listaTypowArgumentow.add(String.class);
				break;
			default:
				if(!(listaArgumentowString[i].equals("") && listaArgumentowString[i] != null))
					throw new Exception("Nieznana klasa: " + listaArgumentowString[i]);
			}
		}
		return listaTypowArgumentow.toArray(new Class[0]);
	}

	public static void wywolajMetode(Object object, Method method) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<Object> listaArgumentow = new ArrayList<Object>();
		System.out.println();
		for (Class klasa : method.getParameterTypes()) {
			System.out.println("Podaj wartosc argumentu zgodnie z typem: \"" + klasa + "\"");
			if (klasa == int.class)
				listaArgumentow.add(scanner.nextInt());
			else if (klasa == char.class || klasa == String.class)
				listaArgumentow.add(scanner.next());
			else if (klasa == double.class)
				listaArgumentow.add(scanner.nextDouble());
			else if (klasa == byte.class)
				listaArgumentow.add(scanner.nextByte());
			else if (klasa == short.class)
				listaArgumentow.add(scanner.nextShort());
			else if (klasa == float.class)
				listaArgumentow.add(scanner.nextFloat());
			else if (klasa == long.class)
				listaArgumentow.add(scanner.nextLong());
			else if (klasa == boolean.class)
				listaArgumentow.add(scanner.nextBoolean());
		}
		method.invoke(object, listaArgumentow.toArray());
	}

	public static void pokazKonstruktoryKlasy(Class klasa) {
		for (Constructor x : klasa.getDeclaredConstructors()) {
			System.out.println(x.toGenericString());
		}
	}

}
