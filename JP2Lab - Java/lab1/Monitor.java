package lab1;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

//GUI-tylko wyswietla
public class Monitor extends JFrame implements IMonitor, Runnable {

	private JTextArea textArea;
	private ICentrala iCentrala;
	private int portCentrali;
	private boolean czyZakoncz;
	private Registry registryCentrali;

	public Monitor(int portCentrali) {
		super();
		init();
		this.portCentrali = portCentrali;
		czyZakoncz = false;

	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Podaj port");
		int port = Registry.REGISTRY_PORT + 20;//scanner.nextInt();
		System.out.println("Podaj nazwe monitora");
		String nazwa = "Monitor";//scanner.next();
		int portCentrali = Registry.REGISTRY_PORT;
		//-------------------------------



		Registry registry;
		Monitor m = new Monitor(portCentrali);
		Thread watekMonitora = new Thread(m);
		try {
			try {
				registry = LocateRegistry.createRegistry(port);
				System.out.println("java RMI registry created.");
			} catch (Exception e){
				System.out.println("Using existing registry");
				registry = LocateRegistry.getRegistry(port);
			}
			IMonitor iMonitor = (IMonitor) UnicastRemoteObject.exportObject(m, port);
			registry.bind(nazwa, iMonitor);
			System.out.println(iMonitor + "\nCreated and binded to name: " + nazwa +", port: "+ port);
			watekMonitora.start();
			watekMonitora.join();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void init() {
		setVisible(true);
		setSize(450, 450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		textArea = new JTextArea();
		textArea.setBounds(10, 11, 414, 239);
		textArea.setEditable(false);

		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setBounds(0, 0, 434, 261);
		add(scroll);
		textArea.setAutoscrolls(true);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				czyZakoncz = true;
				e.getWindow().dispose();
			}
		});

	}

	@Override
	public void loguj(String h) {
		try {
			textArea.setText("Nazwa centrali : " + registryCentrali.list()[0] + "\n");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		textArea.append(h);
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}

	@Override
	public void run() {
		while (!czyZakoncz) {
			try {
				Thread.sleep(3000);
				if (!(iCentrala == null)) {
					try {
						registryCentrali.lookup(registryCentrali.list()[0]);
					} catch (Exception e) {
						iCentrala = null;
						textArea.setText("Blad polaczenia, zgubiono centrale");
					}
				} else {
					try {
						registryCentrali = LocateRegistry.getRegistry(portCentrali);
						if (registryCentrali != null) {
							iCentrala = (ICentrala) registryCentrali.lookup(registryCentrali.list()[0]);
							textArea.setText("Znaleziono centrale: " + registryCentrali.list()[0] + "\n");
							if (iCentrala.rejestruj(this))
								textArea.append("Zarejestrowano w centrali: " + registryCentrali.list()[0] + "\n");
							else {
								textArea.setText("Blad przy rejestracji w centrali");
								registryCentrali = null;
							}
							Thread.sleep(2000);
						}
					} catch (Exception e) {
						textArea.setText("Brak centrali");
					}
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			iCentrala.wyrejestruj(this);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}


}
