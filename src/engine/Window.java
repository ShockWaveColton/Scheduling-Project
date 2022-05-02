package engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import objects.Classroom;
import objects.Course;
import objects.Instructor;
import objects.Lesson;
import objects.Program;
import objects.Schedule;

public class Window {
	
//	private ObjectManager objectManager;
	private static JPanel panel;
	
	private static DefaultComboBoxModel<Program> programsModel = new DefaultComboBoxModel<Program>();
	private static DefaultComboBoxModel<Instructor> instructorsModel = new DefaultComboBoxModel<Instructor>();
	private static DefaultComboBoxModel<Classroom> classroomsModel = new DefaultComboBoxModel<Classroom>();
	private static DefaultComboBoxModel<Course> coursesModel = new DefaultComboBoxModel<Course>();
	
	private static JLabel lastClickedTop = new JLabel();
	private static JLabel lastClickedMid = new JLabel();
	private static JLabel lastClickedBot = new JLabel();
	
	private static int courseProgramValue = 0; // Global so I can get it out of the button ActionListeners.
	private static int selectedTerm       = 0; // Global so it can be accessed by programs and instructors functions.
	
	private static JLabel[][][] scheduleLabel = new JLabel[5][9][3];
	private static boolean[][][] isClicked;
	private static boolean slotSelected = false;
	private static int daySelected;
	private static int timeSelected;
	
