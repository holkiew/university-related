
import java.util.ArrayList;
import java.util.List;

public class Reklama {
	private List<Lampka> lampki = new ArrayList<Lampka>();
	private String ekran;
	
	public Reklama(String ekran){
		this.ekran = ekran;
	}
	public Reklama(List<Lampka> lampki, String ekran) {
		super();
		this.lampki = lampki;
		this.ekran = ekran;
	}
	public List<Lampka> getLampki() {
		return lampki;
	}
	public void setLampki(List<Lampka> lampki) {
		this.lampki = lampki;
	}
	public String getEkran() {
		return ekran;
	}
	public void setEkran(String ekran) {
		this.ekran = ekran;
	}
	
}
