package lab6;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Aplikacja extends JFrame {

	private JPanel contentPane;
	public PanelGrafiki panelGrafiki;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private List<Double> bety;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.load("C:/Users/DZONI/EclipseWorkspace/JP2lab/lab6/bin/GeneratorObrazkow.dll");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Aplikacja frame = new Aplikacja();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Aplikacja() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		PanelGrafiki panelGrafiki = new PanelGrafiki();
		panelGrafiki.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelGrafiki.setBounds(30, 30, 300, 200);
		panelGrafiki.inicjalizuj(PanelGrafiki.ZAKRES_KOLOROW);
		contentPane.add(panelGrafiki);

		JButton btnResetuj = new JButton("Resetuj");
		btnResetuj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panelGrafiki.ustawLosoweKolory(PanelGrafiki.ZAKRES_KOLOROW);
				panelGrafiki.repaint();
			}
		});
		btnResetuj.setBounds(335, 68, 89, 23);
		contentPane.add(btnResetuj);

		JButton btnMiksuj = new JButton("Miksuj");
		btnMiksuj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Double> temp = new ArrayList<Double>();
				try{
				temp.add(Double.parseDouble(textField.getText()));
				temp.add(Double.parseDouble(textField_1.getText()));
				temp.add(Double.parseDouble(textField_2.getText()));
				temp.add(Double.parseDouble(textField_3.getText()));
				
				for(Double v : temp){
					if(v<-1 || v>1){
						throw new NumberFormatException();
					}
				}
				bety = temp;
				}catch (NumberFormatException ex){
					System.out.println("Bledna wartosc: wartosci musza byc z zakresu <-1.00, 1.00>");
					return;
				}
				panelGrafiki.miksuj(bety);
				panelGrafiki.repaint();
			}
		});
		btnMiksuj.setBounds(335, 143, 89, 23);
		contentPane.add(btnMiksuj);
		
		JLabel lblB = new JLabel("B1:");
		lblB.setBounds(33, 11, 27, 14);
		contentPane.add(lblB);
		
		JLabel lblB_1 = new JLabel("B2:");
		lblB_1.setBounds(118, 11, 32, 14);
		contentPane.add(lblB_1);
		
		JLabel lblB_2 = new JLabel("B3:");
		lblB_2.setBounds(193, 11, 32, 14);
		contentPane.add(lblB_2);
		
		JLabel lblB_3 = new JLabel("B4:");
		lblB_3.setBounds(279, 11, 27, 14);
		contentPane.add(lblB_3);
		
		textField = new JTextField();
		textField.setText("0.3");
		textField.setBounds(63, 8, 45, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(138, 8, 45, 20);
		textField_1.setText("0.3");
		contentPane.add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(224, 8, 45, 20);
		textField_2.setText("0.3");
		contentPane.add(textField_2);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(305, 8, 45, 20);
		textField_3.setText("0.3");
		contentPane.add(textField_3);
	}

	private class PanelGrafiki extends JPanel implements Runnable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		Color kolorPiksela[][];
		boolean zainicjalizowane = false;
		public static final int ZAKRES_KOLOROW = 255;
		private Mikser mikser = new Mikser();

		public void inicjalizuj(int zakresKolorow) {
			if (!zainicjalizowane) {
				kolorPiksela = new Color[this.getWidth()][this.getHeight()];
				ustawLosoweKolory(zakresKolorow);
				// new Thread(this).start();
				zainicjalizowane = true;
			}
		}

		public void miksuj(List<Double> bety) {
			for (int x = 1; x < this.getWidth() - 1; x++) {
				for (int y = 1; y < this.getHeight() - 1; y++) {
					//System.out.print("("+x +","+y+")=");
					int kolor = mikser(wybierzTablice3na3(x, y),bety);
					kolorPiksela[x][y] = new Color(kolor, kolor, kolor);
				}
			}
		}

		private int[][] wybierzTablice3na3(int _x, int _y) {
			int tablica[][] = new int[3][3];
			for (int x = 0; x < 3; x++)
				for (int y = 0; y < 3; y++) {
					tablica[x][y] = kolorPiksela[x + _x - 1][y + _y - 1].getBlue();
				}
			return tablica;
		}

		private int mikser(int[][] tab,  List<Double> bety) {
			int[] tablica = stworzTablice1DBezSrodka(tab);
			int wynik = mikser.miksuj(tablica, Mikser.ZAKRES_KOLOROW, bety.stream().mapToDouble(i->i).toArray(), Math.random());
			//System.out.println(Arrays.toString(tablica)+" = "+wynik);
			return wynik;
		}

		private int[] stworzTablice1DBezSrodka(int tab[][]) {
			int[] tablica = new int[8];
			int z = 0;
			for (int x = 0; x < 3; x++)
				for (int y = 0; y < 3; y++) {
					if (!(x == 1 && y == 1)) {
						tablica[z++] = tab[x][y];
					}
				}
			return tablica;
		}

		public void ustawLosoweKolory(int zakresKolorow) {
			for (int x = 0; x < this.getWidth(); x++) {
				for (int y = 0; y < this.getHeight(); y++) {
					int kolor = (int) (Math.random() * zakresKolorow);
					kolorPiksela[x][y] = new Color(kolor, kolor, kolor);
				}
			}
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			for (int x = 0; x < this.getWidth(); x++) {
				for (int y = 0; y < this.getHeight(); y++) {
					g.setColor(kolorPiksela[x][y]);
					g.drawOval(x, y, 0, 0);
				}

			}
		}

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(100);
					ustawLosoweKolory(ZAKRES_KOLOROW);
					this.repaint();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}
}
