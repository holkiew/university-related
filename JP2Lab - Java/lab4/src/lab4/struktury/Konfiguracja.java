package lab4.struktury;

import java.io.Serializable;
import java.util.List;

import lab4.adnotacje.Serializowalne;
@Serializowalne
public class Konfiguracja implements Serializable{
	public List<Przejazd> listPrzejazdow;
	public List<Osoba> listWolnychOsob;
}
