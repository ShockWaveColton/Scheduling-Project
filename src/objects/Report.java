package objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		
		for(Instructor instructor : instructorHours.keySet()) {
			calculateHours(instructor);
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
		int tempHours = 0;
		Window mainWindow = Main.getWindow();
		
		List<Course> courses = ObjectManager.getCourses();
		
		Schedule programSchedule = mainWindow.getScheduleByID(program.getSchedule());
		
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
		Window mainWindow = Main.getWindow();
		Schedule schedule = mainWindow.getScheduleByID(instructor.getSchedule());
		
		//If the term is equal to this term
		if(schedule.getTerm() == term) {
			
			for (int x = 0; x < 5; x++) {
				for (int y = 0; y < 9; y++) {
					Lesson lesson = schedule.getAllLessons()[x][y];
					
					
					if(lesson != null && lesson.getInstructor().equals(instructor)) {
						int oldHours = instructorHours.get(instructor);
						int newHours = oldHours + 1;
						
						instructorHours.put(instructor, newHours);
						
					}
				}
				
			}
			
		}

		
		
		int hours = instructorHours.get(instructor);
		return hours;

	}
	
}
