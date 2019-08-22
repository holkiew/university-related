package lab3;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Scanner;

public class LadowaczKlas {
	private static final String MENU_OPENER = "1. Wywolaj metode zaladowanej klasy\n"
			+ "2. Wyswietl pola zaladowanej klasy\n" + "3. Wyswietl konstruktory zaladowanej klasy\n"
			+ "4. Zaladuj klase\n"
			+ "5. Zwolnij klase\n";

	public static void main(String[] args) {
		CustomClassLoader CCL = new CustomClassLoader(LadowaczKlas.class.getClassLoader());
		Scanner scanner = new Scanner(System.in);
		int plik = -1;
		String pakiet = null;
		File[] pliki = null;
		Object object = null;
		Method method = null;
		Class klasa = null;
		try {
			boolean menu = true;
			int wybor = 0;
			while (menu) {
				System.out.println("\n\n\n\n");
				System.out.println(
						"########################################################################################");
				System.out.println("AKTUALNY STAN \nClass: " + klasa + "\nObject: " + object + "\nMethod: " + method);
				System.out.println(
						"########################################################################################");
				System.out.println(MENU_OPENER);
				wybor = scanner.nextInt();
				switch (wybor) {
				case 1:
					method = Utils.stworzMetode(klasa, Utils.wybierzMetodyKlasy(klasa));
					Utils.wywolajMetode(object, method);
					break;
				case 2:
					System.out.println("Pola: ");
					Utils.pokazPolaKlasy(klasa);
					break;
				case 3:
					System.out.println("Konstruktory: ");
					Utils.pokazKonstruktoryKlasy(klasa);
					break;
				case 4:
					while (plik < 0) {
						System.out.println("Dostepne pakiety :");
						for (File file : CCL.wylistujZasobyWPakiecie("")) {
							System.out.println(file.getName());
						}
						System.out.println("Wpisz nazwe pakietu :");
						pakiet = scanner.next();

						System.out.println("Klasy dostepne w pakiecie:");
						pliki = CCL.wylistujZasobyWPakiecie(pakiet);
						plik = Utils.wybierzPlik(pliki);
					}
					klasa = CCL.loadClass(pakiet + "." + pliki[plik].getName().toString().split(".class")[0]);
					object = klasa.newInstance();
					plik = -1;
					break;
				case 5:
					object = null;
					method = null;
					klasa = null;
					triggerGC();
					break;
				default:
					menu = false;
				}
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
	}
	
	private static void triggerGC() throws InterruptedException {
        System.out.println("\n-- Starting GC");
        System.gc();
        Thread.sleep(100);
        System.out.println("-- End of GC\n");
    }
}
