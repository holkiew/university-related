package lab2t;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import ziarenka.ChartBean;
import java.awt.Color;
import lab2.RownanieKwadratoweViewBean;
import java.awt.Dimension;
import java.awt.Rectangle;

public class testView {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testView window = new testView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public testView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 683, 522);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 657, 472);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		ChartBean chartBean = new ChartBean();
		chartBean.setValues(new Double[]{1.0,2.0,3.0});
		chartBean.setTitle("dasdsa");
		chartBean.setBounds(521, 11, 136, 265);
		panel.add(chartBean);
		
		RownanieKwadratoweViewBean rownanieKwadratoweViewBean = new RownanieKwadratoweViewBean();
		rownanieKwadratoweViewBean.setWielkoscOknaX(413);
		rownanieKwadratoweViewBean.setSize(new Dimension(146, 320));
		rownanieKwadratoweViewBean.setPreferredSize(new Dimension(146, 320));
		rownanieKwadratoweViewBean.setBounds(new Rectangle(34, 35, 146, 150));
		rownanieKwadratoweViewBean.setBounds(36, 38, 430, 306);
		panel.add(rownanieKwadratoweViewBean);
		
	}
}
