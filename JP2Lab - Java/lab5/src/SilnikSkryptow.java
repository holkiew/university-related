
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class SilnikSkryptow {
	private static ScriptEngine engine;
	private static Invocable invocable;
	private static boolean czyWczytanySkrypt = false;
	private static String wczytanySkrypt;
	
	static {
		init();
	}
	
	private static void init(){
		engine = new ScriptEngineManager().getEngineByName("nashorn");
		invocable = (Invocable) engine;
	}
	
	public static void wczytajSkrypt(String sciezka){
		try {
			engine.eval(new FileReader(sciezka));
			czyWczytanySkrypt=true;
		} catch (FileNotFoundException | ScriptException e) {
			e.printStackTrace();
			czyWczytanySkrypt=false;
		}
	}
	
	public static Object uzyjFunkcji(String nazwa, Object... params){
		if(czyWczytanySkrypt){
			Object result;
			try {
				result = invocable.invokeFunction(nazwa, params);
			} catch (NoSuchMethodException | ScriptException e) {
				e.printStackTrace();
				return null;
			}
			return result;
		}else{
			return null;
		}	
	}

	public static String getWczytanySkrypt() {
		if(czyWczytanySkrypt){
			return wczytanySkrypt;
		}
		else 
		{
			return "brak wczytanego skrytpu";
		}
	}
	
}
