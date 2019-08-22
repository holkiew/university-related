package lab2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Customizer;
import java.beans.PropertyChangeListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JButton;

public class RownanieKwadratoweViewBeanCustomizer extends JTabbedPane implements Customizer {
	private JPanel panelRownania;
	private JPanel panelKolory;
	private JPanel panelFormat;
	private JPanel panelRozmiarOkna;
	private RownanieKwadratoweViewBean bean;
	private JSpinner spinnerIloscRK;
	private JTextPane textPaneFormat;
	private JSlider slider_szerokosc;
	private JSlider slider_wysokosc;
	private final static String POMARANCZOWY = "Pomaranczowy";
	private final static String CZARNY = "Czarny";
	private final static String ZIELONY = "Zielony";
	private final static String ZOLTY = "Zolty";
	private final static String SZARY = "Szary";

	public RownanieKwadratoweViewBeanCustomizer() {
		setSize(new Dimension(304, 132));
		initPanelRownania();
		initPanelKolory();
		initPanelRozmiarOkna();
		initPanelFormat();
	}

	private void initPanelRownania() {
		panelRownania = new JPanel();
		addTab("Rownania", null, panelRownania, null);
		panelRownania.setLayout(null);

		spinnerIloscRK = new JSpinner();
		spinnerIloscRK.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(bean != null){
					int temp = bean.getPoczatkowaIloscRownan();
					bean.setPoczatkowaIloscRownan((int)spinnerIloscRK.getValue());
					firePropertyChange("poczatkowaIloscRownan", temp, (int)spinnerIloscRK.getValue());
				}
			}
		});
		spinnerIloscRK.setModel(new SpinnerNumberModel(1, 0, 9, 1));
		spinnerIloscRK.setBounds(96, 11, 29, 20);
		panelRownania.add(spinnerIloscRK);

		JLabel lblIloscRownan = new JLabel("Ilosc rownan:");
		lblIloscRownan.setBounds(10, 14, 76, 14);
		panelRownania.add(lblIloscRownan);
	}

	private void initPanelKolory() {
		panelKolory = new JPanel();
		addTab("Kolor", null, panelKolory, null);
		panelKolory.setLayout(null);

		JLabel lblUstawKolor = new JLabel("Ustaw kolor tla");
		lblUstawKolor.setBounds(10, 12, 93, 14);
		panelKolory.add(lblUstawKolor);

		JPanel panel_podgladKoloru = new JPanel();
		panel_podgladKoloru.setBounds(113, 55, 97, 23);
		panelKolory.add(panel_podgladKoloru);

		JLabel lblPodglad = new JLabel("Podglad");
		lblPodglad.setBounds(10, 55, 77, 14);
		panelKolory.add(lblPodglad);

		JComboBox comboBoxKolor = new JComboBox();
		comboBoxKolor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color kolor = ustawPodgladKoloru((String) comboBoxKolor.getSelectedItem(), panel_podgladKoloru);
				if( bean != null){
					Color temp = bean.getKolor();
					bean.setKolor(kolor);
					firePropertyChange("kolor", kolor, temp);
				}
				
					
			}
		});
		comboBoxKolor.setBounds(113, 9, 97, 20);
		stworzListeKolorow(comboBoxKolor);
		ustawPodgladKoloru((String) comboBoxKolor.getSelectedItem(), panel_podgladKoloru);
		panelKolory.add(comboBoxKolor);
	}

	private void initPanelRozmiarOkna() {
		panelRozmiarOkna = new JPanel();
		addTab("Rozmiar okna", null, panelRozmiarOkna, null);
		panelRozmiarOkna.setLayout(null);

		JLabel lblSzerokosc = new JLabel("Szerokosc");
		lblSzerokosc.setBounds(10, 11, 80, 26);
		panelRozmiarOkna.add(lblSzerokosc);

		JLabel lblWysokosc = new JLabel("Wysokosc");
		lblWysokosc.setBounds(10, 55, 80, 26);
		panelRozmiarOkna.add(lblWysokosc);

		JLabel lblSzerokoscSlide = new JLabel("lblSzerokoscSlide");
		lblSzerokoscSlide.setBounds(243, 17, 46, 14);
		panelRozmiarOkna.add(lblSzerokoscSlide);

		JLabel lblWysokoscSlide = new JLabel("lblWysokoscSlide");
		lblWysokoscSlide.setBounds(243, 61, 46, 14);
		panelRozmiarOkna.add(lblWysokoscSlide);

		slider_szerokosc = new JSlider();
		slider_szerokosc.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				lblSzerokoscSlide.setText(Integer.valueOf(slider_szerokosc.getValue()).toString());
				if(bean != null){
					int temp = bean.getWielkoscOknaX();
					int temp_new =slider_szerokosc.getValue();
					bean.setWielkoscOknaX(temp_new);
					firePropertyChange("WielkoscOknaX", temp, temp_new);
				}
			}
		});
		slider_szerokosc.setMaximum(500);
		slider_szerokosc.setValue(250);
		slider_szerokosc.setBounds(89, 11, 149, 26);
		panelRozmiarOkna.add(slider_szerokosc);

		slider_wysokosc = new JSlider();
		slider_wysokosc.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				lblWysokoscSlide.setText(Integer.valueOf(slider_wysokosc.getValue()).toString());
				if(bean != null){
					int temp = bean.getWielkoscOknaY();
					int temp_new = slider_wysokosc.getValue();
					bean.setWielkoscOknaY(temp_new);
					firePropertyChange("WielkoscOknaX", temp, temp_new);
				}
			}
		});
		slider_wysokosc.setMaximum(500);
		slider_wysokosc.setValue(250);
		slider_wysokosc.setBounds(89, 55, 149, 26);
		panelRozmiarOkna.add(slider_wysokosc);

	}

	private void initPanelFormat() {
		panelFormat = new JPanel();
		addTab("Format", null, panelFormat, null);
		panelFormat.setLayout(null);

		textPaneFormat = new JTextPane();
		textPaneFormat.setBounds(10, 11, 279, 57);
		textPaneFormat.setText(RownanieKwadratoweViewBean.DEFAULT_FORMAT_PLIKU);
		panelFormat.add(textPaneFormat);
		
		JButton btnUstaw = new JButton("Ustaw");
		btnUstaw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(bean != null)
					bean.setFormatPliku(textPaneFormat.getText());
			}
		});
		btnUstaw.setBounds(104, 70, 89, 23);
		panelFormat.add(btnUstaw);
	}

	private void stworzListeKolorow(JComboBox lista) {
		lista.addItem(SZARY);
		lista.addItem(CZARNY);
		lista.addItem(ZOLTY);
		lista.addItem(ZIELONY);
		lista.addItem(POMARANCZOWY);
	}

	private Color ustawPodgladKoloru(String text, JPanel panel) {

		switch (text) {
		case CZARNY:
			panel.setBackground(Color.black);
			return Color.black;
		case ZOLTY:
			panel.setBackground(Color.yellow);
			return Color.yellow;
		case ZIELONY:
			panel.setBackground(Color.green);
			return Color.green;
		case POMARANCZOWY:
			panel.setBackground(Color.orange);
			return Color.orange;
		case SZARY:
			panel.setBackground(Color.lightGray);
			return Color.lightGray;
		default:
			panel.setBackground(Color.lightGray);
			return Color.lightGray;
		}
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener e) {

	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener e) {

	}

	@Override
	public void setObject(Object obj) {
		bean = (RownanieKwadratoweViewBean) obj;
		if(bean != null){
			spinnerIloscRK.setValue(bean.getPoczatkowaIloscRownan());
			textPaneFormat.setText(bean.getFormatPliku());
			slider_szerokosc.setValue(bean.getWielkoscOknaX());
			slider_wysokosc.setValue(bean.getWielkoscOknaY());
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame frame = new JFrame();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setBounds(100, 100, 500, 500);
					frame.setVisible(true);
					frame.setContentPane(new RownanieKwadratoweViewBeanCustomizer());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
