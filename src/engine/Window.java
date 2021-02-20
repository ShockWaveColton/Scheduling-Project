package engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.Border;

import objects.Classroom;
import objects.Course;
import objects.Instructor;
import objects.Program;
import objects.Schedule;

public class Window {
	
//	private ObjectManager objectManager;
	private static JPanel panel;
	
	private static DefaultComboBoxModel<String> classroomsModel = new DefaultComboBoxModel<>();
	private static DefaultComboBoxModel<String> coursesModel = new DefaultComboBoxModel<>();
	private static DefaultComboBoxModel<String> instructorsModel = new DefaultComboBoxModel<>();
	private static DefaultComboBoxModel<String> programsModel = new DefaultComboBoxModel<>();
	
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
		    	if (FileIO.New() == 0) 
		    		menuBar_Edit.setEnabled(true);
		    	else
		    		menuBar_Edit.setEnabled(false);
	    	}
	    });
		menuBar_File.add(file_new);
		JMenuItem file_load = new JMenuItem("Load");
		file_load.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
		    	if (FileIO.Load() == 0) {
		    		menuBar_Edit.setEnabled(true);
		    		reloadDropDowns();
		    	}
		    	else
		    		menuBar_Edit.setEnabled(false);
	    	}
	    });
		menuBar_File.add(file_load);

		JMenuItem file_export = new JMenuItem("Export");
		file_export.setEnabled(false);
		file_export.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
		    	//TO-DO: ADD EXPORT CSV FUNCTIONALITY.
		    }
	    });
		menuBar_File.add(file_export);

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
						+ "Database Management options are available under the Edit Menu,\n"
						+ "But a file must be loaded/created first.\n"
						+ "\n"
						+ ""
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
						+ "Copyright © 2021.               Hi Darren! :)");
	    	}
	    });
		menuBar_Help.add(help_About);
		menuBar.add(menuBar_Help);
		menuBar.setBounds(0, 0, window.getWidth(), 25);
		panel.add(menuBar);
		
