package lab4.daneTestowe;

public enum DanePlikow {
	NAZWA_PLIKU_DOMYSLNEJ_KONFIGURACJI("domyslnaKonfiguracja");
	
	private String nazwa;
	
	private DanePlikow(String nazwa) {
		this.nazwa = nazwa;
	}

	public String getNazwa() {
		return nazwa;
	}

}
