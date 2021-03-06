package objects;

import java.util.HashMap;


import engine.Main;
import engine.ObjectManager;
import engine.Window;

public class Report {

	int term;
	
	private HashMap<Instructor, Integer> instructorHours = new HashMap<Instructor, Integer>();
	
	private HashMap<Program, Integer> programHours = new HashMap<Program, Integer>();
	
	//Adds instructors to instructor hours
	private void initializeInstructors() {
		
		for(Instructor instructor : ObjectManager.getInstructors()) {
			instructorHours.put(instructor, 0);
		}
		
	}
	
	private void initializePrograms() {
		
		for(Program program : ObjectManager.getPrograms()) {
			programHours.put(program,0);
		}
		
	}
	
	public Report(int term) {
		this.term = term;
		
		initializeInstructors();
		initializePrograms();
		createReport();
		
	}
	
	
	private void createReport() {
		//if statement to avoid errors
		if(term == 0 || term == 1)
		{
			for(Instructor instructor : instructorHours.keySet()) {
				calculateHours(instructor);
			}
		}
		
		for(Program program : programHours.keySet()) {
			calculateHoursForProgram(program);
		}
		
	}
	
	
	/**
	 * Use this to retrieve the hours of the instructor after doing the report
	 * 
	 * @param instructor
	 * @return the hours the specified instructor
	 * 
	 */
	public int getHoursForInstructor(Instructor instructor) {
		return instructorHours.get(instructor);
	}
	
	
	public int getHoursForProgram(Program program) {
		return programHours.get(program);
	}
	
	//Calculate hours for the program
	private int calculateHoursForProgram(Program program) {
		int programHrs = 0;
		
		Window mainWindow = Main.getWindow();
		Schedule programSchedule = mainWindow.getScheduleByID(program.getSchedule() + term);
		
		//Count the hours
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 9; y++) {
				Lesson tempLesson = programSchedule.getAllLessons()[x][y];
				if(tempLesson != null) {
					programHrs++;
				}
			}
		}
		
		programHours.put(program, programHrs);
		
		int hours = programHours.get(program);
		
		return hours;

	}
		
	//Calculate hours for instructor
	private int calculateHours(Instructor instructor) {
		int instructorScheduleID = (instructor.getSchedule()) + term;
		Schedule schedule = ObjectManager.getSchedules().get(instructorScheduleID - 1);
		//Loop through schedule
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 9; y++) {
				Lesson lesson = schedule.getAllLessons()[x][y];
					
				//See if lesson there and has current instructor teaching it
				if(lesson != null && lesson.getInstructor().equals(instructor)) {
					int oldHours = instructorHours.get(instructor);
					int newHours = oldHours + 1;
						
					instructorHours.put(instructor, newHours);
						
				}
			}
		}	


		
		
		int hours = instructorHours.get(instructor);
		return hours;

	}
	
}