//		JComboBox<String> listClassrooms = new JComboBox<String>(classroomsModel);
//		listClassrooms.setBounds(10, 35, 140, 25);
//		listClassrooms.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				Schedule.Display(listClassrooms.getSelectedItem().toString());
//			}
//		});
//		panel.add(listClassrooms);
//		
//		JComboBox<String> listCourses = new JComboBox<String>(coursesModel);
//		listCourses.setBounds(165, 35, 140, 25);
//		listCourses.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				Schedule.Display(listCourses.getSelectedItem().toString());
//			}
//		});
//		panel.add(listCourses);
//
//		JComboBox<String> listInstructors = new JComboBox<String>(coursesModel);
//		listInstructors.setBounds(320, 35, 140, 25);
//		listInstructors.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				Schedule.Display(listInstructors.getSelectedItem().toString());
//			}
//		});
//		panel.add(listInstructors);
//
//		JComboBox<String> listPrograms = new JComboBox<String>(coursesModel);
//		listPrograms.setBounds(475, 35, 140, 25);
//		listPrograms.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				Schedule.Display(listPrograms.getSelectedItem().toString());
//			}
//		});
//		panel.add(listPrograms);

		
		panel.setVisible(true);
		window.add(panel);
		window.setVisible(true);
	}
	
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
		classroomManager.setMinimumSize(new Dimension());
		classroomManager.setVisible(true);
		classroomManager.setBounds(0, 0,250, 280);
		classroomManager.setLocationRelativeTo(null);
		JPanel classroomPanel = new JPanel();
		classroomPanel.setLayout(null);
		classroomPanel.setPreferredSize(new Dimension(300, 300));
		JLabel classroomListLabel = new JLabel("Classrooms:");
		classroomListLabel.setBounds(10, 05, 100, 30);
		JLabel classroomWingLabel = new JLabel("Campus Wing:");
		classroomWingLabel.setBounds(10, 40, 100, 30);
		JLabel classroomNumberLabel = new JLabel("Room Number:");
		classroomNumberLabel.setBounds(10, 70, 100, 30);
		JTextField classroom_wing = new JTextField();
		classroom_wing.setBounds(100, 45, 100, 20);
		JTextField classroom_number = new JTextField();
		classroom_number.setBounds(100, 75, 100, 20);
		JRadioButton noLab = new JRadioButton("Not a Lab");
		noLab.setSelected(true);
		noLab.setBounds(10, 100, 150, 20);
		JRadioButton macLab = new JRadioButton("Mac Lab");
		macLab.setBounds(10, 120, 150, 20);
		JRadioButton netLab = new JRadioButton("Networking Lab");
		netLab.setBounds(10, 140, 150, 20);
		JRadioButton winLab = new JRadioButton("Windows Lab");
		winLab.setBounds(10, 160, 150, 20);
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(noLab);
		radioGroup.add(macLab);
		radioGroup.add(netLab);
		radioGroup.add(winLab);

		JComboBox<String> listClassrooms = new JComboBox<>(classroomsModel);
		listClassrooms.setBounds(105, 05, 125, 25);
		if (listClassrooms.getItemCount() == 0)
			listClassrooms.setVisible(false);
		else
			listClassrooms.setVisible(true);			
		listClassrooms.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (listClassrooms.getSelectedIndex() >= 0) {
					Schedule.Display(listClassrooms.getSelectedItem().toString());
					classroom_wing.setText(ObjectManager.getClassrooms().get(listClassrooms.getSelectedIndex()).getWing()); 
					classroom_number.setText(ObjectManager.getClassrooms().get(listClassrooms.getSelectedIndex()).getRoom());
					switch (ObjectManager.getClassrooms().get(listClassrooms.getSelectedIndex()).getLab()) {
					case 0: 
						noLab.setSelected(true);
						break;
					case 1: 
						macLab.setSelected(true);
						break;
					case 2: 
						netLab.setSelected(true);
						break;
					case 3: 
						winLab.setSelected(true);
						break;
					default:
						throw new IllegalArgumentException("Unexpected value: " + ObjectManager.getClassrooms().get(listClassrooms.getSelectedIndex()).getLab());
					}

				}
			}
		});
		JButton classroomAdd = new JButton("Add");
		classroomAdd.setBounds(10, 185, 225, 25);
		classroomAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!classroom_number.getText().equals("") && !classroom_wing.getText().equals("")) {
					ArrayList<Classroom> classrooms = ObjectManager.getClassrooms();
					String userValues = classroom_wing.getText() + "-" + classroom_number.getText();
					for (int i = 0; i < classrooms.size(); i++) {
						String classroomName = classrooms.get(i).getWing() +"-"+classrooms.get(i).getRoom();
						if (userValues.equals(classroomName)) {
							JOptionPane.showMessageDialog(null, "Classroom is already in database!");
							return;
						}
					}
					int labType = getLabType(noLab, macLab, netLab, winLab);
					Classroom.Create(classroom_wing.getText(), classroom_number.getText(), labType);
					String classroomName = classroom_wing.getText() + "-" + classroom_number.getText();
					listClassrooms.addItem(classroomName);
					if (listClassrooms.getItemCount() > 1)
						listClassrooms.setSelectedIndex(classrooms.size()-1);
					listClassrooms.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "You must fill in Wing and Number.");
				}
			}
		});
		JButton classroomUpdate = new JButton("Update");
		classroomUpdate.setBounds(10, 215, 120, 25);
		classroomUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!classroom_number.getText().equals("") && !classroom_wing.getText().equals("")) {
					if (!(listClassrooms.getSelectedIndex() == -1)) {
						ArrayList<Classroom> classrooms = ObjectManager.getClassrooms();
						String userValues = classroom_wing.getText() + "-" + classroom_number.getText();
						for (int i = 0; i < classrooms.size(); i++) {
							String classroomName = classrooms.get(i).getWing() +"-"+classrooms.get(i).getRoom();
							if (userValues.equals(classroomName)) {
								JOptionPane.showMessageDialog(null, "Classroom is already in database!");
								return;
							}
						}
						int labType = getLabType(noLab, macLab, netLab, winLab);
						Classroom.Update(listClassrooms.getSelectedIndex() + 1, classroom_wing.getText(), classroom_number.getText(), labType);
						reloadDropDowns();
					} else {
						JOptionPane.showMessageDialog(null, "You must select a classroom above to update.");
					}
				} else {
					JOptionPane.showMessageDialog(null, "You must fill in Wing and Number.");
				}
			}
		});
		JButton classroomDelete = new JButton("Delete");
		classroomDelete.setBounds(115, 215, 120, 25);
		classroomDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Classroom.Delete(ObjectManager.getClassrooms().get(listClassrooms.getSelectedIndex()).getID());
				reloadDropDowns();
				listClassrooms.setSelectedIndex(-1);
			}
		});
		classroomPanel.add(classroomListLabel);
		classroomPanel.add(classroomWingLabel);
		classroomPanel.add(classroomNumberLabel);
		classroomPanel.add(listClassrooms);
		classroomPanel.add(classroom_wing);
		classroomPanel.add(classroom_number);
		classroomPanel.add(noLab);
		classroomPanel.add(macLab);
		classroomPanel.add(netLab);
		classroomPanel.add(winLab);
		classroomPanel.add(classroomAdd);
		classroomPanel.add(classroomUpdate);
		classroomPanel.add(classroomDelete);
		classroomManager.add(classroomPanel);
	}
	private void ManageCourses(){
		JFrame courseManager = new JFrame("Course Manager");
		courseManager.setMinimumSize(new Dimension());
		courseManager.setVisible(true);
		courseManager.setBounds(0, 0,250, 390);
		courseManager.setLocationRelativeTo(null);

		JPanel coursePanel = new JPanel();
		coursePanel.setLayout(null);
		coursePanel.setPreferredSize(new Dimension(300, 300));

		JLabel courseListLabel = new JLabel("Courses:");
		JLabel courseCodeLabel = new JLabel("Course Code:");
		JLabel courseNameLabel = new JLabel("Course Name:");
		JLabel courseHoursLabel = new JLabel("Course Hours:");
		JLabel courseProgramLabel = new JLabel("Program:");
		JLabel courseTermLabel = new JLabel("Term:");
		JLabel courseSectionsLabel = new JLabel("Sections:");
		JLabel courseInstructorLabel = new JLabel("Instructor:");
		courseListLabel.setBounds(10, 10, 100, 20);
		courseCodeLabel.setBounds(10, 30, 100, 20);
		courseNameLabel.setBounds(10, 50, 100, 20);
		courseHoursLabel.setBounds(10, 70, 100, 20);
		courseSectionsLabel.setBounds(10, 90, 100, 20);
		courseInstructorLabel.setBounds(10, 110, 100, 20);
		courseProgramLabel.setBounds(10, 130, 100, 20);
		courseTermLabel.setBounds   (10, 150, 100, 20);

		JTextField courseCode = new JTextField();
		JTextField courseName = new JTextField();
		JTextField courseHours = new JTextField();
		JTextField courseSections = new JTextField();
		JComboBox<String> courseInstructor = new JComboBox<String>(instructorsModel);
		JComboBox<String> courseProgram = new JComboBox<String>(programsModel);
		JSlider courseTerm = new JSlider();
		courseCode.setBounds(100, 30, 140, 20);
		courseName.setBounds(100, 50, 140, 20);
		courseHours.setBounds(100, 70, 140, 20);
		courseSections.setBounds(100, 90, 140, 20);
		courseInstructor.setBounds(100, 110, 140, 20);
		courseProgram.setBounds(100, 130, 140, 20);
		courseTerm.setBounds   (100, 150, 140, 50);
		courseTerm.setMinimum(1);
		courseTerm.setMaximum(4);
		courseTerm.setMajorTickSpacing(1);
		courseTerm.setPaintTicks(true);
		courseTerm.setPaintLabels(true);

		JRadioButton noLab = new JRadioButton("No Lab required");
		JRadioButton macLab = new JRadioButton("Requires Mac Lab");
		JRadioButton netLab = new JRadioButton("Requires Networking Lab");
		JRadioButton winLab = new JRadioButton("Requires Windows Lab");
		noLab.setSelected(true);
		noLab.setBounds(10, 200, 180, 20);
		macLab.setBounds(10, 220, 180, 20);
		netLab.setBounds(10, 240, 180, 20);
		winLab.setBounds(10, 260, 180, 20);

		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(noLab);
		radioGroup.add(macLab);
		radioGroup.add(netLab);
		radioGroup.add(winLab);

		JComboBox<String> courseList = new JComboBox<>(coursesModel);
		courseList.setBounds(100, 10, 140, 20);
		if (courseList.getItemCount() == 0) {
			courseListLabel.setVisible(false);
			courseList.setVisible(false);
		} else {
			courseListLabel.setVisible(true);
			courseList.setVisible(true);
		}
						
		courseList.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (courseList.getSelectedIndex() >= 0) {
					Schedule.Display(courseList.getSelectedItem().toString());
					courseCode.setText(ObjectManager.getCourses().get(courseList.getSelectedIndex()).getCode()); 
					courseName.setText(ObjectManager.getCourses().get(courseList.getSelectedIndex()).getName());
					switch (ObjectManager.getClassrooms().get(courseList.getSelectedIndex()).getLab()) {
					case 0: 
						noLab.setSelected(true);
						break;
					case 1: 
						macLab.setSelected(true);
						break;
					case 2: 
						netLab.setSelected(true);
						break;
					case 3: 
						winLab.setSelected(true);
						break;
					default:
						throw new IllegalArgumentException("Unexpected value: " + ObjectManager.getClassrooms().get(courseList.getSelectedIndex()).getLab());
					}

				}
			}
		});
		JButton courseAdd = new JButton("Add");
		courseAdd.setBounds(10, 290, 225, 25);
		courseAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!courseCode.getText().equals("") && !courseName.getText().equals("")) {
					ArrayList<Course> courses = ObjectManager.getCourses();
					String userValues = courseCode.getText() + "-" + courseName.getText();
					for (int i = 0; i < courses.size(); i++) {
						if (userValues.equals(courseCode.getText())) {
							JOptionPane.showMessageDialog(null, "Course is already in database!");
							return;
						}
					}
					int labType = getLabType(noLab, macLab, netLab, winLab);
//					Course.Create(courseCode.getText(), courseName.getText(), courseHours.getText(), courseProgram.getText(), courseSection.getText(), courseInstructor.getSelectedIndex());
//					listClassrooms.addItem(classroomName);
//					if (listClassrooms.getItemCount() > 1)
//						listClassrooms.setSelectedIndex(classrooms.size()-1);
//					listClassrooms.setVisible(true);
//				} else {
					JOptionPane.showMessageDialog(null, "You must fill in Wing and Number.");
				}
			}
		});
		JButton courseUpdate = new JButton("Update");
		courseUpdate.setBounds(10, 320, 120, 25);
