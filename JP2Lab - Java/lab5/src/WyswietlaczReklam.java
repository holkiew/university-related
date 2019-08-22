import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class WyswietlaczReklam extends JFrame {

	private JPanel contentPane;
	public ObslugaLampek obslugaLampek;
	public JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Reklama reklama = new Reklama("Tekst reklamy xxx");
					for(Lampki lampka: Lampki.values())
						reklama.getLampki().add(lampka.getLampka());
					
					WyswietlaczReklam frame = new WyswietlaczReklam(reklama);
					frame.setVisible(true);
					new Thread(frame.obslugaLampek).start();
					new Thread(new ZmieniaczPolaTekstowego(frame.textArea, reklama)).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public WyswietlaczReklam(Reklama reklama) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 397, 463);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textArea = new JTextArea();
		textArea.setEnabled(false);
		textArea.setEditable(false);
		textArea.setBounds(155, 29, 216, 384);
		contentPane.add(textArea);
		
		obslugaLampek = new ObslugaLampek(reklama, contentPane);
	}

}
