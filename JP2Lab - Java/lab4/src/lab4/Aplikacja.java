package lab4;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import lab4.daneTestowe.DanePlikow;
import lab4.daneTestowe.PrzykladoweOsoby;
import lab4.daneTestowe.PrzykladowePrzejazdy;
import lab4.obslugaAdnotacji.Opisywator;
import lab4.obslugaAdnotacji.Serializator;
import lab4.struktury.ArrayListSerializowana;
import lab4.struktury.Konfiguracja;
import lab4.struktury.Osoba;
import lab4.struktury.Przejazd;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JTextField;

public class Aplikacja extends JFrame {

	private JPanel contentPane;
	JList listaOsob;
	JList listaPrzejazdow;
	JList listaWolnychOsob;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	List<Przejazd> listPrzejazdow;
	List<Osoba> listWolnychOsob;
	DefaultListModel listModelPrzejazdow = new DefaultListModel();
	DefaultListModel listModelOsob = new DefaultListModel();
	DefaultListModel listModelWolnychOsob = new DefaultListModel();
	private JScrollPane scrollPane;
	private JTextField textFieldDaneKonfiguracji;
	private JButton btnWyswietlOpisyMetod;

	/**
	 * Create the frame.
	 */
	public Aplikacja() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 678, 371);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(432, 38, 136, 157);
		contentPane.add(scrollPane_2);

		listaWolnychOsob = new JList();
		scrollPane_2.setViewportView(listaWolnychOsob);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(245, 38, 136, 157);
		contentPane.add(scrollPane_1);

		listaOsob = new JList();
		scrollPane_1.setViewportView(listaOsob);

		JLabel lblListaPrzejazdow = new JLabel("Lista przejazdow");
		lblListaPrzejazdow.setBounds(50, 11, 143, 14);
		contentPane.add(lblListaPrzejazdow);

		JLabel lblLisaOsobW = new JLabel("Lisa osob w zaznaczonym przyjezdzie");
		lblLisaOsobW.setBounds(203, 11, 230, 14);
		contentPane.add(lblLisaOsobW);

		JLabel lblNieprzydzieloneOsoby = new JLabel("Nieprzydzielone osoby");
		lblNieprzydzieloneOsoby.setBounds(433, 11, 152, 14);
		contentPane.add(lblNieprzydzieloneOsoby);

		JButton btnDodajOsobe = new JButton("Dodaj osobe");
		btnDodajOsobe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Przejazd przejazd = (Przejazd) listaPrzejazdow.getSelectedValue();
				Osoba osoba = (Osoba)listaWolnychOsob.getSelectedValue();
				if(przejazd == null || osoba == null){
					return;
				}
				if(przejazd.dodajOsobe(osoba)){
					listModelWolnychOsob.removeElement(osoba);
					listWolnychOsob.remove(osoba);
					wyswietlListeOsobWPzejedzie();
				}	
				
			}
		});
		
		btnDodajOsobe.setBounds(443, 213, 113, 23);
		contentPane.add(btnDodajOsobe);

		JButton btnUsunOsobe = new JButton("Usun osobe");
		btnUsunOsobe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Przejazd przejazd = (Przejazd) listaPrzejazdow.getSelectedValue();
				Osoba osoba = (Osoba)listaOsob.getSelectedValue();
				
				if(przejazd == null || osoba == null){
					return;
				}
				
				if(przejazd.usunOsobe(osoba)){
					listModelOsob.removeElement(osoba);
					przejazd.usunOsobe(osoba);
					listModelWolnychOsob.addElement(osoba);
				}
				
			}
		});
		btnUsunOsobe.setBounds(259, 213, 113, 23);
		contentPane.add(btnUsunOsobe);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(36, 39, 166, 155);
		contentPane.add(scrollPane);

		listaPrzejazdow = new JList();
		listaPrzejazdow.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				wyswietlListeOsobWPzejedzie();
			}
		});
		scrollPane.setViewportView(listaPrzejazdow);
		
		listaWolnychOsob.setModel(listModelWolnychOsob);
		listaPrzejazdow.setModel(listModelPrzejazdow);
		listaOsob.setModel(listModelOsob);
		
		textFieldDaneKonfiguracji = new JTextField();
		textFieldDaneKonfiguracji.setToolTipText("wpisz nazwe pliku");
		textFieldDaneKonfiguracji.setBounds(23, 277, 136, 32);
		contentPane.add(textFieldDaneKonfiguracji);
		textFieldDaneKonfiguracji.setColumns(10);
		
		JButton btnZapiszKonfiguracje = new JButton("Zapisz konfiguracje");
		btnZapiszKonfiguracje.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String text = textFieldDaneKonfiguracji.getText();
				if(text!=null && !text.equals("")){
					zapiszDane(text);
				}
			}
		});
		btnZapiszKonfiguracje.setBounds(164, 269, 143, 23);
		contentPane.add(btnZapiszKonfiguracje);
		
		JButton btnWczytajKonfiguracje = new JButton("Wczytaj konfiguracje");
		btnWczytajKonfiguracje.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String text = textFieldDaneKonfiguracji.getText();
				if(text!=null && !text.equals("")){
					wczytajDane(text);
				}
			}
		});
		btnWczytajKonfiguracje.setBounds(164, 298, 143, 23);
		contentPane.add(btnWczytajKonfiguracje);
		
		btnWyswietlOpisyMetod = new JButton("Wyswietl opisy metod klas konfiguracyjnych");
		btnWyswietlOpisyMetod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(contentPane, wypiszOpisyMetod());
				
			}
		});
		btnWyswietlOpisyMetod.setBounds(372, 274, 253, 39);
		contentPane.add(btnWyswietlOpisyMetod);
	}

	public void wczytajDane(String nazwaPliku) {
		Konfiguracja konfiguracja = (Konfiguracja) Serializator.wczytajObiekt(nazwaPliku);
		listModelWolnychOsob.clear();
		listModelPrzejazdow.clear();
		listModelOsob.clear();
		
		listWolnychOsob = konfiguracja.listWolnychOsob;
		listPrzejazdow = konfiguracja.listPrzejazdow;
		listWolnychOsob.forEach(s -> listModelWolnychOsob.addElement(s));
		listPrzejazdow.forEach(s -> listModelPrzejazdow.addElement(s));
	}
	public void zapiszDane(String nazwaPliku){
		Konfiguracja konfiguracja = new Konfiguracja();
		konfiguracja.listPrzejazdow = listPrzejazdow;
		konfiguracja.listWolnychOsob = listWolnychOsob;
		Serializator.zapiszObiekt(nazwaPliku, konfiguracja);
	}
	
	private String wypiszOpisyMetod(){
		String string = new String();
		String listaDoOpisania[] = {"listPrzejazdow", "listWolnychOsob"};
		
		if(listPrzejazdow == null || listWolnychOsob == null){
			return null;
		}
		try {
			for(String pole : listaDoOpisania){
				Class listGenericType = zwrocTypGenerycznyListy(pole);
				String opisMetod = Opisywator.getOpisMetod(listGenericType);
				string += listPrzejazdow.getClass() + "<" + listGenericType +">\n" + opisMetod + "\n";
			}
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		}
		return string;
	}
	
	private Class zwrocTypGenerycznyListy(String nazwaPolaListy){
		Field field;
		try {
			field = this.getClass().getDeclaredField(nazwaPolaListy);
			ParameterizedType  listType = (ParameterizedType ) field.getGenericType();
			Class<?> listGenericType = (Class<?>) listType.getActualTypeArguments()[0];
			return listGenericType;
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void wyswietlListeOsobWPzejedzie(){
		listModelOsob.clear();
		Przejazd przejazd = (Przejazd)listaPrzejazdow.getSelectedValue();
		if(przejazd == null){
			return;
		}
		przejazd.getListaOsob().forEach(s -> listModelOsob.addElement(s));
	}

	/**
	 * Launch the application.
	 */

	private static void zapiszPrzykladoweDane() {
		List<Osoba> osoby = new ArrayListSerializowana<>();
		Konfiguracja konfiguracja = new Konfiguracja();
		for (PrzykladoweOsoby value : PrzykladoweOsoby.values()) {
			osoby.add(value.getOsoba());
		}
		konfiguracja.listWolnychOsob = osoby;
		
		List<Przejazd> przejazdy = new ArrayListSerializowana<>();
		for (PrzykladowePrzejazdy value : PrzykladowePrzejazdy.values()) {
			przejazdy.add(value.getPrzejazd());
		}
		konfiguracja.listPrzejazdow = przejazdy;
		Serializator.zapiszObiekt(DanePlikow.NAZWA_PLIKU_DOMYSLNEJ_KONFIGURACJI.getNazwa(), konfiguracja);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Aplikacja frame = new Aplikacja();
					frame.setVisible(true);
					zapiszPrzykladoweDane();
					frame.wczytajDane(DanePlikow.NAZWA_PLIKU_DOMYSLNEJ_KONFIGURACJI.getNazwa());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
