package engine;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import objects.Classroom;
import objects.Course;
import objects.Instructor;
import objects.Program;
import objects.Schedule;

public class Window {
	
//	private ObjectManager objectManager;
	private static JPanel panel;
	
	private static JComboBox<String> listInstructors;
	private static JComboBox<String> listClassrooms;
	private static JComboBox<String> listCourses;
	private static JComboBox<String> listPrograms;
	
	private static JLabel lastClickedTop = new JLabel();
	private static JLabel lastClickedMid = new JLabel();
	private static JLabel lastClickedBot = new JLabel();
	
	private static boolean[][][] isClicked;
	
	public Window(ObjectManager objectManager) {
//		this.objectManager = objectManager;
		JFrame window = new JFrame("NSCC Scheduling Protoype");
		window.setSize(635, 840);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel();
		panel.setLayout(null);

		JMenuBar menuBar = new JMenuBar();
		JMenu menuBar_File = new JMenu("File");
		// Had to initialize Edit (and make it final) so it can be enabled when file is loaded.
		final JMenu menuBar_Edit = new JMenu("Edit"); 
		JMenuItem file_new = new JMenuItem("New");
		file_new.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
		    	FileIO.New();
		    	menuBar_Edit.setEnabled(true);
	    	}
	    });
		menuBar_File.add(file_new);
		JMenuItem file_load = new JMenuItem("Load");
		file_load.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
		    	FileIO.Load();
		    	menuBar_Edit.setEnabled(true);
	    	}
	    });
		menuBar_File.add(file_load);
		JMenuItem file_quit = new JMenuItem("Quit");
		file_quit.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
		    	System.exit(0);
	    	}
	    });	
		menuBar_File.add(file_quit);
		menuBar.add(menuBar_File);
		menuBar_Edit.setEnabled(false);			
		JMenuItem edit_Classrooms = new JMenuItem("Manage Classrooms");
		edit_Classrooms.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
		    	ManageClassrooms();
	    	}
	    });
		menuBar_Edit.add(edit_Classrooms);
		JMenuItem edit_Courses = new JMenuItem("Manage Courses");
		edit_Courses.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
		    	ManageCourses();
	    	}
	    });
		menuBar_Edit.add(edit_Courses);
		JMenuItem edit_Instructors = new JMenuItem("Manage Instructors");
		edit_Instructors.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
		    	ManageInstructors();
	    	}
	    });
		menuBar_Edit.add(edit_Instructors);
		JMenuItem edit_Programs = new JMenuItem("Manage Programs");
		edit_Programs.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
		    	ManagePrograms();
	    	}
	    });
		menuBar_Edit.add(edit_Programs);
		menuBar.add(menuBar_Edit);
		JMenu menuBar_Help = new JMenu("Help");
		JMenuItem help_How = new JMenuItem("How to use");
		help_How.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "How to use this application:\n"
						+ "\n"
						+ "Create a new database, or Load an existing database via File >> New / Load.\n"
						+ "\n"
						+ "Database Management is available under Edit Menu,\n"
						+ "But a file must be loaded/created first.\n"
						+ "\n"
						+ "");
	    	}
	    });
		menuBar_Help.add(help_How);
		JMenuItem help_About = new JMenuItem("About");
		help_About.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "NSCC Scheduling Assistant\n"
						+ "Version 1.0\n"
						+ "\n"
						+ "Written ENTIRELY by Rob MacDonald\n"
						+ "\n"
						+ "For NSCC IT-Programming Year 2\n"
						+ "INFT-4000 (Capstone) Project.\n"
						+ "\n"
						+ "Copyright � 2021.               Hi Darren! :)");
	    	}
	    });
		menuBar_Help.add(help_About);
		menuBar.add(menuBar_Help);
		menuBar.setBounds(0, 0, window.getWidth(), 25);
		panel.add(menuBar);
		
		listClassrooms = new JComboBox<>();
		listClassrooms.setBounds(10, 35, 140, 25);
		ArrayList<Classroom> classrooms = ObjectManager.getClassrooms();
		for (int i = 0; i < classrooms.size(); i++) {
			listClassrooms.addItem(classrooms.get(i).getName());
			System.out.println(classrooms.get(i).getName());
		}
		if (listClassrooms.getItemCount() == 0)
			listClassrooms.setVisible(false);
		else
			listClassrooms.setVisible(true);			
		listClassrooms.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				Schedule.Display(listClassrooms.getSelectedItem().toString());
			}
		});
		panel.add(listClassrooms);
		
		JButton addCourse = new JButton("Add Course");
		addCourse.setBounds(165, 35, 140, 25);
		addCourse.setVisible(true);
		panel.add(addCourse);
		addCourse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Course> courses = ObjectManager.getCourses();
				Course.Add();
				listCourses.addItem(courses.get(courses.size()-1).getName());
				if (listCourses.getItemCount() > 1)
					listCourses.setSelectedIndex(courses.size()-1);
				listCourses.setVisible(true);
			}
		});
		listCourses = new JComboBox<>();
		listCourses.setBounds(165, 60, 140, 25);
		listCourses.setVisible(false);
		listCourses.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				Schedule.Display(listCourses.getSelectedItem().toString());
			}
		});
		panel.add(listCourses);
		
		JButton addInstructor = new JButton("Add Instructor");
		addInstructor.setBounds(320, 35, 140, 25);
		addInstructor.setVisible(true);
		panel.add(addInstructor);
		addInstructor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Instructor> instructors = ObjectManager.getInstructors();
				Instructor.Add();
				listInstructors.addItem(instructors.get(instructors.size()-1).getName());
				if (listInstructors.getItemCount() > 1)
					listInstructors.setSelectedIndex(instructors.size()-1);
				listInstructors.setVisible(true);
			}
		});
		listInstructors = new JComboBox<>();
		listInstructors.setBounds(320, 60, 140, 25);
		listInstructors.setVisible(false);
		listInstructors.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				Schedule.Display(listInstructors.getSelectedItem().toString());
			}
		});
		panel.add(listInstructors);

		JButton addProgram = new JButton("Add Program");
		addProgram.setBounds(475, 35, 140, 25);
		addProgram.setVisible(true);
		panel.add(addProgram);
		addProgram.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Program> programs = ObjectManager.getPrograms();
				Program.Add();
				listPrograms.addItem(programs.get(programs.size()-1).getName());
				if (listPrograms.getItemCount() > 1)
					listPrograms.setSelectedIndex(programs.size()-1);
				listPrograms.setVisible(true);
			}
		});
		listPrograms = new JComboBox<>();
		listPrograms.setBounds(475, 60, 140, 25);
		listPrograms.setVisible(false);
		listPrograms.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				Schedule.Display(listPrograms.getSelectedItem().toString());
			}
		});
		panel.add(listPrograms);
		
		panel.setVisible(true);
		window.add(panel);
		window.setVisible(true);
	}
	
	public static JComboBox<String> getClassroomList() { return listInstructors; }
	public static JComboBox<String> getCourseList() { return listInstructors; }
	public static JComboBox<String> getInstructorList() { return listInstructors; }
	public static JComboBox<String> getProgramList() { return listInstructors; }

	public static void DrawSchedule(String name) {
		
		Border topBorder = BorderFactory.createMatteBorder(2, 2, 0, 2, Color.black);
		Border midBorder = BorderFactory.createMatteBorder(0, 2, 0, 2, Color.black);
		Border botBorder = BorderFactory.createMatteBorder(0, 2, 2, 2, Color.black);
		isClicked = new boolean[5][9][3];
		UnClickLabels();
		
		int cellWidth = 100;
		int cellHeight = 25;
		
		JLabel titleTime = new JLabel(" Time");
		titleTime.setBounds(10, 95, cellWidth, cellHeight);
		titleTime.setOpaque(true);
		titleTime.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));
		titleTime.setBackground(new Color(255,255,255,255));
		panel.add(titleTime);
		JLabel titleMon = new JLabel(" Monday");
		titleMon.setBounds(110, 95, cellWidth, cellHeight);
		titleMon.setOpaque(true);
		titleMon.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));
		titleMon.setBackground(new Color(255,255,255,255));
		panel.add(titleMon);
		JLabel titleTue = new JLabel(" Tuesday");
		titleTue.setBounds(210, 95, cellWidth, cellHeight);
		titleTue.setOpaque(true);
		titleTue.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));
		titleTue.setBackground(new Color(255,255,255,255));
		panel.add(titleTue);
		JLabel titleWed = new JLabel(" Wednesday");
		titleWed.setBounds(310, 95, cellWidth, cellHeight);
		titleWed.setOpaque(true);
		titleWed.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));
		titleWed.setBackground(new Color(255,255,255,255));
		panel.add(titleWed);
		JLabel titleThu = new JLabel(" Thursday");
		titleThu.setBounds(410, 95, cellWidth, cellHeight);
		titleThu.setOpaque(true);
		titleThu.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));
		titleThu.setBackground(new Color(255,255,255,255));
		panel.add(titleThu);
		JLabel titleFri = new JLabel(" Friday");
		titleFri.setBounds(510, 95, cellWidth, cellHeight);
		titleFri.setOpaque(true);
		titleFri.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));
		titleFri.setBackground(new Color(255,255,255,255));
		panel.add(titleFri);
		
		JLabel title830 = new JLabel(" 8:30 AM");
		title830.setBounds(10, 95+25, cellWidth, cellHeight*3);
		title830.setOpaque(true);
		title830.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));
		title830.setBackground(new Color(255,255,255,255));
		panel.add(title830);
		JLabel title930 = new JLabel(" 9:30 AM");
		title930.setBounds(10, 170+25, cellWidth, cellHeight*3);
		title930.setOpaque(true);
		title930.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));
		title930.setBackground(new Color(255,255,255,255));
		panel.add(title930);
		JLabel title1030 = new JLabel(" 10:30 AM");
		title1030.setBounds(10, 245+25, cellWidth, cellHeight*3);
		title1030.setOpaque(true);
		title1030.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));
		title1030.setBackground(new Color(255,255,255,255));
		panel.add(title1030);
		JLabel title1130 = new JLabel(" 11:30 AM");
		title1130.setBounds(10, 320+25, cellWidth, cellHeight*3);
		title1130.setOpaque(true);
		title1130.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));
		title1130.setBackground(new Color(255,255,255,255));
		panel.add(title1130);
		JLabel title1230 = new JLabel(" 12:30 PM");
		title1230.setBounds(10, 395+25, cellWidth, cellHeight*3);
		title1230.setOpaque(true);
		title1230.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));
		title1230.setBackground(new Color(255,255,255,255));
		panel.add(title1230);
		JLabel title1330 = new JLabel(" 1:30 PM");
		title1330.setBounds(10, 470+25, cellWidth, cellHeight*3);
		title1330.setOpaque(true);
		title1330.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));
		title1330.setBackground(new Color(255,255,255,255));
		panel.add(title1330);
		JLabel title1430 = new JLabel(" 2:30 PM");
		title1430.setBounds(10, 545+25, cellWidth, cellHeight*3);
		title1430.setOpaque(true);
		title1430.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));
		title1430.setBackground(new Color(255,255,255,255));
		panel.add(title1430);
		JLabel title1530 = new JLabel(" 3:30 PM");
		title1530.setBounds(10, 620+25, cellWidth, cellHeight*3);
		title1530.setOpaque(true);
		title1530.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));
		title1530.setBackground(new Color(255,255,255,255));
		panel.add(title1530);
		JLabel title1630 = new JLabel(" 4:30 PM");
		title1630.setBounds(10, 695+25, cellWidth, cellHeight*3);
		title1630.setOpaque(true);
		title1630.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));
		title1630.setBackground(new Color(255,255,255,255));
		panel.add(title1630);

		int labelStartX = 110;
		int labelStartY = 120;
		JLabel[][][] label = new JLabel[5][9][3];
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 5; x++) {
				for (int z = 0; z < 3; z++) {
					label[x][y][z] = new JLabel();
					label[x][y][z].setBounds(labelStartX+(x*100), labelStartY+(z*25)+(y*75), cellWidth, cellHeight);
					label[x][y][z].setOpaque(true);
					switch (z) {
					case 0:
						label[x][y][z].setBorder(topBorder);
						break;
					case 1:
						label[x][y][z].setBorder(midBorder);
						break;
					case 2:
						label[x][y][z].setBorder(botBorder);
						break;
					default:
						throw new IllegalArgumentException("Unexpected value: " + z);
					}
					label[x][y][z].setBackground(new Color(255,255,255,255));
					panel.add(label[x][y][z]);
					
					//Workaround to avoid "Local variable defined in an enclosing scope must be final or effectively final"
					final int innerX = x;
					final int innerY = y;
					label[x][y][z].addMouseListener(new MouseAdapter() {
						public void mousePressed(MouseEvent e) {
							lastClickedTop.setBackground(new Color(255, 255, 255, 255));
							lastClickedMid.setBackground(new Color(255, 255, 255, 255));
							lastClickedBot.setBackground(new Color(255, 255, 255, 255));
							UnClickLabels();
							label[innerX][innerY][0].setBackground(new Color(255, 255, 0, 255));
							label[innerX][innerY][1].setBackground(new Color(255, 255, 0, 255));
							label[innerX][innerY][2].setBackground(new Color(255, 255, 0, 255));
							lastClickedTop = label[innerX][innerY][0];
							lastClickedMid = label[innerX][innerY][1];
							lastClickedBot = label[innerX][innerY][2];
							isClicked[innerX][innerY][0] = true;
							isClicked[innerX][innerY][1] = true;
							isClicked[innerX][innerY][2] = true;
						}
						public void mouseEntered(MouseEvent e) {
							if (!(isClicked[innerX][innerY][0] || isClicked[innerX][innerY][1] || isClicked[innerX][innerY][2])) {
								label[innerX][innerY][0].setBackground(new Color(192, 192, 192, 255));
								label[innerX][innerY][1].setBackground(new Color(192, 192, 192, 255));
								label[innerX][innerY][2].setBackground(new Color(192, 192, 192, 255));
							}
						}
						public void mouseExited(MouseEvent e) {
							if (!(isClicked[innerX][innerY][0] || isClicked[innerX][innerY][1] || isClicked[innerX][innerY][2])) {
								label[innerX][innerY][0].setBackground(new Color(255, 255, 255, 255));
								label[innerX][innerY][1].setBackground(new Color(255, 255, 255, 255));
								label[innerX][innerY][2].setBackground(new Color(255, 255, 255, 255));
							}
						}
					});
				}
			}			
		}
		panel.repaint();
		panel.revalidate();
	}
	
	private static void UnClickLabels(){
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 9; y++) {
				for (int z = 0; z < 3; z++) {
					isClicked[x][y][z] = false;
				}
			}	
		}
	}
	
	//Builds a new Frame when Edit>>Manage Classrooms is selected:
	private void ManageClassrooms(){
		JFrame classroomManager = new JFrame("Classroom Manager");
		classroomManager.setBounds(0, 0, 20, 170);
		classroomManager.setLocationRelativeTo(null);
		classroomManager.setVisible(true);
		JPanel classroomPanel = new JPanel();
		classroomPanel.setLayout(null);
		JLabel classroomListLabel = new JLabel("Classrooms:");
		classroomListLabel.setBounds(10, 05, 100, 30);
		JLabel classroomWingLabel = new JLabel("    Wing:");
		classroomWingLabel.setBounds(10, 40, 50, 30);
		JLabel classroomNumberLabel = new JLabel("Number:");
		classroomNumberLabel.setBounds(10, 70, 50, 30);
		JTextArea classroom_wing = new JTextArea();
		classroom_wing.setBounds(60, 45, 100, 20);
		JTextArea classroom_number = new JTextArea();
		classroom_number.setBounds(60, 75, 100, 20);
		listClassrooms = new JComboBox<>();
		listClassrooms.setBounds(105, 05, 100, 25);
		ArrayList<Classroom> classrooms = ObjectManager.getClassrooms();
		for (int i = 0; i < classrooms.size(); i++) {
			listClassrooms.addItem(classrooms.get(i).getName());
			System.out.println(classrooms.get(i).getName());
		}
		if (listClassrooms.getItemCount() == 0)
			listClassrooms.setVisible(false);
		else
			listClassrooms.setVisible(true);			
		listClassrooms.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				Schedule.Display(listClassrooms.getSelectedItem().toString());
			}
		});
		JButton classroomAdd = new JButton("Add");
		classroomAdd.setBounds(10, 100, 100, 25);
		classroomAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!classroom_number.getText().equals("") && !classroom_wing.getText().equals("")) {
					Classroom.Add(classroom_wing.getText(), classroom_number.getText());
					ArrayList<Classroom> classrooms = ObjectManager.getClassrooms();
					listClassrooms.addItem(classrooms.get(classrooms.size()-1).getName());
					if (listClassrooms.getItemCount() > 1)
						listClassrooms.setSelectedIndex(classrooms.size()-1);
					listClassrooms.setVisible(true);					
				} else {
					JOptionPane.showMessageDialog(null, "You must fill in Wing and Number.");
				}
			}
		});
		JButton classroomUpdate = new JButton("Update");
		classroomUpdate.setBounds(115, 100, 100, 25);
		classroomUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!classroom_number.getText().equals("") && !classroom_wing.getText().equals("")) {
					if (!(listClassrooms.getSelectedIndex() == -1)) {
						Classroom.Update(listClassrooms.getSelectedIndex(), classroom_wing.getText(), classroom_number.getText());
						ArrayList<Classroom> classrooms = ObjectManager.getClassrooms();
						listClassrooms.addItem(classrooms.get(classrooms.size()-1).getName());
						if (listClassrooms.getItemCount() > 1)
							listClassrooms.setSelectedIndex(classrooms.size()-1);
						listClassrooms.setVisible(true);											
					} else {
						JOptionPane.showMessageDialog(null, "You must select a classroom above to update");
					}
				} else {
					JOptionPane.showMessageDialog(null, "You must fill in Wing and Number.");
				}
			}
		});
		classroomPanel.add(classroomListLabel);
		classroomPanel.add(classroomWingLabel);
		classroomPanel.add(classroomNumberLabel);
		classroomPanel.add(listClassrooms);
		classroomPanel.add(classroom_wing);
		classroomPanel.add(classroom_number);
		classroomPanel.add(classroomAdd);
		classroomPanel.add(classroomUpdate);
		classroomManager.add(classroomPanel);
	}
	private void ManageCourses(){

	}
	private void ManageInstructors(){
		
	}
	private void ManagePrograms(){
		
	}
}