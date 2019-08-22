package lab2;

import java.util.HashMap;
import java.util.Map;

public class RownanieKwadratowe {
	private double a,b,c;
	public final static String X1 = "X1";
	public final static String X2 = "X2";
	public final static String X0 = "X0";
	public final static String A = "A";
	public final static String B = "B";
	public final static String C = "C";
	public final static String DELTA_UJEMNA = "DELTA_UJEMNA";
	
	public RownanieKwadratowe() {
		this.a=0;
		this.b=0;
		this.c=0;
	}
	public RownanieKwadratowe(double a, double  b, double c) {
		this.a=a;
		this.b=b;
		this.c=c;
	}
	public Map<String, Double> oblicz(){
		Map<String, Double> temp = new HashMap<String, Double>();
		double delta = Math.pow(b, 2)-4*a*c;
		temp.put(A, a);
		temp.put(B, b);
		temp.put(C, c);
		if(delta > 0){
			temp.put(X1, new Double( (-b-Math.sqrt(delta))/2*a) );
			temp.put(X2, new Double( (-b+Math.sqrt(delta))/2*a) );
		} else if(delta == 0){
			temp.put(X0, new Double( -b/2*a ) );
		}else{
			temp.put(DELTA_UJEMNA, delta );
		}
		return temp;
	}
	
	public double getA() {
		return a;
	}
	public void setA(double a) {
		this.a = a;
	}
	public double getB() {
		return b;
	}
	public void setB(double b) {
		this.b = b;
	}
	public double getC() {
		return c;
	}
	public void setC(double c) {
		this.c = c;
	}
	@Override
	public String toString() {
		return "RownanieKwadratowe [A=" + a + ", B=" + b + ", C=" + c + "]";
	}
	
}
