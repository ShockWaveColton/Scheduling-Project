package objects;

import engine.ObjectManager;
import engine.Window;

public class Schedule {
	private Class[][] schedule;
	private String scheduleName;
 	
	private Schedule(String name) {
		this.scheduleName = name;
		schedule = new Class[6][9];
	}
	
	public static void Create(String name) {
		Schedule schedule = new Schedule(name);
		ObjectManager.addScheduleToList(schedule);
	}
	
	//Add a new class to the schedule.
	public void Add(String name) {
		
	}
	
	public Schedule getSchedule() { return this; }
	public String getScheduleName() { return scheduleName; } 
	public Class getScheduleEvent(int a, int b) { return schedule[a][b]; }

	public static void Display(String name) {
		System.out.println("Displaying schedule for: " + name);
		Window.DrawSchedule(name);
	}
}