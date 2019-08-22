package lab4.obslugaAdnotacji;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializator {

	public static void zapiszObiekt(String nazwa, Object object){
		try {
			sprawdzCzyObiektJestSerializowalny(object);
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(nazwa));
			output.writeObject(object);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NieSerializowalneException e) {
			e.printStackTrace();
		} 
	}
	
	public static Object wczytajObiekt(String nazwa){
		try {
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(nazwa));
			Object obj = input.readObject();
			input.close();
			sprawdzCzyObiektJestSerializowalny(obj);
			return obj;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (NieSerializowalneException e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	private static void sprawdzCzyObiektJestSerializowalny(Object object) throws NieSerializowalneException{
		if(object.getClass().getAnnotation(lab4.adnotacje.Serializowalne.class) == null){
			throw new NieSerializowalneException("Nie serializowalny obiekt");
		} 
	}	
	
}
