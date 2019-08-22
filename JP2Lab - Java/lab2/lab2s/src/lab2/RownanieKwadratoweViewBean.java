package lab2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class RownanieKwadratoweViewBean extends JPanel  {
	public static final String DEFAULT_FORMAT_PLIKU = "A: {A}, B: {B}, C: {C} \n X1 = {X1}, X2 = {X2}, X0 = {X0}";

	private List<RKFieldStruct> listaRownan;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JSpinner spinnerIloscRK;
	private int poczatkowaIloscRownan = 1;
	private int odlegloscMiedzyRzedami = 30;
	private int wielkoscOknaX = 419, wielkoscOknaY = 147;
	private String formatPliku = DEFAULT_FORMAT_PLIKU;
	private Color kolor = Color.LIGHT_GRAY;

	public RownanieKwadratoweViewBean() {
		listaRownan = new ArrayList<>();
		setPreferredSize(new Dimension(453, 315));
		setLayout(null);
		setBorder(new EmptyBorder(5, 5, 5, 5));

		panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel.setBackground(kolor);

		scrollPane = new JScrollPane(panel);
		scrollPane.setAutoscrolls(true);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel.setAutoscrolls(true);
		scrollPane.setBounds(10, 88, wielkoscOknaX, wielkoscOknaY);
		scrollPane.setViewportView(panel);
		panel.setLayout(null);
		add(scrollPane);

		JButton btnOblicz = new JButton("Oblicz");
		btnOblicz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (RKFieldStruct rkFieldStruct : listaRownan) {
					rkFieldStruct.setResults();
				}
			}
		});
		btnOblicz.setBounds(10, 266, 89, 23);
		add(btnOblicz);

		spinnerIloscRK = new JSpinner();
		spinnerIloscRK.setModel(new SpinnerNumberModel(poczatkowaIloscRownan, 0, 9, 1));
		spinnerIloscRK.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ustawIloscWyswietlanychRownan((int) spinnerIloscRK.getValue());
				panel.repaint();
			}
		});
		spinnerIloscRK.setBounds(400, 11, 29, 20);
		add(spinnerIloscRK);

		JButton btnResetuj = new JButton("Resetuj");
		btnResetuj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ustawIloscWyswietlanychRownan(1);
				spinnerIloscRK.setValue(1);
				scrollPane.repaint();
				for (RKFieldStruct rkFieldStruct : listaRownan) {
					rkFieldStruct.setDefaultValues();
				}
			}
		});
		btnResetuj.setBounds(340, 266, 89, 23);
		add(btnResetuj);

		JLabel lbl_RKs = new JLabel("Ilosc rownan:");
		lbl_RKs.setBounds(318, 14, 81, 14);
		add(lbl_RKs);

		JButton btnZapisz = new JButton("Zapisz");
		btnZapisz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (RKFieldStruct rkFieldStruct : listaRownan) {
					ZapiszRownanieKwadratowe.zapiszPlik(formatPliku, rkFieldStruct.rownanieKwadratowe, null);
				}
			}
		});
		btnZapisz.setBounds(241, 266, 89, 23);
		add(btnZapisz);
		ustawIloscWyswietlanychRownan(1);

	}

	private void ustawIloscWyswietlanychRownan(int ilosc) {
		if (listaRownan.size() > ilosc) {
			int iloscUsuniec = listaRownan.size() - ilosc;
			for (int i = 0; i < iloscUsuniec; i++) {
				listaRownan.get(listaRownan.size() - 1).removeAll();
				listaRownan.remove(listaRownan.size() - 1);
			}

		} else if (listaRownan.size() < ilosc) {
			int iloscDodan = ilosc - listaRownan.size();
			for (int i = 0; i < iloscDodan; i++) {
				if (listaRownan.size() == 0)
					listaRownan.add(new RKFieldStruct(0, 0, panel));
				else
					listaRownan.add(new RKFieldStruct(0,
							listaRownan.get(listaRownan.size() - 1).y + odlegloscMiedzyRzedami, panel));
			}
		}
		panel.setPreferredSize(new Dimension(400, listaRownan.size() > 0 ?  listaRownan.get(listaRownan.size()-1).y + odlegloscMiedzyRzedami : 2*odlegloscMiedzyRzedami));
		panel.revalidate();
	}

	public int getPoczatkowaIloscRownan() {
		return poczatkowaIloscRownan;
	}

	public void setPoczatkowaIloscRownan(int poczatkowaIloscRownan) {
		this.poczatkowaIloscRownan = poczatkowaIloscRownan;
		this.spinnerIloscRK.setValue(poczatkowaIloscRownan);
		panel.repaint();
	}

	public int getOdlegloscMiedzyRzedami() {
		return odlegloscMiedzyRzedami;
	}

	public void setOdlegloscMiedzyRzedami(int odlegloscMiedzyRzedami) {
		this.odlegloscMiedzyRzedami = odlegloscMiedzyRzedami;
	}

	public Color getKolor() {
		return kolor;
	}

	public void setKolor(Color kolor) {
		this.kolor = kolor;
		panel.setBackground(kolor);
		repaint();
	}

	public String getFormatPliku() {
		return formatPliku;
	}

	public void setFormatPliku(String formatPliku) {
		this.formatPliku = formatPliku;
		
	}

	public int getWielkoscOknaX() {
		return wielkoscOknaX;
	}
	
	public void setWielkoscOknaX(int wielkoscOknaX) {
		this.wielkoscOknaX = wielkoscOknaX;
		//setPreferredSize(new Dimension(wielkoscOknaX, wielkoscOknaY));
		scrollPane.setSize(new Dimension(wielkoscOknaX, wielkoscOknaY));
		repaint();
	}

	public int getWielkoscOknaY() {
		return wielkoscOknaY;
	}

	public void setWielkoscOknaY(int wielkoscOknaY) {
		this.wielkoscOknaY = wielkoscOknaY;
		//setPreferredSize(new Dimension(wielkoscOknaX, wielkoscOknaY));
		scrollPane.setSize(new Dimension(wielkoscOknaX, wielkoscOknaY));
		repaint();
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame frame = new JFrame();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setBounds(100, 100, 500, 500);
					frame.setVisible(true);
					frame.setContentPane(new RownanieKwadratoweViewBean());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private class RKFieldStruct {
		private JTextField textField_A;
		private JTextField textField_B;
		private JTextField textField_C;
		private JTextField textField_X1;
		private JTextField textField_X2;
		private JTextField textField_X0;
		private JLabel lblA;
		private JLabel lblB;
		private JLabel lblC;
		private JLabel lbl_X0;
		private JLabel lblX_1;
		private JLabel lbl_X2;
		private RownanieKwadratowe rownanieKwadratowe;
		private int x;
		private int y;
		private JPanel panel;

		public RKFieldStruct(int x, int y, JPanel panel) {
			this.x = x;
			this.y = y;
			this.panel = panel;
			if (panel instanceof JPanel) {
				init();
				setDefaultValues();
			} else {
				throw new NullPointerException("Brak panelu");
			}
		}

		public void setResults() {
			double a = textField_A.getText() == null || textField_A.equals("") ? 0
					: Double.parseDouble(textField_A.getText().replace(",", "."));
			double b = textField_B.getText() == null || textField_B.equals("") ? 0
					: Double.parseDouble(textField_B.getText().replace(",", "."));
			double c = textField_C.getText() == null || textField_C.equals("") ? 0
					: Double.parseDouble(textField_C.getText().replace(",", "."));
			rownanieKwadratowe = new RownanieKwadratowe(a, b, c);
			Map<String, Double> temp = rownanieKwadratowe.oblicz();
			if (temp.get(RownanieKwadratowe.DELTA_UJEMNA) != null) {
				textField_X1.setText(RownanieKwadratowe.DELTA_UJEMNA);
				textField_X2.setText(RownanieKwadratowe.DELTA_UJEMNA);
				textField_X0.setText(RownanieKwadratowe.DELTA_UJEMNA);
			} else {
				textField_X1.setText(
						temp.get(RownanieKwadratowe.X1) != null ? temp.get(RownanieKwadratowe.X1).toString() : "");
				textField_X2.setText(
						temp.get(RownanieKwadratowe.X2) != null ? temp.get(RownanieKwadratowe.X2).toString() : "");
				textField_X0.setText(
						temp.get(RownanieKwadratowe.X0) != null ? temp.get(RownanieKwadratowe.X0).toString() : "");
			}
		}

		public void setDefaultValues() {
			textField_A.setText("0.00");
			textField_B.setText("0.00");
			textField_C.setText("0.00");
			textField_X1.setText("");
			textField_X2.setText("");
			textField_X0.setText("");
		}

		public void cleanUpValues() {
			textField_A.setText("");
			textField_B.setText("");
			textField_C.setText("");
			textField_X1.setText("");
			textField_X2.setText("");
			textField_X0.setText("");
		}

		private void init() {
			lblA = new JLabel("A:");
			lblA.setBounds(x + 11, y + 11, 16, 14);
			panel.add(lblA);

			textField_A = new JTextField();
			textField_A.setBounds(x + 21, y + 8, 35, 20);
			panel.add(textField_A);
			textField_A.setColumns(10);

			lblB = new JLabel("B:");
			lblB.setBounds(x + 64, y + 11, 16, 14);
			panel.add(lblB);

			textField_B = new JTextField();
			textField_B.setColumns(10);
			textField_B.setBounds(x + 77, y + 8, 35, 20);
			panel.add(textField_B);

			lblC = new JLabel("C:");
			lblC.setBounds(x + 120, y + 11, 16, 14);
			panel.add(lblC);

			textField_C = new JTextField();
			textField_C.setColumns(10);
			textField_C.setBounds(x + 133, y + 8, 35, 20);
			panel.add(textField_C);

			lblX_1 = new JLabel("X1:");
			lblX_1.setBounds(x + 205, y + 11, 25, 14);
			panel.add(lblX_1);

			textField_X1 = new JTextField();
			textField_X1.setEditable(false);
			textField_X1.setColumns(10);
			textField_X1.setBounds(x + 222, y + 8, 35, 20);
			panel.add(textField_X1);

			lbl_X2 = new JLabel("X2:");
			lbl_X2.setBounds(x + 267, y + 11, 25, 14);
			panel.add(lbl_X2);

			textField_X2 = new JTextField();
			textField_X2.setEditable(false);
			textField_X2.setColumns(10);
			textField_X2.setBounds(x + 286, y + 8, 35, 20);
			panel.add(textField_X2);

			lbl_X0 = new JLabel("X0:");
			lbl_X0.setBounds(x + 325, y + 11, 25, 14);
			panel.add(lbl_X0);

			textField_X0 = new JTextField();
			textField_X0.setEditable(false);
			textField_X0.setColumns(10);
			textField_X0.setBounds(x + 345, y + 8, 35, 20);
			panel.add(textField_X0);
		}

		public void removeAll() {
			panel.remove(textField_A);
			panel.remove(textField_B);
			panel.remove(textField_C);
			panel.remove(textField_X1);
			panel.remove(textField_X2);
			panel.remove(textField_X0);
			panel.remove(lblA);
			panel.remove(lblB);
			panel.remove(lblC);
			panel.remove(lblX_1);
			panel.remove(lbl_X2);
			panel.remove(lbl_X0);

		}

	}
}
