package lab4.struktury;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lab4.adnotacje.Opisz;
import lab4.adnotacje.Serializowalne;

@Serializowalne
public class Przejazd implements Serializable {
	String nazwa;
	private List<Osoba> listaOsob;
	int iloscMiejsc;

	public Przejazd() {
		listaOsob = new ArrayList<>();
	}
	
	public Przejazd(String nazwa, int iloscMiejsc) {
		this.nazwa = nazwa;
		this.iloscMiejsc = iloscMiejsc;
		listaOsob = new ArrayList<>();
	}
	
	
	public Przejazd(String nazwa, List<Osoba> listaOsob, int iloscMiejsc) {
		super();
		this.nazwa = nazwa;
		this.listaOsob = listaOsob;
		this.iloscMiejsc = iloscMiejsc;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}
	
	
	public List<Osoba> getListaOsob() {
		return listaOsob;
	}
	
	public int getIloscMiejsc() {
		return iloscMiejsc;
	}
	@Opisz(opisMetody = "ustaw ilosc miejsc", wyswietl = false)
	public void setIloscMiejsc(int iloscMiejsc) {
		this.iloscMiejsc = iloscMiejsc;
	}
	@Opisz(opisMetody = "Dodaj osobe jesli nie zostal przekroczony maksymalny limit", wyswietl = true)
	public boolean dodajOsobe(Osoba osoba) {
		if (iloscMiejsc > listaOsob.size()) {
			listaOsob.add(osoba);
			return true;
		} else {
			return false;
		}
	}
	@Opisz(opisMetody = "Usuwa osobe jesli istnieje", wyswietl = true)
	public boolean usunOsobe(Osoba osoba){
		return listaOsob.remove(osoba);
	}

	@Override
	public String toString() {
		return nazwa + ", max osob: " + iloscMiejsc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + iloscMiejsc;
		result = prime * result + ((listaOsob == null) ? 0 : listaOsob.hashCode());
		result = prime * result + ((nazwa == null) ? 0 : nazwa.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Przejazd other = (Przejazd) obj;
		if (iloscMiejsc != other.iloscMiejsc)
			return false;
		if (listaOsob == null) {
			if (other.listaOsob != null)
				return false;
		} else if (!listaOsob.equals(other.listaOsob))
			return false;
		if (nazwa == null) {
			if (other.nazwa != null)
				return false;
		} else if (!nazwa.equals(other.nazwa))
			return false;
		return true;
	}

	

}
