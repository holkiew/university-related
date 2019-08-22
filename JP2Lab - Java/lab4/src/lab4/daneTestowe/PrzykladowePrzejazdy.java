package lab4.daneTestowe;

import lab4.struktury.Przejazd;

public enum PrzykladowePrzejazdy {
	PRZEJAZD1(new Przejazd("przejazd1",1)),
	PRZEJAZD2(new Przejazd("przejazd2",2)),
	PRZEJAZD3(new Przejazd("przejazd3",3)),
	PRZEJAZD4(new Przejazd("ZAKON",666)),
	PRZEJAZD5(new Przejazd("squad",0));
	
	private Przejazd przejazd;
	
	private PrzykladowePrzejazdy(Przejazd przejazd) {
		this.przejazd = przejazd;
	}
	public Przejazd getPrzejazd() {
		return przejazd;
	}

	
}
