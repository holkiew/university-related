package lab1;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

//zarzadza
public class Centrala implements ICentrala, Runnable{
	private List<ITablica> listaTablic;
	private List<IMonitor> listaMonitorow;

    public Centrala() {
        this.listaMonitorow = new ArrayList();
		this.listaTablic = new ArrayList();

	}

    public static void main(String[] args) {

        //wejscie konsoli
        //scanner.nextInt();
        String nazwa = "Centrala";
        int port = Registry.REGISTRY_PORT;
        //-------------------------------

        Registry registry;
        Centrala centrala;
        Thread watekCentrali;
        try {
            centrala = new Centrala();
            registry = LocateRegistry.createRegistry(port);
            ICentrala iCentrala = (ICentrala) UnicastRemoteObject.exportObject(centrala, port);
            registry.bind(nazwa, iCentrala);

            watekCentrali = new Thread(centrala);
            watekCentrali.start();
            centrala.menu();
            watekCentrali.join();

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

	@Override
	public boolean rejestruj(ITablica n) {
		for (Iterator<ITablica> iterator = listaTablic.iterator(); iterator.hasNext(); ) {
			if(iterator.next() == n) {
				return false;
			}
		}
		listaTablic.add(n);
		return true;
	}

	@Override
	public boolean wyrejestruj(ITablica n) {
		for (Iterator<ITablica> iterator = listaTablic.iterator(); iterator.hasNext(); ) {
			if(iterator.next() == n) {
				iterator.remove();
				return true;
			}
		}
        return false;
	}

	@Override
	public boolean rejestruj(IMonitor n) {
		for (Iterator<IMonitor> iterator = listaMonitorow.iterator(); iterator.hasNext(); ) {
			if(iterator.next() == n) {
				return false;
			}
		}
		listaMonitorow.add(n);
		return true;
	}

	@Override
	public boolean wyrejestruj(IMonitor n) {
		for (Iterator<IMonitor> iterator = listaMonitorow.iterator(); iterator.hasNext(); ) {
			if(iterator.next() == n) {
				iterator.remove();
				return true;
			}
		}
		return false;
	}

	public void menu(){
		Scanner scanner = new Scanner(System.in);
		int menu=0;
		int wybor,numer;
		String wiadomosc;
		synchronized (this) {
			try {
				while (true) {
					System.out.println("Wybierz opcje: \n" +
                            "1. Dodaj haslo do tablicy \n" +
                            "2. Usun haslo z tablicy \n" +
                            "3. Startuj tablice \n" +
                            "4. Zastopuj tablice \n");
                    menu = scanner.nextInt();
					switch (menu) {
						case 1:
							System.out.println("Dodaj haslo do tablicy nr: ");
							for (int i = 0; i < listaTablic.size(); i++) {
								System.out.println(i + ". " + listaTablic.get(i).getNazwa());
							}
							wybor = scanner.nextInt();
							if(wybor >= listaTablic.size() || wybor < 0)
								break;
							System.out.println("Podaj haslo");
							wiadomosc = scanner.next();
							System.out.println("Podaj numer");
							numer = scanner.nextInt();
							if(listaTablic.get(wybor).ustaw(new Haslo(wiadomosc, numer)))
								System.out.println("Operacja wykonana poprawnie");
							else
								System.out.println("Operacja niewykonana");
							break;
                        case 2:
                            System.out.println("Usun haslo z tablicy nr: ");
							for (int i = 0; i < listaTablic.size(); i++) {
								System.out.println(i + ". " + listaTablic.get(i).getNazwa());
							}
							wybor = scanner.nextInt();
							if(wybor >= listaTablic.size() || wybor < 0)
								break;
							System.out.println("Podaj haslo");
							wiadomosc = scanner.next();
							System.out.println("Podaj numer");
							numer = scanner.nextInt();
							if(listaTablic.get(wybor).kasuj(new Haslo(wiadomosc, numer)))
								System.out.println("Operacja wykonana poprawnie");
							else
								System.out.println("Operacja niewykonana");
							break;
                        case 3:
                            System.out.println("Startuj tablice nr: ");
							for (int i = 0; i < listaTablic.size(); i++) {
								System.out.println(i + ". " + listaTablic.get(i).getNazwa());
							}
							wybor = scanner.nextInt();
							if(wybor >= listaTablic.size() || wybor < 0)
								break;
							if(listaTablic.get(wybor).start())
								System.out.println("Operacja wykonana poprawnie");
							else
								System.out.println("Operacja niewykonana");
							break;
                        case 4:
                            System.out.println("Zastopuj tablice nr: ");
							for (int i = 0; i < listaTablic.size(); i++) {
								System.out.println(i + ". " + listaTablic.get(i).getNazwa());
							}
							wybor = scanner.nextInt();
							if(wybor >= listaTablic.size() || wybor < 0)
								break;
                            String nazwa = listaTablic.get(wybor).getNazwa();
                            if (listaTablic.get(wybor).stop()) {
                                for (IMonitor iMonitor : listaMonitorow) {
                                    iMonitor.loguj("Wyrejestrowano tablice: " + nazwa);
                                }
                                System.out.println("Operacja wykonana poprawnie");
                            } else
								System.out.println("Operacja niewykonana");
							break;
						default :
							System.out.println("Zly numer");
					}
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(3000);
				listaMonitorow.forEach(lM -> {
					String wiadomosc = new String();
					try {
					for (ITablica iTablica : listaTablic) {
						wiadomosc+= iTablica.getNazwa() + ", lista hasel: \n";
							for (Haslo haslo : iTablica.pobierz()) {
								wiadomosc+= haslo.toString()+"\n";
                            }
						}
					lM.loguj(wiadomosc);
					}catch (RemoteException e) {
						e.printStackTrace();
					}
				});

            } catch (InterruptedException e) {
                e.printStackTrace();
			}
		}
		}

}
