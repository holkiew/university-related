package lab2;

public enum OdleglosciMiedzyRownaniami {
	MALA("Mala", Stale.MALA_VALUE),
	SREDNIA("Srednia",Stale.SREDNIA_VALUE),
	DUZA("Duza",Stale.DUZA_VALUE);
	
	private final String nazwa;
	private final int wartosc;
	
	private static class Stale{
		private static final int MALA_VALUE = 30;
		private static final int SREDNIA_VALUE = 45;
		private static final int DUZA_VALUE = 60;
	}
	private OdleglosciMiedzyRownaniami(String nazwa, int wartosc){
		this.nazwa = nazwa;
		this.wartosc = wartosc;
	}
	public String getNazwa() {
		return nazwa;
	}
	public int getWartosc() {
		return wartosc;
	}
	
}
