package lab4.struktury;

import java.io.Serializable;

import lab4.adnotacje.Opisz;
import lab4.adnotacje.Serializowalne;

@Serializowalne
public class Osoba implements Serializable{
	private String nazwa;

	public Osoba(){};
	public Osoba(String nazwa) {
		this.nazwa = nazwa;
	}
	@Opisz(opisMetody = "metoda pobierajaca nazwe", wyswietl = true)
	public String getNazwa() {
		return nazwa;
	}
	@Opisz(opisMetody = "metoda dajaca nazwe", wyswietl = false)
	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}
	@Override
	public String toString() {
		return nazwa;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Osoba other = (Osoba) obj;
		if (nazwa == null) {
			if (other.nazwa != null)
				return false;
		} else if (!nazwa.equals(other.nazwa))
			return false;
		return true;
	}
	
	
	
}
