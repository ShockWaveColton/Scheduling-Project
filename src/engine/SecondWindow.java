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
	JFrame UnscheduleProgramWindow = new JFrame();
	JFrame UnscheduleInstructorWindow = new JFrame();

	
	//Main Window items
	JLabel Prompt = new JLabel("Would you like a program report or an instructor report");
	private JButton program = new JButton("Program");
	private JButton instructor = new JButton("Instructor");
    private JButton unsched_Programs = new JButton("<html>Unscheduled <br/> Programs </html>");
    private JButton unsched_Instructors = new JButton("<html>Unscheduled <br/>Instructors</html>");
	
	//Report Choice Window items
	JLabel Sem1 = new JLabel("Semester 1");
	JLabel Sem2 = new JLabel("Semester 2");
	
	JLabel Seme1 = new JLabel("Semester 1");
	JLabel Seme2 = new JLabel("Semester 2");
	
	JLabel UnscheInstrSem1 = new JLabel("Semester 1");
	JLabel UnscheInstrSem2 = new JLabel("Semester 2");

	JLabel UnscheProSem1 = new JLabel("Semester 1");
	JLabel UnscheProSem2 = new JLabel("Semester 2");
	
	//instructor list box
	private DefaultListModel InstructorListModelSem1 = new DefaultListModel();
	private JList InstructorListSem1 = new JList(InstructorListModelSem1);
	
	private DefaultListModel InstructorListModelSem2 = new DefaultListModel();
	private JList InstructorListSem2 = new JList(InstructorListModelSem2);
	
	//unscheduled instructor list box
	private DefaultListModel UncheInstructorListModelSem1 = new DefaultListModel();
	private JList UncheInstructorListSem1 = new JList(UncheInstructorListModelSem1);
	
	private DefaultListModel UncheInstructorListModelSem2 = new DefaultListModel();
	private JList UncheInstructorListSem2 = new JList(UncheInstructorListModelSem2);
	
	
	//unscheduled program list box
	private DefaultListModel UncheProgramListModelSem1 = new DefaultListModel();
	private JList UncheProgramListSem1 = new JList(UncheProgramListModelSem1);
	
	private DefaultListModel UncheProgramListModelSem2 = new DefaultListModel();
	private JList UncheProgramListSem2 = new JList(UncheProgramListModelSem2);
	
	// program list box
	private DefaultListModel ProgramListModelSem1 = new DefaultListModel();
	private JList ProgramListSem1 = new JList(ProgramListModelSem1);
	
	private DefaultListModel ProgramListModelSem2 = new DefaultListModel();
	private JList ProgramListSem2 = new JList(ProgramListModelSem2);
	
	private JButton Return = new JButton("Return");
	private JButton Return2 = new JButton("Return");
	private JButton Return3 = new JButton("Return");
	private JButton Return4 = new JButton("Return");

	
	
	//**ListModels require filling and setting**
	
	
	public void fillTheList() {
		for(Instructor instructor : ObjectManager.getInstructors()) {
			Report report1 = new Report(0);
			
			report1.getHoursForInstructor(instructor);
			
			InstructorListModelSem1.addElement(instructor.getFullName() + ": " + (report1.getHoursForInstructor(instructor)));
			
		}
		
		for(Instructor instructor : ObjectManager.getInstructors()) {
			Report report2 = new Report(1);
			
			report2.getHoursForInstructor(instructor);
			
			InstructorListModelSem2.addElement(instructor.getFullName() + ": " + report2.getHoursForInstructor(instructor));
			
		}
		
		
		for(Program program : ObjectManager.getPrograms()) {
			Report report1 = new Report(0);
			Report report2 = new Report(1);
			
			report1.getHoursForProgram(program);
			report2.getHoursForProgram(program);
			
			
			ProgramListModelSem1.addElement(program.getName() + ": " + (report1.getHoursForProgram(program) + report2.getHoursForProgram(program)));
			
		}
		
		for(Program program : ObjectManager.getPrograms()) {
			Report report3 = new Report(2);
			Report report4 = new Report(3);
			
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
		Return.setBounds(50,350,100,50);
		Return.addActionListener(this);
		
		InstructorWindow.setSize(550, 500);
		InstructorWindow.setLayout(null);
		InstructorWindow.setVisible(false);
		
		InstructorWindow.add(unsched_Instructors);
		unsched_Instructors.setBounds(350,350,120,50);
		unsched_Instructors.addActionListener(this);
		
		//unscheduled instructor window
		UnscheduleInstructorWindow.add(UnscheInstrSem1);
		UnscheInstrSem1.setBounds(50,0,500,50);
		
		UnscheduleInstructorWindow.add(UnscheInstrSem2);
		UnscheInstrSem2.setBounds(350,0,500,50);
		
		UnscheduleInstructorWindow.add(UncheInstructorListSem1);
		UncheInstructorListSem1.setBounds(25,50,150,300);
		
		UnscheduleInstructorWindow.add(UncheInstructorListSem2);
		UncheInstructorListSem2.setBounds(325,50,150,300);
		
		UnscheduleInstructorWindow.add(Return3);
		Return3.setBounds(50,350,100,50); //need fix to be center
		Return3.addActionListener(this);
		
		UnscheduleInstructorWindow.setSize(550, 500);
		UnscheduleInstructorWindow.setLayout(null);
		UnscheduleInstructorWindow.setVisible(false);
		
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
		Return2.setBounds(50,350,100,50);
		Return2.addActionListener(this);
		
		ProgramWindow.add(unsched_Programs);
		unsched_Programs.setBounds(350,350,120,50);
		unsched_Programs.addActionListener(this);
		
		ProgramWindow.setSize(550, 500);
		ProgramWindow.setLayout(null);
		ProgramWindow.setVisible(false);
		
		//unscheduled program window
		UnscheduleProgramWindow.add(UnscheProSem1);
		UnscheProSem1.setBounds(50,0,500,50);
		
		UnscheduleProgramWindow.add(UnscheProSem2);
		UnscheProSem2.setBounds(350,0,500,50);
		
		UnscheduleProgramWindow.add(UncheProgramListSem1);
		UncheProgramListSem1.setBounds(25,50,150,300);
		
		UnscheduleProgramWindow.add(UncheProgramListSem2);
		UncheProgramListSem2.setBounds(325,50,150,300); 
		
		UnscheduleProgramWindow.add(Return4);
		Return4.setBounds(50,350,100,50); //need fix to be center
		Return4.addActionListener(this);

		UnscheduleProgramWindow.setSize(550, 500);
		UnscheduleProgramWindow.setLayout(null);
		UnscheduleProgramWindow.setVisible(false);

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
			
			//unscheduled program button is clicked
			if (e.getSource()==unsched_Programs) {
				UnscheduleProgramWindow.setSize(550, 500);
				UnscheduleProgramWindow.setLayout(null);
				UnscheduleProgramWindow.setVisible(true);
				ProgramWindow.setVisible(false);
				InstructorWindow.setVisible(false);
				UnscheduleInstructorWindow.setVisible(false);
				SWindow.setVisible(false);
			}
			
			//unscheduled instructor button is clicked
			if (e.getSource()==unsched_Instructors) {
				UnscheduleInstructorWindow.setSize(550, 500);
				UnscheduleInstructorWindow.setLayout(null);
				UnscheduleInstructorWindow.setVisible(true);
				UnscheduleProgramWindow.setVisible(false);
				ProgramWindow.setVisible(false);
				InstructorWindow.setVisible(false);
				SWindow.setVisible(false);
			}
			
			//return button on instructor window
			if (e.getSource()==Return) {
				SWindow.setSize(500, 200);
				SWindow.setLayout(null);
				SWindow.setVisible(true);
				ProgramWindow.setVisible(false);
				InstructorWindow.setVisible(false);
			}
			
			//return button on program window
			if (e.getSource()==Return2) {
				SWindow.setSize(500, 200);
				SWindow.setLayout(null);
				SWindow.setVisible(true);
				ProgramWindow.setVisible(false);
				InstructorWindow.setVisible(false);
			}
			
			//return button on unscheduled instructor window
			if (e.getSource()==Return3) {
				InstructorWindow.setSize(550, 500);
				InstructorWindow.setLayout(null);
				InstructorWindow.setVisible(true);
				SWindow.setVisible(false);
				ProgramWindow.setVisible(false);
				UnscheduleInstructorWindow.setVisible(false);
			}
			
			//return button on unscheduled program window
			if (e.getSource()==Return4) {
				ProgramWindow.setSize(550, 500);
				ProgramWindow.setLayout(null);
				ProgramWindow.setVisible(true);
				SWindow.setVisible(false);
				InstructorWindow.setVisible(false);
				UnscheduleProgramWindow.setVisible(false);
			}
		}
			public void fillUnscheduledList() {
				for(Instructor instructor : ObjectManager.getInstructors()) {
				
					Report report5 = new Report(4);
					report5.getHoursForInstructor(instructor);
					//if (instructorHours < 1) {
					InstructorListModelSem1.addElement(instructor.getFullName() + ": " + (report5.getHoursForInstructor(instructor)));
				}
				//else {
				//}
				
				//}
			}
			
}