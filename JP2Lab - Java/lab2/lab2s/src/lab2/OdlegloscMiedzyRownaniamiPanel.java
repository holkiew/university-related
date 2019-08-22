package lab2;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class OdlegloscMiedzyRownaniamiPanel extends JPanel {
	
	private OdlegloscMiedzyRownaniamiEditor bean;
	private JRadioButton rdbntS;
	private JRadioButton rdbntM;
	private JRadioButton rdbntL;
	
	public OdlegloscMiedzyRownaniamiPanel(OdlegloscMiedzyRownaniamiEditor bean) {
		this.bean=bean;
		init();
	}
	private void init(){
		JRadioButton rdbntS = new JRadioButton(OdleglosciMiedzyRownaniami.MALA.getNazwa());
		rdbntS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bean.setAsText(OdleglosciMiedzyRownaniami.MALA.getNazwa());
			}
		});
		JRadioButton rdbntM = new JRadioButton(OdleglosciMiedzyRownaniami.SREDNIA.getNazwa());
		rdbntM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bean.setAsText(OdleglosciMiedzyRownaniami.SREDNIA.getNazwa());
			}
		});
		JRadioButton rdbntL = new JRadioButton(OdleglosciMiedzyRownaniami.DUZA.getNazwa());
		rdbntL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bean.setAsText(OdleglosciMiedzyRownaniami.DUZA.getNazwa());
			}
		});
		ButtonGroup bG = new ButtonGroup();
		bG.add(rdbntS);
		bG.add(rdbntM);
		bG.add(rdbntL);
		add(rdbntS);
		add(rdbntM);
		add(rdbntL);
		switch (bean.getAsText()){
			case "Mala":
				rdbntM.setSelected(true);
				break;
			case "Srednia":
				rdbntS.setSelected(true);
				break;
			case "Duza":
				rdbntL.setSelected(true);
				break;
			default:
				rdbntM.setSelected(true);
				break;
		}
	}
}
