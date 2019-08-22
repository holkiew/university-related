package lab1;

import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Tablica implements ITablica, Serializable, Runnable {
	
	private String nazwa;
	private List<Haslo> hasla;
	private boolean czyDziala;
	private boolean czyZakoncz;

	private ICentrala iCentrala;
	private int portCentrali;
	private Registry registryCentrali;

	public Tablica(String n, int portCentrali) {
		super();
		this.nazwa = n;
		hasla = new ArrayList<Haslo>();
		czyDziala = false;
		czyZakoncz = false;
		this.portCentrali = portCentrali;
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Podaj port");

		int port = Registry.REGISTRY_PORT + 10;//scanner.nextInt();
		int portCentrali = Registry.REGISTRY_PORT;
		System.out.println("Podaj nazwe tablicy");
		String nazwa = "Tablica"; //scanner.next();
		//-------------------------------

		Registry registry;
		Tablica t = new Tablica(nazwa, portCentrali);
		Thread watekTablicy = new Thread(t);
		try {
			try {
				registry = LocateRegistry.createRegistry(port);
				System.out.println("java RMI registry-tablica created.");
			} catch (Exception e) {
				registry = LocateRegistry.getRegistry(port);
				System.out.println("Using existing registry-tablica");
			}
			ITablica iTablica = (ITablica) UnicastRemoteObject.exportObject(t, port);
			registry.bind(nazwa, iTablica);    //optional
			System.out.println(iTablica + "\nCreated and binded to name: " + iTablica.getNazwa() + ", port: " + port);
			watekTablicy.start();
			watekTablicy.join();
			UnicastRemoteObject.unexportObject(t, true);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean ustaw(Haslo h) throws RemoteException {
		return hasla.add(h);
	}

	@Override
	public List<Haslo> pobierz() throws RemoteException {
		if(czyDziala)
			return hasla;
		else
			return Collections.emptyList();
	}

	@Override
	public boolean kasuj(Haslo h) throws RemoteException {
		Iterator<Haslo> it = hasla.iterator();
		while(it.hasNext())
		{
			if(it.next().equals(h))
			{
				it.remove();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean wyczysc() throws RemoteException {
		if(!hasla.isEmpty())
		{
			hasla.clear();
			return true;
		}
		return false;
	}

	@Override
	public boolean start() throws RemoteException {
		synchronized (this) {
			if (iCentrala == null) {
				return false;
			}
			if (czyDziala)
				return false;
			czyDziala = !czyDziala;
			notify();
			return true;
		}
	}

	@Override
	public boolean stop() throws RemoteException {

		if (!czyZakoncz) {
			try {
				iCentrala.wyrejestruj(this);
			} catch (NullPointerException e) {
				System.out.println("Brak przypisanej centrali");
			}
			czyDziala = true;
			czyZakoncz = true;
				return true;
		}
		return false;
	}

	@Override
	public String getNazwa() throws RemoteException {
		return nazwa;
	}

	@Override
	public void setNazwa(String n) throws RemoteException {
		this.nazwa = n;
	}

	@Override
	public void run() {
		while (!czyZakoncz) {
			synchronized (this){
				try {
					Thread.sleep(3000);
					if (!(iCentrala == null)) {
						if (!czyDziala) {
							System.out.println("Czekam na start");
							wait(1000);
						}
						hasla.forEach(h -> System.out.println("Aktualna lista hasel: \n" + h.toString()));
						try {
							registryCentrali.lookup(registryCentrali.list()[0]);
						} catch (Exception e) {
							System.out.println("Blad polaczenia z centrala");
							iCentrala = null;
						}
					} else {
						try {
							registryCentrali = LocateRegistry.getRegistry(portCentrali);
							if (registryCentrali != null) {
								iCentrala = (ICentrala) registryCentrali.lookup(registryCentrali.list()[0]);
								System.out.println("Znaleziono centrale: " + registryCentrali.list()[0]);
								iCentrala.rejestruj(this);
								System.out.println("Zarejestrowano w centrali: " + registryCentrali.list()[0]);
							}
						} catch (Exception e) {
							System.out.println("Brak centrali");
							czyDziala = false;
							iCentrala = null;
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Koncze watek");
	}
}
