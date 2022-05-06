package engine;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class SecondWindow {
	JFrame window = new JFrame();
	JLabel label = new JLabel("This needs to be designed");
		SecondWindow(){
			window.add(label);
			label.setBounds(0,0,100,50);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(635, 840);
		window.setLayout(null);
		window.setVisible(true);
	}
}