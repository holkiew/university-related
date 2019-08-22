package lab1;

import java.io.Serializable;

public class Haslo implements Serializable{
	private static final long serialVersionUID = -2869888931119295940L;
	public String h;
	public int t;
	
	public Haslo(String h, int t){
		super();
		this.h=h;
		this.t=t;
	}
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof Haslo))
		{
			return false;
		}
		Haslo temp = (Haslo)obj;
		if(temp.h.equals(this.h) && temp.t == this.t)
			return true;
		return false;
	}

	@Override
	public String toString() {
		return "Haslo{" +
				"h='" + h + '\'' +
				", t=" + t +
				'}';
	}
}
