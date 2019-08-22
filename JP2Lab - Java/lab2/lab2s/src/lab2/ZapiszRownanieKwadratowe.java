package lab2;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class ZapiszRownanieKwadratowe {
	
	private static PrintWriter writer;
	/**
	 * @author DZONI
	 * nazwaPliku == null to defaultowa (RownanieKwadratowe.toString() 
	 */
	public static void zapiszPlik(String format, RownanieKwadratowe rownanieKwadratowe, String nazwaPliku){
		if(rownanieKwadratowe == null)
			return;
		try {
			writer = new PrintWriter(nazwaPliku == null || nazwaPliku.equals("") ? rownanieKwadratowe.toString()+".txt" : nazwaPliku, "UTF-8");
			writer.print(formatuj(format, rownanieKwadratowe));
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	private static String formatuj(String format, RownanieKwadratowe rownanieKwadratowe){
		if(format == null || format.equals(""))
			return rownanieKwadratowe.toString();
		Map<String, Double> wyniki = rownanieKwadratowe.oblicz();
		format = format.replaceAll("\\{A\\}", wyniki.get(RownanieKwadratowe.A).toString());
		format = format.replaceAll("\\{B\\}", wyniki.get(RownanieKwadratowe.B).toString());
		format = format.replaceAll("\\{C\\}", wyniki.get(RownanieKwadratowe.C).toString());
		format = format.replaceAll("\\{X0\\}", wyniki.get(RownanieKwadratowe.X0) != null ? wyniki.get(RownanieKwadratowe.X0).toString() : "Nie istnieje");
		format = format.replaceAll("\\{X1\\}", wyniki.get(RownanieKwadratowe.X1) != null ? wyniki.get(RownanieKwadratowe.X1).toString() : "Nie istnieje");
		format = format.replaceAll("\\{X2\\}", wyniki.get(RownanieKwadratowe.X2) != null ? wyniki.get(RownanieKwadratowe.X2).toString() : "Nie istnieje");
		if(wyniki.get(RownanieKwadratowe.DELTA_UJEMNA) != null)
			format+="\n DELTA UJEMNA : "  + wyniki.get(RownanieKwadratowe.DELTA_UJEMNA);
		return format;
	}
}