//		classroomUpdate.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if (!classroom_number.getText().equals("") && !classroom_wing.getText().equals("")) {
//					if (!(listClassrooms.getSelectedIndex() == -1)) {
//						ArrayList<Classroom> classrooms = ObjectManager.getClassrooms();
//						String userValues = classroom_wing.getText() + "-" + classroom_number.getText();
//						for (int i = 0; i < classrooms.size(); i++) {
//							String classroomName = classrooms.get(i).getWing() +"-"+classrooms.get(i).getRoom();
//							if (userValues.equals(classroomName)) {
//								JOptionPane.showMessageDialog(null, "Classroom is already in database!");
//								return;
//							}
//						}
//						int labType = getLabType(noLab, macLab, netLab, winLab);
//						Classroom.Update(listClassrooms.getSelectedIndex() + 1, classroom_wing.getText(), classroom_number.getText(), labType);
//						reloadDropDowns();
//					} else {
//						JOptionPane.showMessageDialog(null, "You must select a classroom above to update.");
//					}
//				} else {
//					JOptionPane.showMessageDialog(null, "You must fill in Wing and Number.");
//				}
//			}
//		});
		JButton courseDelete = new JButton("Delete");
		courseDelete.setBounds(115, 320, 120, 25);
//		classroomDelete.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				Classroom.Delete(ObjectManager.getClassrooms().get(listClassrooms.getSelectedIndex()).getID());
//				reloadDropDowns();
//				listClassrooms.setSelectedIndex(-1);
//			}
//		});
		coursePanel.add(courseListLabel);
		coursePanel.add(courseCodeLabel);
		coursePanel.add(courseNameLabel);
		coursePanel.add(courseHoursLabel);
		coursePanel.add(courseProgramLabel);
		coursePanel.add(courseTermLabel);
		coursePanel.add(courseSectionsLabel);
		coursePanel.add(courseInstructorLabel);
		coursePanel.add(courseList);
		coursePanel.add(courseCode);
		coursePanel.add(courseName);
		coursePanel.add(courseHours);
		coursePanel.add(courseProgram);
		coursePanel.add(courseTerm);
		coursePanel.add(courseSections);
		coursePanel.add(courseInstructor);
		coursePanel.add(noLab);
		coursePanel.add(macLab);
		coursePanel.add(netLab);
		coursePanel.add(winLab);
		coursePanel.add(courseAdd);
		coursePanel.add(courseUpdate);
		coursePanel.add(courseDelete);
		courseManager.add(coursePanel);
	}
	private void ManageInstructors(){
		
	}
	private void ManagePrograms(){
		
	}
	private void reloadDropDowns(){
		ArrayList<Classroom> classrooms = ObjectManager.getClassrooms();
		classroomsModel.removeAllElements();
		for (int i = 0; i < classrooms.size(); i++) {
			String classroomName = classrooms.get(i).getWing() + "-" + classrooms.get(i).getRoom();
			classroomsModel.addElement(classroomName);
		}
	}
	
	private int getLabType(JRadioButton noLab, JRadioButton macLab, JRadioButton netLab, JRadioButton windowsLab) {
		if (noLab.isSelected() == true)
			return 0;
		else if (macLab.isSelected() == true)
			return 1;
		else if (netLab.isSelected() == true)
			return 2;
		else if (windowsLab.isSelected() == true)
			return 3;
		else
			return -1;
	}
}