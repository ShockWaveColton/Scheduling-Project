package objects;

import java.util.ArrayList;

import engine.FileIO;
import engine.Main;
import engine.ObjectManager;
import engine.Window;

// Member Variables:
public class Schedule {
	private int		   ID;
	private String     name;
	private int        term;
	private Lesson[][] lessons;
	private static Window window;
	
	// Constructor for new Schedule
	private Schedule(int ID, String name, int term) {
		window = Main.getWindow();
		this.ID = ID;
		this.name = name;
		this.term = term;
		this.lessons = new Lesson[5][9];
	}
	
	// Constructor (existing schedule):
	private Schedule(int ID, String name, int term, Lesson[][] schedule) {
		window = Main.getWindow();
		this.ID = ID;
		this.name = name;
		this.term = term;
		this.lessons = schedule;
	}

	// Accessors:
	public Schedule   getSchedule(String name)             { return this; }
	public int        getID()                              { return this.ID; }
	public String     getScheduleName()                    { return this.name; }
	public int        getTerm()                            { return this.term; }
	public Lesson[][] getAllLessons()				       { return this.lessons; }
	public Lesson     getSpecificLesson(int day, int time) { return this.lessons[day][time]; }
	public boolean    hasEvent(int day, int time)          { if (this.lessons[day][time] != null) return true; else return false; }
	private void      setLesson(int x, int y, Lesson lesson) {this.lessons[x][y] = lesson; }

	public static int Create(String name, int term, boolean isProgram) {

		int ID = FileIO.CreateSchedule(name, isProgram);
		Schedule schedule = null;
		schedule = new Schedule(ID, name, term);
		schedule = new Schedule(ID, name, term+1);
		if (isProgram) {
			schedule = new Schedule(ID, name, term+2);
			schedule = new Schedule(ID, name, term+3);			
		}

		ObjectManager.AddScheduleToList(schedule);
		return ID;
	}
	
	public static void FullRead(int ID, String name, int term, int[][] scheduleData) {
		Lesson[][] lessons = new Lesson[5][9];
		ArrayList<Lesson> lessonList = ObjectManager.getLessons();
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 9; y++) {
				if (scheduleData[x][y] > 0) {
					int index = Integer.parseInt((x+1) +""+ (y+1));
					lessons[x][y] = lessonList.get(index);
				}
			}
		}
		Schedule schedule = new Schedule(ID, name, term, lessons);
		ObjectManager.AddScheduleToList(schedule);		
	}
	
	public static void ReadPass1(int ID, String name, int term) {
		// First pass creates schedule objects, without lesson data (since it isn't read from the database yet)
		Lesson[][] lessons = new Lesson[5][9];
		Schedule schedule = new Schedule(ID, name, term, lessons);
		ObjectManager.AddScheduleToList(schedule);		
	}
	public static void ReadPass2(int ID, int[][] scheduleData) {
		// Second pass updates each schedule object with lesson data, after all data is read in from database.
		ArrayList<Lesson> lessons = ObjectManager.getLessons();
		Schedule schedule = ObjectManager.getSchedules().get(ID-1);
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 9; y++) {
				if (scheduleData[x][y] != 0) {
					int lesson_id = scheduleData[x][y];
					for (int i = 0; i < lessons.size(); i++) {
						if (lessons.get(i).getID() == lesson_id) {
							Lesson lesson = lessons.get(i);
							schedule.setLesson(x, y, lesson);
							break;
						}
					}
				}
			}
		}
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
		lessons[day][time] = tempClass;
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

	public static void Display(Object object, int semester) {
		ArrayList<Schedule> schedules = ObjectManager.getSchedules();
		//We need to confirm which of the 4 schedules of the program we need to display: 
		if (object instanceof Program) {
			for (int i = 0; i < schedules.size(); i++) {
				//System.out.println(schedules.get(i).getScheduleName() + " - " + ((Program)object).getName());
				if (schedules.get(i).getScheduleName().equals(((Program)object).getName()) && schedules.get(i).getTerm() == semester) {
					window.DrawSchedule(schedules.get(i).getID());
					break;
				}
			}					
		}
		//Same as above, but this time there are only 2 schedules:
		else if (object instanceof Instructor) {
			for (int i = 0; i < schedules.size(); i++) {
				//System.out.println("(" + schedules.get(i).getScheduleName() + "_" + schedules.get(i).getTerm() + ") - (" + ((Instructor)object).getW_Number() + "_" + semester + ")");
				if (schedules.get(i).getScheduleName().equals(((Instructor)object).getW_Number())) {
					if (schedules.get(i).getTerm() == semester) {
						window.DrawSchedule(schedules.get(i).getID());	
						break;						
					}
				}
			}
		}					
	}
	
	//Remove a lesson from the schedule, and from the schedule database
	public void DeleteScheduledEvent(int day, int time) {

		lessons[day][time] = null;	
		
		String scheduleLocation = (day+1) + "" + (time+1);
		FileIO.updateSchedule(ID, scheduleLocation);
		
		// Pass the update schedule to the ObjectManager.
		ArrayList<Schedule> scheduleList = ObjectManager.getSchedules();
		for (int i = 0; i < scheduleList.size(); i++) {
			if (scheduleList.get(i).getID() == ID) {
				ObjectManager.UpdateScheduleInList(i, this);
				break;
			}
		}
	}
}