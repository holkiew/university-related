package lab4.obslugaAdnotacji;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import lab4.adnotacje.Opisz;

public class Opisywator {

	public static String getOpisMetod(Object object){
		String string = new String();
		for(Method method :object.getClass().getMethods()){
			Opisz annotation =  method.getDeclaredAnnotation(Opisz.class);
			if(annotation != null && annotation.wyswietl()){
				string += method.getName()+": " + annotation.opisMetody()+"\n";
			}
		}
		return string;
	}
	
	public static String getOpisMetod(Class klasa){
		String string = new String();
		Object object;
		try {
			object = klasa.newInstance();
			for(Method method :object.getClass().getMethods()){
				Opisz annotation =  method.getDeclaredAnnotation(Opisz.class);
				if(annotation != null && annotation.wyswietl()){
					string += method.getName()+": " + annotation.opisMetody()+"\n";
				}
			}
			if(string.equals(""))
				return "Brak adnotacji do wyswietlenia opisow";
			else
				return string;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
		
	}
}
