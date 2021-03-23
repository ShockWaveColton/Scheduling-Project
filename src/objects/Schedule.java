package objects;

import java.util.ArrayList;

import engine.FileIO;
import engine.ObjectManager;
import engine.Window;

// Member Variables:
public class Schedule {
	private int		  ID;
	private String    name;
	private Lesson[][] schedule;
 	
	//Constructor (new schedule):
	private Schedule(int ID, String name) {
		this.ID = ID;
		this.name = name;
		this.schedule = new Lesson[5][9];
	}
	
	// Constructor (existing schedule):
	private Schedule(int ID, String name, Lesson[][] schedule) {
		this.ID = ID;
		this.name = name;
		this.schedule = schedule;
	}

	// Accessors:
	public Schedule getSchedule(String name)            { return this; }
	public int      getID()                             { return this.ID; }
	public String   getScheduleName()                   { return this.name; } 
	public Lesson   getScheduleEvent(int day, int time) { return this.schedule[day][time]; }
	public boolean  hasEvent(int day, int time)         { if (this.schedule[day][time] != null) return true; else return false; }

	public static Schedule Create(String name) {
		int ID = FileIO.CreateSchedule(name);
		Schedule schedule = new Schedule(ID, name);
		ObjectManager.AddScheduleToList(schedule);
		return schedule;
	}
	
	public static void Read(int ID, String name, int[][] scheduleData) {
		Lesson[][] lessons = new Lesson[5][9];
		Lesson lesson = null;
		ArrayList<Lesson> lessonList = ObjectManager.getLessons();
		for (int x = 1; x < 5; x++) {
			for (int y = 1; y < 9; y++) {
				if (scheduleData[x-1][y-1] != 0) {
					int lesson_id = Integer.parseInt((x-1) +""+ (y-1));
					lesson = lessonList.get(lesson_id);					
				}				
				lessons[x][y] = lesson; 
			}
		}
		Schedule schedule = new Schedule(ID, name, lessons);
		ObjectManager.AddScheduleToList(schedule);		
	}
	
	public void setScheduleEvent(int day, int time, Course course) {
		int InstructorID = course.getInstructor();
		Instructor tempInstructor = null;
		// course contains an instructor ID, we need the instructor object:
		ArrayList<Instructor> instructors = ObjectManager.getInstructors();
		for (int i = 0; i < instructors.size(); i++) {
			if (InstructorID == instructors.get(i).getID()) {
				tempInstructor = instructors.get(i);
				break;
			}
		}
		Classroom tempClassroom =   ObjectManager.getClassrooms().get(course.getClassroom());
		Lesson tempClass = Lesson.Create(course, tempInstructor, tempClassroom);
		schedule[day][time] = tempClass;
		// Pass the updated schedule to the database
		String scheduleLocation = (day+1) + "" + (time+1);
		FileIO.updateSchedule(ID, scheduleLocation, tempClass.getID());
		// Pass the update schedule to the ObjectManager.
		ArrayList<Schedule> scheduleList = ObjectManager.getSchedules();
		for (int i = 0; i < scheduleList.size(); i++) {
			if (scheduleList.get(i).getID() == ID) {
				ObjectManager.UpdateScheduleInList(i, this);
				break;
			}
		}
	}

	
	public static void Display(Object object) {
		if (object instanceof Program)
			Window.DrawSchedule(((Program)object).getSchedule());	
		else if (object instanceof Instructor)
			Window.DrawSchedule(((Instructor)object).getSchedule());	
	}	
}