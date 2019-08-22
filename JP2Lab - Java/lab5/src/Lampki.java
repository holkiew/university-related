
public enum Lampki {
 
	LAMPKA1(new Lampka("lampka1",false)),
	LAMPKA2(new Lampka("lampka2",false)),
	LAMPKA3(new Lampka("lampka3",false)),
	LAMPKA4(new Lampka("lampka4",false)),
	LAMPKA5(new Lampka("lampka5",false)),
	LAMPKA6(new Lampka("lampka6",false)),
	LAMPKA7(new Lampka("lampka7",false));
	
	private Lampka lampka;
	private Lampki(Lampka lampka) {
		this.lampka = lampka;
	}

	public Lampka getLampka() {
		return lampka;
	}
	
}
