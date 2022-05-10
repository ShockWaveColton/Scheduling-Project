package engine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;

import objects.Instructor;
import objects.Program;
import objects.Report;

public class SecondWindow extends JFrame implements ActionListener{
	JFrame SWindow = new JFrame();
	JFrame InstructorWindow = new JFrame();
	JFrame ProgramWindow = new JFrame();
	
	//Main Window items
	JLabel Prompt = new JLabel("Would you like a program report or an instructor report");
	private JButton program = new JButton("Program");
	private JButton instructor = new JButton("Instructor");
	
	//Report Choice Window items
	JLabel Sem1 = new JLabel("Semester 1");
	JLabel Sem2 = new JLabel("Semester 2");
	
	JLabel Seme1 = new JLabel("Semester 1");
	JLabel Seme2 = new JLabel("Semester 2");
	
	private DefaultListModel InstructorListModelSem1 = new DefaultListModel();
	private JList InstructorListSem1 = new JList(InstructorListModelSem1);
	
	private DefaultListModel InstructorListModelSem2 = new DefaultListModel();
	private JList InstructorListSem2 = new JList(InstructorListModelSem2);
	
	private DefaultListModel ProgramListModelSem1 = new DefaultListModel();
	private JList ProgramListSem1 = new JList(ProgramListModelSem1);
	
	private DefaultListModel ProgramListModelSem2 = new DefaultListModel();
	private JList ProgramListSem2 = new JList(ProgramListModelSem2);
	
	private JButton Return = new JButton("Return");
	private JButton Return2 = new JButton("Return");
	
	
	
	//**ListModels require filling and setting**
	
	
	public void fillTheList() {
		for(Instructor instructor : ObjectManager.getInstructors()) {
			Report report1 = new Report(1);
			Report report2 = new Report(2);
			
			report1.getHoursForInstructor(instructor);
			report2.getHoursForInstructor(instructor);
			
			
			InstructorListModelSem1.addElement(instructor.getFullName() + ": " + (report1.getHoursForInstructor(instructor) + report2.getHoursForInstructor(instructor)));
			
		}
		
		for(Instructor instructor : ObjectManager.getInstructors()) {
			Report report3 = new Report(3);
			Report report4 = new Report(4);
			
			report3.getHoursForInstructor(instructor);
			report4.getHoursForInstructor(instructor);
			
			
			InstructorListModelSem2.addElement(instructor.getFullName() + ": " + (report3.getHoursForInstructor(instructor) + report4.getHoursForInstructor(instructor)));
			
		}
		
		
		for(Program program : ObjectManager.getPrograms()) {
			Report report1 = new Report(1);
			Report report2 = new Report(2);
			
			report1.getHoursForProgram(program);
			report2.getHoursForProgram(program);
			
			
			ProgramListModelSem1.addElement(program.getName() + ": " + (report1.getHoursForProgram(program) + report2.getHoursForProgram(program)));
			
		}
		
		for(Program program : ObjectManager.getPrograms()) {
			Report report3 = new Report(3);
			Report report4 = new Report(4);
			
			report3.getHoursForProgram(program);
			report4.getHoursForProgram(program);
			
			
			ProgramListModelSem2.addElement(program.getName() + ": " + (report3.getHoursForProgram(program) + report4.getHoursForProgram(program)));
			
		}
		
	}
	
	
		SecondWindow(){
			
		fillTheList();
			
		SWindow.add(Prompt);
		Prompt.setBounds(75,0,500,50);
		
		SWindow.add(program);
		program.setBounds(300,75,100,50);
		program.addActionListener(this);
		
		SWindow.add(instructor);
		instructor.setBounds(50,75,100,50);
		instructor.addActionListener(this);
		
		SWindow.setSize(500, 200);
		SWindow.setLayout(null);
		SWindow.setVisible(true);
		
		//Instructor
		InstructorWindow.add(Sem1);
		Sem1.setBounds(50,0,500,50);
		
		InstructorWindow.add(Sem2);
		Sem2.setBounds(350,0,500,50);
		
		InstructorWindow.add(InstructorListSem1);
		InstructorListSem1.setBounds(25,50,150,300);
		
		InstructorWindow.add(InstructorListSem2);
		InstructorListSem2.setBounds(325,50,150,300);
		
		InstructorWindow.add(Return);
		Return.setBounds(200,350,100,50);
		Return.addActionListener(this);
		
		InstructorWindow.setSize(550, 500);
		InstructorWindow.setLayout(null);
		InstructorWindow.setVisible(false);
		
		//Program
		ProgramWindow.add(Seme1);
		Seme1.setBounds(50,0,500,50);
		
		ProgramWindow.add(Seme2);
		Seme2.setBounds(350,0,500,50);
		
		ProgramWindow.add(ProgramListSem1);
		ProgramListSem1.setBounds(25,50,150,300);
		
		ProgramWindow.add(ProgramListSem2);
		ProgramListSem2.setBounds(325,50,150,300);
		
		ProgramWindow.add(Return2);
		Return2.setBounds(200,350,100,50);
		Return2.addActionListener(this);
		
		ProgramWindow.setSize(550, 500);
		ProgramWindow.setLayout(null);
		ProgramWindow.setVisible(false);
	
	}
		
		public void actionPerformed(ActionEvent e) { // click event to show or hide windows depending on the window selected.
			if (e.getSource()==program) {
				ProgramWindow.setSize(550, 500);
				ProgramWindow.setLayout(null);
				ProgramWindow.setVisible(true);
				SWindow.setVisible(false);
			}
			
			if (e.getSource()==instructor) {
				InstructorWindow.setSize(550, 500);
				InstructorWindow.setLayout(null);
				InstructorWindow.setVisible(true);
				SWindow.setVisible(false);
			}
			
			if (e.getSource()==Return) {
				SWindow.setSize(500, 200);
				SWindow.setLayout(null);
				SWindow.setVisible(true);
				ProgramWindow.setVisible(false);
				InstructorWindow.setVisible(false);
			}
			
			if (e.getSource()==Return2) {
				SWindow.setSize(500, 200);
				SWindow.setLayout(null);
				SWindow.setVisible(true);
				ProgramWindow.setVisible(false);
				InstructorWindow.setVisible(false);
			}
			
			
		}
}