	public Window(ObjectManager objectManager) {
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 9; y++) {
				for (int z = 0; z < 3; z++) {
					scheduleLabel[x][y][z] = new JLabel();
				}
			}
		}
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
		    	ObjectManager.ClearData();
		    	if (FileIO.NewDatabase() == 0) 
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
		    	ObjectManager.ClearData();
		    	if (FileIO.LoadDatabase() == 0) {
		    		menuBar_Edit.setEnabled(true);
		    		reloadDropDowns();
		    	} else
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
		JMenuItem edit_Programs = new JMenuItem("Manage Programs");
		edit_Programs.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
		    	ManagePrograms();
	    	}
	    });
		JMenuItem edit_Instructors = new JMenuItem("Manage Instructors");
		edit_Instructors.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
		    	ManageInstructors();
	    	}
	    });
		JMenuItem edit_Classrooms = new JMenuItem("Manage Classrooms");
		edit_Classrooms.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
		    	ManageClassrooms();
	    	}
	    });
		JMenuItem edit_Courses = new JMenuItem("Manage Courses");
		edit_Courses.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
		    	ManageCourses();
	    	}
	    });
		menuBar_Edit.add(edit_Programs);
		menuBar_Edit.add(edit_Instructors);
		menuBar_Edit.add(edit_Classrooms);
		menuBar_Edit.add(edit_Courses);
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
		menuBar.setBounds(0, 0, 835, 25);
		JTabbedPane tabbedPane = new JTabbedPane();
		JPanel instructorPanel = new JPanel();
		JPanel programPanel = new JPanel();
		tabbedPane.setBounds(10, 30, 180, 60);
		instructorPanel.setLayout(null);
		programPanel.setLayout(null);
	    tabbedPane.setUI(new BasicTabbedPaneUI() {
	        private final Insets insets = new Insets(0, 0, 0, 0);
	        protected Insets getContentBorderInsets(int tab) {
	            return insets;
	        }
	    });
		tabbedPane.add("Instructors", instructorPanel);
		tabbedPane.add("Programs", programPanel);
		JLabel listCoursesLabel = new JLabel("Courses:");
		listCoursesLabel.setBounds    (470, 30, 100, 20);
				
		JLabel  courseDetails        = new JLabel("Course Details:");
		JLabel  courseName           = new JLabel("Name:");
		JLabel  courseInstructor     = new JLabel("Instructor:");
		JLabel  courseClassroom      = new JLabel("Classroom:");
		JLabel  courseHours          = new JLabel("Hours:");
		JLabel  courseApplyLabel     = new JLabel("<html><b>Select a Day/Time<br>to add course to schedule.");
		JButton courseApply          = new JButton("Apply");
		JButton courseDelete		 = new JButton("Delete");
		courseDetails.setBounds       (630, 60,  100, 40);
		courseName.setBounds          (630, 100, 200, 45);
		courseInstructor.setBounds    (630, 150, 200, 40);
		courseClassroom.setBounds     (630, 200, 200, 40);
		courseHours.setBounds         (630, 250, 200, 45);
		courseApplyLabel.setBounds(630, 320, 200, 40);
		courseApply.setBounds         (630, 320, 180, 40);
		courseDelete.setBounds        (630, 370, 180, 40);

		//This is a special comboBox for adding courses (thus everything) to the selected schedule.
		JComboBox<Course> listCourses = new JComboBox<Course>();
		listCourses.setRenderer(new ListCellRendererOverride());
		listCourses.setSelectedIndex(-1);
		listCourses.setBounds(470, 55, 140, 25);
		listCourses.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FillSidePanel(window, listCourses, courseName, courseInstructor, courseClassroom, courseHours, courseApplyLabel, courseApply);				
			}
		});

		JComboBox<Program> listPrograms = new JComboBox<Program>(programsModel);
		JComboBox<Instructor> listInstructors = new JComboBox<Instructor>(instructorsModel);

		JLabel listFallTermLabel = new JLabel("Fall:");
		listFallTermLabel.setBounds(200, 25, 100, 20);
		JLabel listWinterTermLabel = new JLabel("Winter:");
		listWinterTermLabel.setBounds(280, 25, 100, 20);
		JSlider listSemesters = new JSlider();
		listSemesters.setBounds(200, 40, 120, 50);
		listSemesters.setMinimum(1);
		listSemesters.setMaximum(2);
		listSemesters.setValue(1);
		listSemesters.setMajorTickSpacing(1);
		listSemesters.setPaintLabels(true);
		listSemesters.addChangeListener(new ChangeListener() {			
			@Override
			public void stateChanged(ChangeEvent e) {
			    JSlider slider = (JSlider)e.getSource();
				if (!slider.getValueIsAdjusting()) {
					selectedTerm = listSemesters.getValue();
					if (tabbedPane.getSelectedIndex() == 0) {				
						listFallTermLabel.setText("Fall:");
						listWinterTermLabel.setText("Winter:");
						Schedule.Display(listInstructors.getSelectedItem(), listSemesters.getValue());
						fillCourseList((Instructor)listInstructors.getSelectedItem(), listSemesters.getValue(), listCourses);
					} else if (tabbedPane.getSelectedIndex() == 1) {
						listFallTermLabel.setText("          Semester:");
						listWinterTermLabel.setText("");
						Schedule.Display(listPrograms.getSelectedItem(), listSemesters.getValue());
						fillCourseList((Program)listPrograms.getSelectedItem(), listSemesters.getValue(), listCourses);				
					}					
				}
			}
		});
	    tabbedPane.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				listSemesters.setValue(1);
				if(tabbedPane.getSelectedIndex() == 0) {
					listSemesters.setMaximum(2);
				} else if(tabbedPane.getSelectedIndex() == 1) {
					listSemesters.setMaximum(4);					
				}
				listSemesters.setLabelTable(null);
				listSemesters.setMajorTickSpacing(1);
				listSemesters.setPaintLabels(true);
			}
		});
		listPrograms.setRenderer(new ListCellRendererOverride());
		listPrograms.setSelectedIndex(-1);
		listPrograms.setBounds(5, 5, 160, 25);
		listPrograms.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (listPrograms.getSelectedIndex() >= 0) {
					Schedule.Display(listPrograms.getSelectedItem(), listSemesters.getValue());
					fillCourseList((Program)listPrograms.getSelectedItem(), listSemesters.getValue(), listCourses);
				}
			}
		});
		
		listInstructors.setRenderer(new ListCellRendererOverride());
		listInstructors.setSelectedIndex(-1);
		listInstructors.setBounds(5, 5, 160, 25);
		listInstructors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (listInstructors.getSelectedIndex() >= 0) {
					Schedule.Display(listInstructors.getSelectedItem(), listSemesters.getValue());
					fillCourseList((Instructor)listInstructors.getSelectedItem(), listSemesters.getValue(), listCourses);
				}
			}
		});
		
		// Pressing the 'Apply' button to add course to schedules:
		courseApply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Check to see if any other instructor is using the same room at the same time:
				ArrayList<Instructor> instructors = ObjectManager.getInstructors();
				Course course = (Course)listCourses.getSelectedItem();
				int instructorID = course.getInstructor();
				int term = 0; 
				int semester = listSemesters.getValue(); 
				if (semester == 2 || semester == 4)
					term = 1; // Term 1 is the lowest, so only add 1 if term 2 (semester 2 or 4) is selected.
				int confirmOverwrite = -1;
				for (int i = 0; i < instructors.size(); i++) {
					Instructor instructor = instructors.get(i);
					if (instructor.getID() != instructorID) { // Do not check the schedule of the selected course's instructor.
						Schedule schedule = ObjectManager.getSchedules().get(instructor.getSchedule()-1 + term); // -1 for 0-based index.						
						if (schedule.hasEvent(daySelected,timeSelected)) { // if true, then there is a potential collision, check which for classroom collision.
							Lesson scheduledLesson = schedule.getSpecificLesson(daySelected, timeSelected);
							Classroom courseClassroom = ObjectManager.getClassrooms().get(course.getClassroom());
							if (scheduledLesson.getClassroom() == courseClassroom) { // Classrooms match, conflict confirmed. Alert, and ask to overwrite.
								String instructorName = instructor.getFullName(); 
								confirmOverwrite = JOptionPane.showConfirmDialog(null, instructorName + " is teaching another class is here. Overwrite?", "Collision Detected", JOptionPane.YES_NO_OPTION);
								if (confirmOverwrite == JOptionPane.YES_OPTION) {
									AddToSchedule(course, term);
									break;
								}
							} 
						} 
					}
				}
				if (confirmOverwrite == -1) { // No class scheduled at this location, proceed with insertion.
					AddToSchedule(course, term);
				}
			}
		});
		courseDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// If a schedule block is selected, get the course (via the Lesson), and instructor
				// Remove the lesson from all appropriate schedules (this is why course is needed).
				if(slotSelected) {
					int scheduleID = 0;
					Instructor instructor = null;
					Program program = null;
					// Get scheduleID of selected object (instructor or program):
					if (tabbedPane.getSelectedIndex() == 0) { //Instructor Selected:
						instructor = (Instructor)listInstructors.getSelectedItem();
						scheduleID = instructor.getSchedule();
					} else { 								  //Program Selected:
						program = (Program)listPrograms.getSelectedItem();
						scheduleID = program.getSchedule();
					}
					int term = listSemesters.getValue() - 1;
					// Get the schedule of the selected object:
					Schedule schedule = null; 
					ArrayList<Schedule> schedules = ObjectManager.getSchedules();
					for (int i = 0; i < schedules.size(); i++) {
						if (schedules.get(i).getID() == scheduleID) {
							schedule = schedules.get(i + term);
							break;
						}
					}
					Lesson lesson = schedule.getSpecificLesson(daySelected, timeSelected);
					// TODO: Delete lessons from program when instructor selected.
					//Course course = lesson.getCourse();
					//int programID = course.getProgram();

					if (tabbedPane.getSelectedIndex() == 1 && lesson != null) // Still need instructor if program was selected
						instructor = lesson.getInstructor();
					// Get confirmation that they do infact want to remove the data.
					if (JOptionPane.showConfirmDialog(null, "Warning! You are about to permanently remove this lesson from the schedule.\n"
							+ "THIS ACTION CANNOT BE UNDONE!\nDo you wish to proceed?", "Delete Lesson?", JOptionPane.YES_NO_OPTION) == 0) {
						//Remove lesson from selected block of selected object.
						schedule.DeleteScheduledEvent(daySelected, timeSelected);
						//Remove lesson from the other matching schedule:
						if (tabbedPane.getSelectedIndex() == 0) //Instructor Selected:
							schedule = schedules.get(program.getSchedule());
						else
							schedule = schedules.get(instructor.getSchedule());
						schedule.DeleteScheduledEvent(daySelected, timeSelected);
						// Redraw the schedule of the selected object after the change:
						if (tabbedPane.getSelectedIndex() == 0) //Instructor Selected:
							DrawSchedule(instructor.getSchedule());
						else
							DrawSchedule(program.getSchedule());						
					}
				}
			}
		});
		panel.add(menuBar);
		panel.add(tabbedPane);
		panel.add(listCoursesLabel);
		instructorPanel.add(listInstructors);
		programPanel.add(listPrograms);
		panel.add(listCourses);
		panel.add(listWinterTermLabel);
		panel.add(listFallTermLabel);
		panel.add(listSemesters);
		panel.add(courseDetails);
		panel.add(courseName);
		panel.add(courseInstructor);
		panel.add(courseClassroom);
		panel.add(courseHours);
		panel.add(courseApplyLabel);
		panel.add(courseApply);
		panel.add(courseDelete);

		panel.setVisible(true);
		window.add(panel);
		window.setVisible(true);
	}

	private void AddToSchedule(Course course, int term) {
		//TODO: Remove lessons from all appropriate schedules when overwriting.
		int scheduleID = 0;
		//Adding course to instructor schedule:
		Instructor instructor = null;
		for (int i = 0; i < instructorsModel.getSize(); i++) {
			if (instructorsModel.getElementAt(i).getID() == course.getInstructor()) {
				instructor = instructorsModel.getElementAt(i);
				break;
			}
		}
		Schedule schedule = ObjectManager.getSchedules().get(instructor.getSchedule()-1 + term);//-1 for zero-based index, + term for winter term.
		scheduleID = schedule.getID();
		schedule.setScheduleEvent(daySelected, timeSelected, course);
		// Then the appropriate program schedules as needed:
		int program_id = (course).getProgram();
		switch (program_id) {
			case 1: {
				AssignCourseProgramSchedule(0, course);
				break;
			} case 2: {
				AssignCourseProgramSchedule(1, course);
				break;
			} case 3: {
				AssignCourseProgramSchedule(0, course);
				AssignCourseProgramSchedule(1, course);
				break;
			} case 4: {
				AssignCourseProgramSchedule(2, course);
				break;
			} case 5: {
				AssignCourseProgramSchedule(0, course);
				AssignCourseProgramSchedule(2, course);
				break;
			} case 6: {
				AssignCourseProgramSchedule(1, course);
				AssignCourseProgramSchedule(2, course);
				break;
			} case 7: {
				AssignCourseProgramSchedule(0, course);
				AssignCourseProgramSchedule(1, course);
				AssignCourseProgramSchedule(2, course);
				break;
			} case 8: {
				AssignCourseProgramSchedule(3, course);
				break;
			} case 9: {
				AssignCourseProgramSchedule(0, course);
				AssignCourseProgramSchedule(3, course);
				break;
			} case 10:{
				AssignCourseProgramSchedule(1, course);
				AssignCourseProgramSchedule(3, course);
				break;
			} case 11: {
				AssignCourseProgramSchedule(0, course);
				AssignCourseProgramSchedule(1, course);
				AssignCourseProgramSchedule(3, course);
				break;
			} case 12: {
				AssignCourseProgramSchedule(2, course);
				AssignCourseProgramSchedule(3, course);
				break;
			} case 13: {
				AssignCourseProgramSchedule(0, course);
				AssignCourseProgramSchedule(2, course);
				AssignCourseProgramSchedule(3, course);
				break;
			} case 14: {
				AssignCourseProgramSchedule(1, course);
				AssignCourseProgramSchedule(2, course);
				AssignCourseProgramSchedule(3, course);
				break;
			} case 15: {
				AssignCourseProgramSchedule(0, course);
				AssignCourseProgramSchedule(1, course);
				AssignCourseProgramSchedule(2, course);
				AssignCourseProgramSchedule(3, course);
				break;
			} default:
				throw new IllegalArgumentException("Unexpected value: " + program_id);
		}
		DrawSchedule(scheduleID);
	}

	protected void FillSidePanel(JFrame window, JComboBox<Course> listCourses, JLabel courseName, JLabel courseInstructor, JLabel courseClassroom, JLabel courseHours, JLabel courseSelectSchedule, JButton courseApply) {
		if (listCourses.getSelectedIndex() > -1) {
			Course course = (Course)listCourses.getSelectedItem();
			String courseNameText =       course.getName();
			String courseCodeText =       course.getFullName();
			String courseInstructorText = GetCourseInstructorName(course); // Needs to be a function because instructormodel doesn't match sorted list
			String courseClassroomText =  GetCourseClassroomName(course); // Same as above.
			String courseHoursText =      CalculateCourseHours(course);
			courseName.setText      ("<html>Name:<br>"       + courseNameText);
			courseInstructor.setText("<html>Instructor:<br>" + courseInstructorText);
			courseClassroom.setText ("<html>Classroom:<br>"  + courseClassroomText);
			courseHours.setText     ("<html>Hours:<br>Scheduled / Total (remaining):<br>" + courseHoursText);
			String applyDayString = "";
			String applyTimeString = "";
			if (slotSelected) {
				for (int x = 0; x < 5; x++) {
					for (int y = 0; y < 9; y++) {
						if (isClicked[x][y][0]) {
							switch (x) {
								case 0: {
									applyDayString = "Monday";
									break;
								} case 1: {
									applyDayString = "Tuesday";
									break;
								} case 2: {
									applyDayString = "Wednesday";
									break;
								} case 3: {
									applyDayString = "Thursday";
									break;
								} case 4: {
									applyDayString = "Friday";
									break;
								} default:
									throw new IllegalArgumentException("Unexpected value: " + x);
							}
							switch (y) {
								case 0: {
									applyTimeString = "8:30am";
									break;
								} case 1: {
									applyTimeString = "9:30am";
									break;
								} case 2: {
									applyTimeString = "10:30am";
									break;
								} case 3: {
									applyTimeString = "11:30am";
									break;
								} case 4: {
									applyTimeString = "12:30pm";
									break;
								} case 5: {
									applyTimeString = "1:30pm";
									break;
								} case 6: {
									applyTimeString = "2:30pm";
									break;
								} case 7: {
									applyTimeString = "3:30pm";
									break;
								} case 8: {
									applyTimeString = "4:30pm";
									break;
								} default:
									throw new IllegalArgumentException("Unexpected value: " + x);
							}
						}
					}
				}
				courseApply.setVisible(true);
				courseSelectSchedule.setVisible(false);
			} else {
				courseApply.setVisible(false);
				courseSelectSchedule.setVisible(true);						
			}
			courseApply.setText("<html>Apply " + courseCodeText + "<br> to " + applyDayString + " at " + applyTimeString + ".");
			window.setSize(845, 840);
		} else {
			window.setSize(635, 840);
		}
	}

	private String GetCourseInstructorName(Course course) {
		// Since the list of instructors in the instructorModel will likely not match
		// the database order (since the database has ORDER BY), we need to loop through the
		// instructorModel to find the matching ID, hence this function is required, instead of a simple statement.
		String instructorName= "";
		int instructorID = course.getInstructor();
		for (int i = 0; i < instructorsModel.getSize(); i++) {
			if (instructorsModel.getElementAt(i).getID() == instructorID)
				instructorName = instructorsModel.getElementAt(i).getFullName();
		}
		return instructorName;
	}
	
	private String GetCourseClassroomName(Course course) {
		// See function above, same explanation applies for classrooms here.
		String classroomName= "";
		int classroomID = course.getClassroom();
		for (int i = 0; i < classroomsModel.getSize(); i++) {
			if (classroomsModel.getElementAt(i).getID() == classroomID)
				classroomName = classroomsModel.getElementAt(i).getName();
		}
		return classroomName;
	}
	
	
	private String CalculateCourseHours(Course course) {
		// This function returns three values:
		// ... the hours currently scheduled...
		int courseScheduledHours = CalculateScheduledCourseHours(course);
		// ... the total hours of a program...
		int courseTotalHours = course.getHours();
		// ... and the hours remaining to schedule.
		int courseRemainingHours = courseTotalHours - courseScheduledHours; 
		// These are converted to single string and returned to caller:
		String courseHours = courseScheduledHours + " / " + courseTotalHours + " ("+ courseRemainingHours + ")";
		return courseHours;
	}
	
	private int CalculateScheduledCourseHours(Course course) {
		int courseScheduledHours = 0;
		int courseInstructorID = course.getInstructor();
		Instructor courseInstructor = null;
		for (int i = 0; i < instructorsModel.getSize(); i++) {
			if (instructorsModel.getElementAt(i).getID() == courseInstructorID) {
				courseInstructor = instructorsModel.getElementAt(i);
				break;
			}
		}
		int instructorScheduleID = 0;
		int semester = course.getSemester();
		int term = 0; 
		if (semester == 2 || semester == 4) // If course is in winter term, set term to 1.
			term = 1; 
		if (courseInstructor != null)
			instructorScheduleID = (courseInstructor.getSchedule()) + term;
		Schedule schedule = ObjectManager.getSchedules().get(instructorScheduleID - 1);  //-1 to account for 0-based index. 
		if(instructorScheduleID > 0) {
			for (int x = 0; x < 5; x++) {
				for (int y = 0; y < 9; y++) {
					if (schedule.getSpecificLesson(x, y) != null) {
						if (schedule.getSpecificLesson(x, y).getCourse().getID() == course.getID())
							courseScheduledHours++;						
					}
				}	
			}
			return courseScheduledHours *= 15;
		}
		return 0;
	}


	public static void DrawSchedule(int schedule_id) {
		Schedule schedule = null;
		ArrayList<Schedule> schedules = ObjectManager.getSchedules();
		for (int i = 0; i < schedules.size(); i++) {
			if (schedules.get(i).getID() == schedule_id)
				schedule = ObjectManager.getSchedules().get(i);
		}
		Border topBorder = BorderFactory.createMatteBorder(2, 2, 0, 2, Color.black);
		Border midBorder = BorderFactory.createMatteBorder(0, 2, 0, 2, Color.black);
		Border botBorder = BorderFactory.createMatteBorder(0, 2, 2, 2, Color.black);
		isClicked = new boolean[5][9][3];

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
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 5; x++) {
				for (int z = 0; z < 3; z++) {
					//scheduleLabel[x][y][z] = new JLabel();
					scheduleLabel[x][y][z].setBounds(labelStartX+(x*100), labelStartY+(z*25)+(y*75), cellWidth, cellHeight);
					scheduleLabel[x][y][z].setOpaque(true);
					switch (z) {
					case 0:
						scheduleLabel[x][y][z].setBorder(topBorder);
						break;
					case 1:
						scheduleLabel[x][y][z].setBorder(midBorder);
						break;
					case 2:
						scheduleLabel[x][y][z].setBorder(botBorder);
						break;
					default:
						throw new IllegalArgumentException("Unexpected value: " + z);
					}
					scheduleLabel[x][y][z].setBackground(new Color(255,255,255,255));
					panel.add(scheduleLabel[x][y][z]);
					
					//Workaround to avoid "Local variable defined in an enclosing scope must be final or effectively final"
					final int innerX = x;
					final int innerY = y;
					scheduleLabel[x][y][z].addMouseListener(new MouseAdapter() {
						public void mousePressed(MouseEvent e) {
							lastClickedTop.setBackground(new Color(255, 255, 255, 255));
							lastClickedMid.setBackground(new Color(255, 255, 255, 255));
							lastClickedBot.setBackground(new Color(255, 255, 255, 255));
							//UnClickLabels(scheduleLabel, tempSchedule);
							scheduleLabel[innerX][innerY][0].setBackground(new Color(255, 255, 0, 255));
							scheduleLabel[innerX][innerY][1].setBackground(new Color(255, 255, 0, 255));
							scheduleLabel[innerX][innerY][2].setBackground(new Color(255, 255, 0, 255));
							lastClickedTop = scheduleLabel[innerX][innerY][0];
							lastClickedMid = scheduleLabel[innerX][innerY][1];
							lastClickedBot = scheduleLabel[innerX][innerY][2];
							isClicked[innerX][innerY][0] = true;
							isClicked[innerX][innerY][1] = true;
							isClicked[innerX][innerY][2] = true;
							slotSelected = true;
							daySelected = innerX;
							timeSelected = innerY;
							//TODO: fix this mess (FillSidePanel by click on schedule block)
							// 		Uncommenting the line below would make the Right panel fill when any schedule time is clicked.
							// 		but NONE of the parameters exist in this scope.
							//		Perhaps a series of getters would work here?
							//FillSidePanel(this, listCourses, courseName, courseInstructor, courseClassroom, courseHours, courseSelectSchedule, courseApply);							
						}
						public void mouseEntered(MouseEvent e) {
							if (!(isClicked[innerX][innerY][0] || isClicked[innerX][innerY][1] || isClicked[innerX][innerY][2])) {
								scheduleLabel[innerX][innerY][0].setBackground(new Color(192, 192, 192, 255));
								scheduleLabel[innerX][innerY][1].setBackground(new Color(192, 192, 192, 255));
								scheduleLabel[innerX][innerY][2].setBackground(new Color(192, 192, 192, 255));
							}
						}
						public void mouseExited(MouseEvent e) {
							if (!(isClicked[innerX][innerY][0] || isClicked[innerX][innerY][1] || isClicked[innerX][innerY][2])) {
								scheduleLabel[innerX][innerY][0].setBackground(new Color(255, 255, 255, 255));
								scheduleLabel[innerX][innerY][1].setBackground(new Color(255, 255, 255, 255));
								scheduleLabel[innerX][innerY][2].setBackground(new Color(255, 255, 255, 255));
							}
						}
					});
				}
				//Set the labels in each box, if there is a class scheduled at that time:
				if (schedule != null)
				{
					if (schedule.hasEvent(x,y)) {
						scheduleLabel[x][y][0].setText(schedule.getSpecificLesson(x, y).getCourse().getFullName());
						scheduleLabel[x][y][1].setText(schedule.getSpecificLesson(x, y).getInstructor().getFullName());
						scheduleLabel[x][y][2].setText(schedule.getSpecificLesson(x, y).getClassroom().getName());					
					} else {
						scheduleLabel[x][y][0].setText("");
						scheduleLabel[x][y][1].setText("");
						scheduleLabel[x][y][2].setText("");
					}
				}
			}			
		}
		UnClickLabels(scheduleLabel, schedule);
		panel.revalidate();
		panel.repaint();
	}
	private static void UnClickLabels(JLabel[][][] label, Schedule schedule){
		slotSelected = false;
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 9; y++) {
				if (schedule != null) {					
					if (schedule.hasEvent(x,y)) {
						label[x][y][0].setText(schedule.getSpecificLesson(x, y).getCourse().getFullName());
						label[x][y][1].setText(schedule.getSpecificLesson(x, y).getInstructor().getFullName());
						label[x][y][2].setText(schedule.getSpecificLesson(x, y).getClassroom().getName());					
					} else {
						label[x][y][0].setText("");
						label[x][y][1].setText("");
						label[x][y][2].setText("");
					}
				}
				for (int z = 0; z < 3; z++) {					
					isClicked[x][y][z] = false;
//					label[x][y][z].setBackground(new Color(255, 255, 255, 255));
				}
			}
		}
	}
	
	private void ManagePrograms() {
		JFrame programManager = new JFrame("Program Manager");
		programManager.setMinimumSize(new Dimension());
		programManager.setVisible(true);
		programManager.setBounds(0, 0, 260, 200);
		programManager.setLocationRelativeTo(null);

		JPanel programPanel = new JPanel();
		programPanel.setLayout(null);
		programPanel.setPreferredSize(new Dimension(300, 300));

		JLabel programListLabel = new JLabel("Programs:");
		JLabel programNameLabel = new JLabel("Name:");
		programListLabel.setBounds  (10,  0, 100, 30);
		programNameLabel.setBounds  (10, 25, 100, 30);

		JTextField programName = new JTextField();
		programName.setBounds(80, 30, 120, 20);

		JComboBox<Program> programList = new JComboBox<Program>(programsModel);
		programList.setSelectedIndex(-1);
		programList.setRenderer(new ListCellRendererOverride());
		programList.setBounds(80, 5, 145, 20);
		programList.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (programList.getSelectedIndex() >= 0) {
					//Schedule.Display(programList.getSelectedItem().toString());
					programName.setText(ObjectManager.getPrograms().get(programList.getSelectedIndex()).getName()); 
				}
			}			
		});
		JButton programAdd = new JButton("Create");
		programAdd.setBounds(10, 100, 225, 25);
		programAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!programName.getText().equals("")) {
					ArrayList<Program> programs = ObjectManager.getPrograms();
					for (int i = 0; i < programs.size(); i++) {
						if (programName.getText().equals(programs.get(i).getName())) {
							JOptionPane.showMessageDialog(null, "Program is already in the database!");
							return;
						}
					}
					programList.addItem(Program.Create(programName.getText(), selectedTerm));
					
					if (programList.getItemCount() > 1)
						programList.setSelectedIndex(programs.size()-1);
					programList.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "You must fill in all empty fields.");
				}
			}
		});
		JButton programUpdate = new JButton("Update");
		programUpdate.setBounds(10, 130, 110, 25);
		programUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!programName.getText().equals("")) {
					if (!(programList.getSelectedIndex() == -1)) {
						int ID = ObjectManager.getPrograms().get(programList.getSelectedIndex()).getID();
						int schedule_id = ObjectManager.getPrograms().get(programList.getSelectedIndex()).getSchedule();
						Program.Update(programList.getSelectedIndex(), ID, programName.getText(), schedule_id);
						reloadDropDowns();
					} else
						JOptionPane.showMessageDialog(null, "You must select a program above to update.");
				} else
					JOptionPane.showMessageDialog(null, "You must fill in all empty fields.");
			}
		});
		JButton programDelete = new JButton("Delete");
		programDelete.setBounds(125, 130, 110, 25);
		programDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Program.Delete(ObjectManager.getPrograms().get(programList.getSelectedIndex()).getID());
				reloadDropDowns();
				programList.setSelectedIndex(-1);
			}
		});
		programPanel.add(programListLabel);
		programPanel.add(programNameLabel);
		programPanel.add(programList);
		programPanel.add(programName);
		programPanel.add(programAdd);
		programPanel.add(programUpdate);
		programPanel.add(programDelete);
		programManager.add(programPanel);
	}
	private void ManageInstructors(){
		JFrame instructorManager = new JFrame("Instructor Manager");
		instructorManager.setMinimumSize(new Dimension());
		instructorManager.setVisible(true);
		instructorManager.setBounds(0, 0, 230, 235);
		instructorManager.setLocationRelativeTo(null);
		
		JPanel instructorPanel = new JPanel();
		instructorPanel.setLayout(null);
		instructorPanel.setPreferredSize(new Dimension(300, 300));
		
		JLabel instructorListLabel = new JLabel("Instructors:");
		JLabel instructorWnumberLabel = new JLabel("ID (W#):");
		JLabel instructorFirstnameLabel = new JLabel("First Name:");
		JLabel instructorLastnameLabel = new JLabel("Last Name:");
		JLabel instructorEmailLabel = new JLabel("E-mail:");
		JLabel instructorPhoneLabel = new JLabel("Phone:");
		instructorListLabel.setBounds     (10,   5, 100, 20);
		instructorWnumberLabel.setBounds  (10,  30, 100, 20);
		instructorFirstnameLabel.setBounds(10,  50, 100, 20);
		instructorLastnameLabel.setBounds (10,  70, 100, 20);
		instructorPhoneLabel.setBounds    (10,  90, 100, 20);
		instructorEmailLabel.setBounds    (10, 110, 100, 20);

		JTextField instructorWnumber =   new JTextField();
		JTextField instructorFirstname = new JTextField();
		JTextField instructorLastname =  new JTextField();
		JTextField instructorPhone =     new JTextField();
		JTextField instructorEmail =     new JTextField();
		instructorWnumber.setBounds  (80,  30, 105, 20);
		instructorFirstname.setBounds(80,  50, 105, 20);
		instructorLastname.setBounds (80,  70, 105, 20);
		instructorPhone.setBounds    (80, 110, 105, 20);
		instructorEmail.setBounds    (80, 90, 105, 20);
		
		JComboBox<Instructor> instructorList = new JComboBox<Instructor>(instructorsModel);
		instructorList.setSelectedIndex(-1);
		instructorList.setRenderer(new ListCellRendererOverride());
		instructorList.setBounds(80, 5, 125, 20);
		instructorList.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (instructorList.getSelectedIndex() >= 0) {
					//Schedule.Display(instructorList.getSelectedItem().toString());
					instructorWnumber.setText  (ObjectManager.getInstructors().get(instructorList.getSelectedIndex()).getW_Number());				
					instructorFirstname.setText(ObjectManager.getInstructors().get(instructorList.getSelectedIndex()).getFirstName());				
					instructorLastname.setText (ObjectManager.getInstructors().get(instructorList.getSelectedIndex()).getLastName());				
					instructorPhone.setText    (ObjectManager.getInstructors().get(instructorList.getSelectedIndex()).getPhone());				
					instructorEmail.setText    (ObjectManager.getInstructors().get(instructorList.getSelectedIndex()).getEmail());
					instructorEmail.setCaretPosition(0);
				}
			}
		});
		JButton instructorAdd = new JButton("Add");
		instructorAdd.setBounds(10, 135, 195, 25);
		instructorAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!instructorWnumber.getText().equals("") &&
						!instructorFirstname.getText().equals("") &&
						!instructorLastname.getText().equals("") &&
						!instructorPhone.getText().equals("") &&
						!instructorEmail.getText().equals("") 
						){
					ArrayList<Instructor> instructors = ObjectManager.getInstructors();
					for (int i = 0; i < instructors.size(); i++) {
						// If Instructor ID or BOTH first name and last name are already saved:
						if (instructorWnumber.getText().equals(instructors.get(i).getW_Number())) {							
							JOptionPane.showMessageDialog(null, "This W-Number is already in database!");
							return;
						} else if (instructorEmail.getText().equals(instructors.get(i).getEmail())) {
							JOptionPane.showMessageDialog(null, "This E-Mail is already in database!");
							return;
						}
					}
					instructorList.addItem(Instructor.Create(instructorWnumber.getText(), instructorFirstname.getText(), instructorLastname.getText(), instructorPhone.getText(), instructorEmail.getText(), selectedTerm));
					if (instructorList.getItemCount() > 1)
						instructorList.setSelectedIndex(instructors.size()-1);
					instructorList.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "You must fill in all fields.");
				}
			}
		});
		JButton instructorUpdate = new JButton("Update");
		instructorUpdate.setBounds(10, 165, 95, 25);
		instructorUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!instructorWnumber.getText().equals("") &&
						!instructorFirstname.getText().equals("") &&
						!instructorLastname.getText().equals("") &&
						!instructorPhone.getText().equals("") &&
						!instructorEmail.getText().equals("")
						){
					if (!(instructorList.getSelectedIndex() == -1)) {
						int instructorID = ObjectManager.getInstructors().get(instructorList.getSelectedIndex()).getID();						
						int instructorSchedule = ObjectManager.getInstructors().get(instructorList.getSelectedIndex()).getSchedule();						
						Instructor.Update(instructorList.getSelectedIndex(),
								instructorID,
								instructorWnumber.getText(),
								instructorFirstname.getText(),
								instructorLastname.getText(),
								instructorEmail.getText(),
								instructorPhone.getText(),
								instructorSchedule
								);
						instructorList.setSelectedIndex(-1);
						reloadDropDowns();
					} else {
						JOptionPane.showMessageDialog(null, "You must select a classroom above to update.");
					}
				} else {
					JOptionPane.showMessageDialog(null, "You must fill in all fields.");
				}
			}
		});
		JButton instructorDelete = new JButton("Delete");
		instructorDelete.setBounds(110, 165, 95, 25);
		instructorDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Instructor.Delete(ObjectManager.getInstructors().get(instructorList.getSelectedIndex()).getID());
				reloadDropDowns();
				instructorList.setSelectedIndex(-1);
			}
		});
		instructorPanel.add(instructorListLabel);
		instructorPanel.add(instructorList);
		instructorPanel.add(instructorFirstnameLabel);		
		instructorPanel.add(instructorWnumberLabel);
		instructorPanel.add(instructorFirstnameLabel);
		instructorPanel.add(instructorLastnameLabel);
		instructorPanel.add(instructorPhoneLabel);
		instructorPanel.add(instructorEmailLabel);
		instructorPanel.add(instructorWnumber);
		instructorPanel.add(instructorFirstname);
		instructorPanel.add(instructorLastname);
		instructorPanel.add(instructorPhone);
		instructorPanel.add(instructorEmail);
		instructorPanel.add(instructorAdd);
		instructorPanel.add(instructorUpdate);
		instructorPanel.add(instructorDelete);
		instructorManager.add(instructorPanel);
	}
	private void ManageClassrooms(){
		//Builds a new Frame when Edit>>Manage Classrooms is selected:
		JFrame classroomManager = new JFrame("Classroom Manager");
		classroomManager.setMinimumSize(new Dimension());
		classroomManager.setVisible(true);
		classroomManager.setBounds(0, 0, 260, 270);
		classroomManager.setLocationRelativeTo(null);

		JPanel classroomPanel = new JPanel();
		classroomPanel.setLayout(null);
		classroomPanel.setPreferredSize(new Dimension(300, 300));

		JLabel classroomListLabel = new JLabel("Classrooms:");
		JLabel classroomWingLabel = new JLabel("Campus Wing:");
		JLabel classroomNumberLabel = new JLabel("Room Number:");
		classroomListLabel.setBounds  (10,  0, 100, 30);
		classroomWingLabel.setBounds  (10, 25, 100, 30);
		classroomNumberLabel.setBounds(10, 45, 100, 30);

		JTextField classroom_wing = new JTextField();
		JTextField classroom_number = new JTextField();
		classroom_wing.setBounds(100, 30, 100, 20);
		classroom_number.setBounds(100, 50, 100, 20);

		JRadioButton noLab = new JRadioButton("Not a Lab");
		JRadioButton macLab = new JRadioButton("Mac Lab");
		JRadioButton netLab = new JRadioButton("Networking Lab");
		JRadioButton winLab = new JRadioButton("Windows Lab");
		JRadioButton hardLab = new JRadioButton("Hardware Lab");
		noLab.setSelected(true);
		noLab.setBounds(10, 70, 150, 20);
		macLab.setBounds(10, 90, 150, 20);
		netLab.setBounds(10, 110, 150, 20);
		winLab.setBounds(10, 130, 150, 20);
		hardLab.setBounds(10, 150, 150, 20);

		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(noLab);
		radioGroup.add(macLab);
		radioGroup.add(netLab);
		radioGroup.add(winLab);
		radioGroup.add(hardLab);

		JComboBox<Classroom> classroomList = new JComboBox<Classroom>(classroomsModel);
		classroomList.setSelectedIndex(-1);
		classroomList.setRenderer(new ListCellRendererOverride());
		classroomList.setBounds(100, 5, 125, 20);
		classroomList.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (classroomList.getSelectedIndex() >= 0) {
					//Schedule.Display(classroomList.getSelectedItem().toString());
					classroom_wing.setText(ObjectManager.getClassrooms().get(classroomList.getSelectedIndex()).getWing()); 
					classroom_number.setText(ObjectManager.getClassrooms().get(classroomList.getSelectedIndex()).getRoom());
					switch (ObjectManager.getClassrooms().get(classroomList.getSelectedIndex()).getLab()) {
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
					case 4: 
						hardLab.setSelected(true);
						break;
					default:
						throw new IllegalArgumentException("Unexpected value: " + ObjectManager.getClassrooms().get(classroomList.getSelectedIndex()).getLab());
					}
				}
			}
		});
		JButton classroomAdd = new JButton("Add");
		classroomAdd.setBounds(10, 170, 225, 25);
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
					int labType = GetLabType(noLab, macLab, netLab, winLab, hardLab);
					Classroom.Create(classroom_wing.getText(), classroom_number.getText(), labType);
					reloadDropDowns();
					if (classroomList.getItemCount() > 1)
						classroomList.setSelectedIndex(classrooms.size()-1);
					classroomList.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "You must fill in Wing and Number.");
				}
			}
		});
		JButton classroomUpdate = new JButton("Update");
		classroomUpdate.setBounds(10, 200, 110, 25);
		classroomUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!classroom_number.getText().equals("") && !classroom_wing.getText().equals("")) {
					if (!(classroomList.getSelectedIndex() == -1)) {
						int labType = GetLabType(noLab, macLab, netLab, winLab, hardLab);
						int ID = ObjectManager.getClassrooms().get(classroomList.getSelectedIndex()).getID();
						Classroom.Update(classroomList.getSelectedIndex(), ID, classroom_wing.getText(), classroom_number.getText(), labType);
						reloadDropDowns();
					} else {
						JOptionPane.showMessageDialog(null, "You must select a classroom above to update.");
					}
				} else {
					JOptionPane.showMessageDialog(null, "You must fill in all empty fields.");
				}
			}
		});
		JButton classroomDelete = new JButton("Delete");
		classroomDelete.setBounds(125, 200, 110, 25);
		classroomDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Classroom.Delete(ObjectManager.getClassrooms().get(classroomList.getSelectedIndex()).getID());
				reloadDropDowns();
				classroomList.setSelectedIndex(-1);
			}
		});
		classroomPanel.add(classroomListLabel);
		classroomPanel.add(classroomWingLabel);
		classroomPanel.add(classroomNumberLabel);
		classroomPanel.add(classroomList);
		classroomPanel.add(classroom_wing);
		classroomPanel.add(classroom_number);
		classroomPanel.add(noLab);
		classroomPanel.add(macLab);
		classroomPanel.add(netLab);
		classroomPanel.add(winLab);
		classroomPanel.add(hardLab);
		classroomPanel.add(classroomAdd);
		classroomPanel.add(classroomUpdate);
		classroomPanel.add(classroomDelete);
		classroomManager.add(classroomPanel);
	}
	private void ManageCourses(){
		//Builds a new Frame when Edit>>Manage Courses is selected:
		JFrame courseManager = new JFrame("Course Manager");
		courseManager.setVisible(true);
		courseManager.setBounds(0, 0, 265, 440);
		courseManager.setLocationRelativeTo(null);

		JPanel coursePanel = new JPanel();
		coursePanel.setLayout(null);
		coursePanel.setPreferredSize(new Dimension(300, 300));

		JLabel courseListLabel = new JLabel("Courses:");
		JLabel courseCodeLabel = new JLabel("Course Code:");
		JLabel courseNameLabel = new JLabel("Course Name:");
		JLabel courseHoursLabel = new JLabel("Course Hours:");
		JLabel courseSectionLabel = new JLabel("Section #:");
		JLabel courseInstructorLabel = new JLabel("Instructor:");
		JLabel courseProgramLabel = new JLabel("Program:");
		JLabel courseSemesterLabel = new JLabel("Semester:");
		JLabel courseClassroomLabel = new JLabel("Classroom:");
		courseListLabel.setBounds(10,  5, 100, 20);
		courseCodeLabel.setBounds(10, 30, 100, 20);
		courseNameLabel.setBounds(10, 50, 100, 20);
		courseHoursLabel.setBounds(10, 70, 100, 20);
		courseSectionLabel.setBounds(10, 90, 100, 20);
		courseInstructorLabel.setBounds(10, 110, 100, 20);
		courseProgramLabel.setBounds(10, 130, 100, 20);
		courseSemesterLabel.setBounds(10, 165, 100, 20);
		courseClassroomLabel.setBounds(10, 310, 100, 20);
		JTextField courseCode = new JTextField();
		JTextField courseName = new JTextField();
		JTextField courseHours = new JTextField();
		JTextField courseSection = new JTextField();
		JComboBox<Instructor> courseInstructor = new JComboBox<Instructor>(instructorsModel);
		// This button will create a frame to programmatically create JCheckboxes for all programs
		JButton coursePrograms = new JButton("Programs...");
		coursePrograms.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame courseProgramFrame = new JFrame("Select program(s)");
				courseProgramFrame.setVisible(true);

				JPanel courseProgramPanel = new JPanel();
				courseProgramPanel.setLayout(null);
				courseProgramFrame.add(courseProgramPanel);
				ArrayList<JCheckBox> programList = new ArrayList<JCheckBox>();
				for (int i = 0; i < programsModel.getSize(); i++) {
					programList.add(new JCheckBox(programsModel.getElementAt(i).getName()));
					programList.get(i).setBounds(10, 10 + i * 20, 140, 20);
					courseProgramPanel.add(programList.get(i));
				
				}
				//Set selected status of checkboxes when frame is loaded:
				int tempcourseProgramValue = courseProgramValue;
				for (int i = programList.size() - 1; i > 0; i--) {
					if (tempcourseProgramValue > 0) {
						if (Math.pow(2, i) <= tempcourseProgramValue) {
							programList.get(i).setSelected(true);
							tempcourseProgramValue -= (Math.pow(2, i));
						}
						if (tempcourseProgramValue == 1) {
							programList.get(0).setSelected(true);
							tempcourseProgramValue -= (1);
						}
					}
				}
				JButton courseProgramSubmit = new JButton("Submit");
				courseProgramSubmit.setBounds(10, programList.size()*20+15, 100, 30);
				courseProgramSubmit.addActionListener(new ActionListener() {					
					@Override
					public void actionPerformed(ActionEvent e) {
						courseProgramValue = 0;
						if (programList.get(0).isSelected())
							courseProgramValue++;
						for (int i = 1; i < programList.size(); i++) {
							if (programList.get(i).isSelected()) {
								double value = Math.pow(2, i);
								courseProgramValue += value;
							}
						}
						courseProgramFrame.dispose();
					}					
				});
				courseProgramPanel.add(courseProgramSubmit);
				courseProgramFrame.setBounds(0, 0, 230, programList.size()*20+95);
				courseProgramFrame.setLocationRelativeTo(null);
			}
		});
		JSlider courseSemester = new JSlider();
		JComboBox<Classroom> courseClassroom = new JComboBox<Classroom>();
		courseCode.setBounds(100, 30, 140, 20);
		courseName.setBounds(100, 50, 140, 20);
		courseHours.setBounds(100, 70, 140, 20);
		courseSection.setBounds(100, 90, 140, 20);
		courseInstructor.setRenderer(new ListCellRendererOverride());
		courseInstructor.setBounds(100, 110, 140, 20);
		//courseProgram.setRenderer(new ListCellRendererOverride());
		//courseProgram.setBounds(100, 130, 140, 20);
		coursePrograms.setBounds(100, 130, 140, 20);
		courseSemester.setBounds(100, 150, 140, 50);
		courseSemester.setMinimum(1);
		courseSemester.setMaximum(4);
		courseSemester.setMajorTickSpacing(1);
		courseSemester.setPaintLabels(true);
		courseClassroom.setRenderer(new ListCellRendererOverride());
		courseClassroom.setBounds(100, 310, 140, 20); //Classroom comes after the buttons on the frame,
													  //But since buttons reference the combo, this must come first.
		JRadioButton noLab = new JRadioButton("No Lab required");
		JRadioButton macLab = new JRadioButton("Requires Mac Lab");
		JRadioButton netLab = new JRadioButton("Requires Networking Lab");
		JRadioButton winLab = new JRadioButton("Requires Windows Lab");
		JRadioButton hardLab = new JRadioButton("Requires Hardware Lab");
		noLab.setBounds(10, 200, 180, 20);		
		noLab.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				courseClassroom.removeAllItems();
				for(int i = 0; i < classroomsModel.getSize(); i++) {
					if (ObjectManager.getClassrooms().get(i).getLab() == 0)
						courseClassroom.addItem(ObjectManager.getClassrooms().get(i));
				}
			}
		});
		macLab.setBounds(10, 220, 180, 20);
		macLab.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				courseClassroom.removeAllItems();
				for(int i = 0; i < classroomsModel.getSize(); i++) {
					if (ObjectManager.getClassrooms().get(i).getLab() == 1)
						courseClassroom.addItem(ObjectManager.getClassrooms().get(i));
				}
			}
		});
		netLab.setBounds(10, 240, 180, 20);
		netLab.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				courseClassroom.removeAllItems();
				for(int i = 0; i < classroomsModel.getSize(); i++) {
					if (ObjectManager.getClassrooms().get(i).getLab() == 2)
						courseClassroom.addItem(ObjectManager.getClassrooms().get(i));
				}
			}
		});
		winLab.setBounds(10, 260, 180, 20);
		winLab.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				courseClassroom.removeAllItems();
				for(int i = 0; i < classroomsModel.getSize(); i++) {
					if (ObjectManager.getClassrooms().get(i).getLab() == 3)
						courseClassroom.addItem(ObjectManager.getClassrooms().get(i));
				}
			}
		});
		hardLab.setBounds(10, 280, 180, 20);
		hardLab.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				courseClassroom.removeAllItems();
				for(int i = 0; i < classroomsModel.getSize(); i++) {
					if (ObjectManager.getClassrooms().get(i).getLab() == 4)
						courseClassroom.addItem(ObjectManager.getClassrooms().get(i));
				}
			}
		});
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(noLab);
		radioGroup.add(macLab);
		radioGroup.add(netLab);
		radioGroup.add(winLab);
		radioGroup.add(hardLab);
		
		JComboBox<Course> courseList = new JComboBox<Course>(coursesModel);
		courseList.setRenderer(new ListCellRendererOverride());
		courseList.setSelectedIndex(-1);
		courseList.setBounds(100, 5, 140, 20);
		courseList.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (courseList.getSelectedIndex() >= 0) {
					//Schedule.Display(courseList.getSelectedItem().toString());
					courseCode.setText(ObjectManager.getCourses().get(courseList.getSelectedIndex()).getCode()); 
					courseName.setText(ObjectManager.getCourses().get(courseList.getSelectedIndex()).getName());
					courseSection.setText(String.valueOf(ObjectManager.getCourses().get(courseList.getSelectedIndex()).getSection()));
					courseHours.setText(String.valueOf(ObjectManager.getCourses().get(courseList.getSelectedIndex()).getHours()));
					courseSemester.setValue(ObjectManager.getCourses().get(courseList.getSelectedIndex()).getSemester());
					for(int i = 0; i < instructorsModel.getSize(); i++) {
						if (instructorsModel.getElementAt(i).getID() == ObjectManager.getCourses().get(courseList.getSelectedIndex()).getInstructor())
							courseInstructor.setSelectedItem(instructorsModel.getElementAt(i));		
					}					
					courseProgramValue = ObjectManager.getCourses().get(courseList.getSelectedIndex()).getProgram();
					switch (ObjectManager.getCourses().get(courseList.getSelectedIndex()).getLab()) {
					case 0: 
						noLab.setSelected(true);
						courseClassroom.removeAllItems();
						for (int i = 0; i < classroomsModel.getSize(); i++) {
							if (classroomsModel.getElementAt(i).getLab() == 0)
								courseClassroom.addItem(classroomsModel.getElementAt(i));
						}
						break;
					case 1: 
						macLab.setSelected(true);
						courseClassroom.removeAllItems();
						for (int i = 0; i < classroomsModel.getSize(); i++) {
							if (classroomsModel.getElementAt(i).getLab() == 1)
								courseClassroom.addItem(classroomsModel.getElementAt(i));
						}
						break;
					case 2: 
						netLab.setSelected(true);
						courseClassroom.removeAllItems();
						for (int i = 0; i < classroomsModel.getSize(); i++) {
							if (classroomsModel.getElementAt(i).getLab() == 2)
								courseClassroom.addItem(classroomsModel.getElementAt(i));
						}
						break;
					case 3: 
						winLab.setSelected(true);
						courseClassroom.removeAllItems();
						for (int i = 0; i < classroomsModel.getSize(); i++) {
							if (classroomsModel.getElementAt(i).getLab() == 3)
								courseClassroom.addItem(classroomsModel.getElementAt(i));
						}
						break;
					case 4: 
						hardLab.setSelected(true);
						courseClassroom.removeAllItems();
						for (int i = 0; i < classroomsModel.getSize(); i++) {
							if (classroomsModel.getElementAt(i).getLab() == 4)
								courseClassroom.addItem(classroomsModel.getElementAt(i));
						}
						break;
					default:
						throw new IllegalArgumentException("Unexpected value: " + ObjectManager.getClassrooms().get(courseList.getSelectedIndex()).getLab());
					}
					for(int i = 0; i < classroomsModel.getSize(); i++) {
						if (classroomsModel.getElementAt(i).getID() == ObjectManager.getCourses().get(courseList.getSelectedIndex()).getClassroom())
							courseClassroom.setSelectedItem(classroomsModel.getElementAt(i));		
					}
				}
			}
		});
		JButton courseAdd = new JButton("Add");
		courseAdd.setBounds(10, 340, 225, 25);
		courseAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				if (!courseCode.getText().equals("") &&
						!courseName.getText().equals("") &&
						!courseHours.getText().equals("") &&
						!courseSection.getText().equals("") &&
						courseInstructor.getSelectedIndex() != -1 &&
						courseProgramValue > 0 &&
						courseClassroom.getSelectedIndex() != -1
					){
					ArrayList<Course> courses = ObjectManager.getCourses();
					String fullCourseName = courseCode.getText() + "-" + courseSection.getText();
					for (int i = 0; i < courses.size(); i++) {
						String storedCourseName = ObjectManager.getCourses().get(i).getCode() + "-" + ObjectManager.getCourses().get(i).getSection();
						if (fullCourseName.equals(storedCourseName)) {
							JOptionPane.showMessageDialog(null, "Course code and section is already in database!");
							return;
						}
					}
					int courseInstructorID = 0;
					int courseClassroomID = 0;
					int int_courseHours = Integer.parseInt(courseHours.getText());
					for(int i = 0; i < instructorsModel.getSize(); i++) {
						if (courseInstructor.getSelectedItem() == ObjectManager.getInstructors().get(i)) {
							courseInstructorID = ObjectManager.getInstructors().get(i).getID();
						}
					} 
					for(int i = 0; i < classroomsModel.getSize(); i++) {
						if (courseClassroom.getSelectedItem() == ObjectManager.getClassrooms().get(i)) {
							courseClassroomID = ObjectManager.getClassrooms().get(i).getID();
						}
					}
					int labType = GetLabType(noLab, macLab, netLab, winLab, hardLab);					
					Course.Create(courseCode.getText(), courseName.getText(),  Integer.parseInt(courseSection.getText()), int_courseHours, labType, courseProgramValue, courseSemester.getValue(), courseInstructorID, courseClassroomID);
					reloadDropDowns();
					if (courseList.getItemCount() > 1)
						courseList.setSelectedIndex(courses.size()-1);
					courseList.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "You must fill in all empty fields.");
				}
			}
		});
		JButton courseUpdate = new JButton("Update");
		courseUpdate.setBounds(10, 370, 110, 25);
		courseUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!courseCode.getText().equals("") &&
						!courseName.getText().equals("") &&
						!courseHours.getText().equals("") &&
						!courseSection.getText().equals("") &&
						courseInstructor.getSelectedIndex() != -1 &&
						courseProgramValue > 0 &&
						courseClassroom.getSelectedIndex() != -1
					){
					if (courseList.getSelectedIndex() != -1) {						
						Course.Update(
								courseList.getSelectedIndex(),
								((Course)courseList.getSelectedItem()).getID(),
								courseCode.getText(),
								courseName.getText(),
								Integer.parseInt(courseSection.getText()),
								Integer.parseInt(courseHours.getText()),
								GetLabType(noLab, macLab, netLab, winLab, hardLab),
								courseProgramValue,
								courseSemester.getValue(),
								((Instructor)courseInstructor.getSelectedItem()).getID(),
								((Classroom)courseClassroom.getSelectedItem()).getID()
							);
						reloadDropDowns();
					} else {
						JOptionPane.showMessageDialog(null, "You must select a classroom above to update.");
					}
				} else {
					JOptionPane.showMessageDialog(null, "You must fill in all empty fields.");
				}
			}
		});
		JButton courseDelete = new JButton("Delete");
		courseDelete.setBounds(125, 370, 110, 25);
		courseDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Course.Delete(ObjectManager.getCourses().get(courseList.getSelectedIndex()).getID());
				reloadDropDowns();
				courseList.setSelectedIndex(-1);
			}
		});
		coursePanel.add(courseListLabel);
		coursePanel.add(courseCodeLabel);
		coursePanel.add(courseNameLabel);
		coursePanel.add(courseHoursLabel);
		coursePanel.add(courseProgramLabel);
		coursePanel.add(courseSemesterLabel);
		coursePanel.add(courseSectionLabel);
		coursePanel.add(courseInstructorLabel);
		coursePanel.add(courseClassroomLabel);
		coursePanel.add(courseList);
		coursePanel.add(courseCode);
		coursePanel.add(courseName);
		coursePanel.add(courseHours);
		coursePanel.add(coursePrograms);
		coursePanel.add(courseSemester);
		coursePanel.add(courseSection);
		coursePanel.add(courseInstructor);
		coursePanel.add(noLab);
		coursePanel.add(macLab);
		coursePanel.add(netLab);
		coursePanel.add(winLab);
		coursePanel.add(hardLab);
		coursePanel.add(courseClassroom);
		coursePanel.add(courseAdd);
		coursePanel.add(courseUpdate);
		coursePanel.add(courseDelete);
		courseManager.add(coursePanel);
	}

	private void reloadDropDowns(){
		//reload programs Array List:
		ArrayList<Program> programs = ObjectManager.getPrograms();
		programsModel.removeAllElements();
		for (int i = 0; i < programs.size(); i++) {
			programsModel.addElement(programs.get(i));
		}
		programsModel.setSelectedItem(null);
		
		//reload instructors Array List:
		ArrayList<Instructor> instructors = ObjectManager.getInstructors();
		instructorsModel.setSelectedItem(null);
		instructorsModel.removeAllElements();
		for (int i = 0; i < instructors.size(); i++) {
			instructorsModel.addElement(instructors.get(i));
		}

		//reload classrooms Array List:
		ArrayList<Classroom> classrooms = ObjectManager.getClassrooms();
		classroomsModel.removeAllElements();
		for (int i = 0; i < classrooms.size(); i++) {
			classroomsModel.addElement(classrooms.get(i));
		}
		classroomsModel.setSelectedItem(null);
				
		//reload courses Array List:
		ArrayList<Course> courses = ObjectManager.getCourses();
		coursesModel.removeAllElements();
		for (int i = 0; i < courses.size(); i++) {
			coursesModel.addElement(courses.get(i));
		}
		coursesModel.setSelectedItem(null);
	}
	private int GetLabType(JRadioButton noLab, JRadioButton macLab, JRadioButton netLab, JRadioButton winLab,JRadioButton hardLab) {
		// LabType is stored as an Int in the database, this converts the radioButton to the appropriate int. 
		if (noLab.isSelected() == true)
			return 0;
		else if (macLab.isSelected() == true)
			return 1;
		else if (netLab.isSelected() == true)
			return 2;
		else if (winLab.isSelected() == true)
			return 3;
		else if (hardLab.isSelected() == true)
			return 4;
		else
			return -1;
	}
	private void fillCourseList(Program program, int semester, JComboBox<Course> courseList){
		// When the Program Combobox on the main JFrame changes value, this function will
		// reset the Courses associated with the selected program (also on the main JFrame).
		courseList.removeAllItems();
		if (program == null)
			return;
		int programID = (int)Math.pow(2, program.getID()-1);
		for (int i = 0; i < coursesModel.getSize(); i++) {
			int courseProgram = coursesModel.getElementAt(i).getProgram();
			int courseSemester = coursesModel.getElementAt(i).getSemester();
			int courseHours = coursesModel.getElementAt(i).getHours();
			switch (courseProgram) {
				case 1:
					if (courseSemester == semester && programID == 1)
						if (CalculateScheduledCourseHours(coursesModel.getElementAt(i)) < courseHours)
							courseList.addItem(coursesModel.getElementAt(i));
					break;
				case 2:
					if (courseSemester == semester && programID == 2)
						if (CalculateScheduledCourseHours(coursesModel.getElementAt(i)) < courseHours)
						courseList.addItem(coursesModel.getElementAt(i));
					break;
				case 3:
					if (courseSemester == semester && (programID == 1 || programID == 2))
						if (CalculateScheduledCourseHours(coursesModel.getElementAt(i)) < courseHours)
						courseList.addItem(coursesModel.getElementAt(i));
					break;
				case 4:
					if (courseSemester == semester && programID == 4)
						if (CalculateScheduledCourseHours(coursesModel.getElementAt(i)) < courseHours)
						courseList.addItem(coursesModel.getElementAt(i));
					break;
				case 5:
					if (courseSemester == semester && (programID == 1 || programID == 4))
						if (CalculateScheduledCourseHours(coursesModel.getElementAt(i)) < courseHours)
						courseList.addItem(coursesModel.getElementAt(i));
					break;
				case 6:
					if (courseSemester == semester && (programID == 2 || programID == 4))
						if (CalculateScheduledCourseHours(coursesModel.getElementAt(i)) < courseHours)
						courseList.addItem(coursesModel.getElementAt(i));
					break;
				case 7:
					if (courseSemester == semester && (programID == 3 || programID == 4))
						if (CalculateScheduledCourseHours(coursesModel.getElementAt(i)) < courseHours)
						courseList.addItem(coursesModel.getElementAt(i));
					break;
				case 8:
					if (courseSemester == semester && programID == 8)
						if (CalculateScheduledCourseHours(coursesModel.getElementAt(i)) < courseHours)
						courseList.addItem(coursesModel.getElementAt(i));
					break;
				case 9:
					if (courseSemester == semester && (programID == 1 || programID == 8))
						if (CalculateScheduledCourseHours(coursesModel.getElementAt(i)) < courseHours)
						courseList.addItem(coursesModel.getElementAt(i));
					break;
				case 10:
					if (courseSemester == semester && (programID == 2 || programID == 8))
						if (CalculateScheduledCourseHours(coursesModel.getElementAt(i)) < courseHours)
						courseList.addItem(coursesModel.getElementAt(i));
					break;
				case 11:
					if (courseSemester == semester && (programID == 1 || programID == 2 || programID == 8))
						if (CalculateScheduledCourseHours(coursesModel.getElementAt(i)) < courseHours)
						courseList.addItem(coursesModel.getElementAt(i));
					break;
				case 12:
					if (courseSemester == semester && (programID == 4 || programID == 8))
						if (CalculateScheduledCourseHours(coursesModel.getElementAt(i)) < courseHours)
						courseList.addItem(coursesModel.getElementAt(i));
					break;
				case 13:
					if (courseSemester == semester && (programID == 1 || programID == 4 || programID == 8))
						if (CalculateScheduledCourseHours(coursesModel.getElementAt(i)) < courseHours)
						courseList.addItem(coursesModel.getElementAt(i));
					break;
				case 14:
					if (courseSemester == semester && (programID == 2 || programID == 4 || programID == 8))
						if (CalculateScheduledCourseHours(coursesModel.getElementAt(i)) < courseHours)
						courseList.addItem(coursesModel.getElementAt(i));
					break;
				case 15:
					if (courseSemester == semester)
						if (CalculateScheduledCourseHours(coursesModel.getElementAt(i)) < courseHours)
						courseList.addItem(coursesModel.getElementAt(i));
					break;
			}
		}
	}
	private void fillCourseList(Instructor instructor, int semester, JComboBox<Course> courseList) {
		// Like the function above, this will reset the course list, but this time it fires when
		// the Instructor Combobox changes index.
		courseList.removeAllItems();
		for (int i = 0; i < coursesModel.getSize(); i++) {	
			Course tempCourse = coursesModel.getElementAt(i);
			if (tempCourse.getInstructor() == instructor.getID() &&
					(tempCourse.getSemester() == semester || tempCourse.getSemester() == (semester + 2))) {
				if (CalculateScheduledCourseHours(tempCourse) < coursesModel.getElementAt(i).getHours()) {
					courseList.addItem(tempCourse);
				}
			}
		}
	}
	
	private void AssignCourseProgramSchedule(int program_id, Course course) {
		int term = course.getSemester() -1; // -1 to line up terms. Term starts at one, so 1 + (4-1) = 4.
		Program tempProgram = ObjectManager.getPrograms().get(program_id);
		Schedule tempSchedule = ObjectManager.getSchedules().get(tempProgram.getSchedule() - 1 + term);  //-1 for 0-based index.
		tempSchedule.setScheduleEvent(daySelected, timeSelected, course);						
	}
}