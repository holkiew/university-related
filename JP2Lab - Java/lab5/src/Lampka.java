public class Lampka {
	private String nazwa;
	private boolean czySwieci;
	
	public Lampka(){
		czySwieci = false;
		nazwa = "default";
	}
	
	public Lampka(String nazwa, boolean czySwieci) {
		super();
		this.czySwieci = czySwieci;
		this.nazwa = nazwa; 
	}

	public boolean isCzySwieci() {
		return czySwieci;
	}
	public void setCzySwieci(boolean czySwieci) {
		this.czySwieci = czySwieci;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	@Override
	public String toString() {
		return "Lampka [czySwieci=" + czySwieci + "]";
	}
	
}
