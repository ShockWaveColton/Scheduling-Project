package engine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;


public class ThirdWindow extends JFrame implements ActionListener{
	JFrame TWindow = new JFrame();

	private JButton Return = new JButton("Return");
	JLabel Sem1 = new JLabel("Semester 1");
	JLabel Sem2 = new JLabel("Semester 2");
	

	
	private DefaultListModel InstructorListModelSem1 = new DefaultListModel();
	private JList InstructorListSem1 = new JList(InstructorListModelSem1);
	
	private DefaultListModel InstructorListModelSem2 = new DefaultListModel();
	private JList InstructorListSem2 = new JList(InstructorListModelSem2);
	
	private DefaultListModel ProgramListModelSem1 = new DefaultListModel();
	private JList ProgramListSem1 = new JList(ProgramListModelSem1);
	
	private DefaultListModel ProgramListModelSem2 = new DefaultListModel();
	private JList ProgramListSem2 = new JList(ProgramListModelSem2);
	


	ThirdWindow(){
			
		//Set position for items in third window
		TWindow.add(Sem1);
		Sem1.setBounds(50,0,500,50);
		
		TWindow.add(Sem2);
		Sem2.setBounds(350,0,500,50);
		
		TWindow.add(InstructorListSem1);
		InstructorListSem1.setBounds(25,50,150,300);
		
		TWindow.add(InstructorListSem2);
		InstructorListSem2.setBounds(325,50,150,300);
		
		TWindow.add(Return);
		Return.setBounds(50,350,100,50); //need to fix to be center 
		Return.addActionListener(this);
		
		TWindow.setSize(550, 500);
		TWindow.setLayout(null);
		TWindow.setVisible(false);
		
		

	}
		
		public void actionPerformed(ActionEvent e) { // click event to show or hide windows depending on the window selected.

		}
}