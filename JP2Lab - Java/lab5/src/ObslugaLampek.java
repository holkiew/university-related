import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class ObslugaLampek implements Runnable {
		private List<JCheckBox> checkBoxy = new ArrayList<JCheckBox>();
		private List<Color> koloryBoxow = new ArrayList<Color>();
		private JPanel contentPane;
		private Reklama reklama;
		private int x = 24, y = 25, width = 97, height = 23;
		private int odlegloscPomiedzyCheckboxami = 30;
		private Color domyslnyKolor = new Color(240, 240, 240);
		
		private final static String FUNKCJAJS = "funkcje.js";

		public ObslugaLampek(Reklama reklama, JPanel contentPane) {
			SilnikSkryptow.wczytajSkrypt(FUNKCJAJS);
			this.contentPane = contentPane;
			this.reklama = reklama;
			List<Lampka> lampki= reklama.getLampki();
			for (int i = 0; i < lampki.size(); i++) {
				JCheckBox checkBox = new JCheckBox(lampki.get(i).getNazwa());
				checkBox.setSelected(lampki.get(i).isCzySwieci());
				checkBox.setEnabled(false);
				checkBox.setBounds(x, y + i * odlegloscPomiedzyCheckboxami, width, height);
				checkBox.setBackground(domyslnyKolor);
				checkBoxy.add(checkBox);
				contentPane.add(checkBox);
				koloryBoxow.add(domyslnyKolor);
			}
		}
		
		public void zmienStanLampek(){
			reklama.setLampki((List<Lampka>)SilnikSkryptow.uzyjFunkcji("zmienStanLampek", reklama));
			for(int i = 0; i< reklama.getLampki().size()-1; i++){
				ustawStanLampki(i);
			}
		}
		
		private void ustawStanLampki(int index){
				if(index < 0  || index > (checkBoxy.size()-1))
					return;
				if(reklama.getLampki().get(index).isCzySwieci()){
					checkBoxy.get(index).setBackground(koloryBoxow.get(index));
					checkBoxy.get(index).setSelected(true);
				}else
				{
					checkBoxy.get(index).setBackground(domyslnyKolor);
					checkBoxy.get(index).setSelected(false);
				}
		}
		private void zmieniajKolory(){
			int ktoryBox = (int)(Math.random()*(koloryBoxow.size()-1));
			int ktoryKolor = (int)(Math.random()*7);
			switch(ktoryKolor){
				case(0):
					koloryBoxow.set(ktoryBox,Color.black);
					break;
				case(1):
					koloryBoxow.set(ktoryBox,Color.blue);
					break;
				case(2):
					koloryBoxow.set(ktoryBox,Color.yellow);
					break;
				case(3):
					koloryBoxow.set(ktoryBox,Color.orange);
					break;
				case(4):
					koloryBoxow.set(ktoryBox,Color.red);
					break;
				case(5):
					koloryBoxow.set(ktoryBox,Color.cyan);
					break;
				case(6):
					koloryBoxow.set(ktoryBox,Color.pink);
					break;
				case(7):
					koloryBoxow.set(ktoryBox,Color.green);
					break;
			}
		}
		@Override
		public void run() {
			while(true){
				try {
					Thread.sleep(250);
					zmieniajKolory();
					zmienStanLampek();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}