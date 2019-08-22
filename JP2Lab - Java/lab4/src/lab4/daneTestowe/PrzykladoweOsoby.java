package lab4.daneTestowe;

import lab4.struktury.Osoba;

public enum PrzykladoweOsoby {
	OSOBA1(new Osoba("toemk")),
	OSOBA2(new Osoba("janusz")),
	OSOBA3(new Osoba("gruby")),
	OSOBA4(new Osoba("ZAKON")),
	OSOBA31(new Osoba("ZAKON")),
	OSOBA213(new Osoba("ZAKON"));
	
	private Osoba osoba;
	
	private PrzykladoweOsoby(Osoba osoba) {
		this.osoba = osoba;
	}

	public Osoba getOsoba() {
		return osoba;
	}

	
}
