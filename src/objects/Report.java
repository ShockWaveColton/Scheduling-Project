package objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.ObjectManager;

public class Report {

	int term;
	
	HashMap<Instructor, Integer> instructorHours = new HashMap<Instructor, Integer>();
	
	//Adds instructors to instructor hours
	private void initializeInstructors() {
		
		for(Instructor instructor : ObjectManager.getInstructors()) {
			instructorHours.put(instructor, 0);
		}
		
	}
	
	public Report(int term) {
		this.term = term;
		
		initializeInstructors();
		createReport();
		
	}
	
	
	private void createReport() {
		
		for(Instructor instructor : instructorHours.keySet()) {
			calculateHours(instructor);
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
	

	//Calculate hours for instructor
	private int calculateHours(Instructor instructor) {
		
		List<Schedule> schedules = ObjectManager.getSchedules();
		
		//Loop through schedules
		for(Schedule schedule : schedules) {

			
			//If the term is equal to this term
			if(schedule.getTerm() == term) {
				
				//Loop through the lessons tables
				for(Lesson[] lessons : schedule.getAllLessons()) {
					
					//Loop through each individual lesson 
					for(Lesson lesson : lessons) {

						if(lesson != null && lesson.getInstructor().equals(instructor)) {
							int oldHours = instructorHours.get(instructor);
							int newHours = oldHours + 1;
							
							instructorHours.put(instructor, newHours);
							
						}
					}
					
				}
				
			}
			
		}
		
		
		int hours = instructorHours.get(instructor);
		return hours;

	}
	
}
