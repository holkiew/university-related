package lab6;

public class Mikser {
	public static final double[] DEFAULT_BETY = {0.3, 0.3, 0.3, 0.3};
	public static final int ZAKRES_KOLOROW = 255;
	public native int miksuj(int[] tablica, int x, double[] bety, double rand01);
}
