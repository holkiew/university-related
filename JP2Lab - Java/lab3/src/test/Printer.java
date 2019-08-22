package test;
public class Printer {
	public int a;
	private double b;
	public String text;
	public Printer(int a, double b, String text){
		this.a = a;
		this.b= b;
		this.text = text;
	}
	public Printer(){};
	
	public void printInt(int a){
		System.out.println("printInt()" + a);
	}
	public void pintInt(int a, int b){
		System.out.println("printInt()" + a +" "+ b);
	}
	public void pintInt(){
		System.out.println("printInt()");
	}
	public void printA(){
		System.out.println("printA()" + this.a);
	}
	public void printDouble(double b){
		System.out.println("printDouble()" + b);
		
	}
	private void printB(){
		System.out.println("printB()" + this.b);
	}
	
	public void printString(String s){
		System.out.println("printString()" + s);
		
	}
	public void printText(){
		System.out.println("printText()" + this.text);
	}
	
}
