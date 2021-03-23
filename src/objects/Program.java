package objects;

import java.util.ArrayList;

import engine.FileIO;
import engine.ObjectManager;


// Member variables:
public class Program {
	private int    ID;
	private String name;
	private Schedule schedule;

	// Constructor:
	public Program(int ID, String name, Schedule schedule) {
		this.ID   = ID;
		this.name = name;
		this.schedule = schedule;
		ObjectManager.AddProgramToList(this);
	}
	
	// Accessors:
	public int      getID()         { return ID; }
	public String   getName()       { return name; }
	public Schedule getSchedule()   { return schedule; }

	public static Program Create(String name) {
		Schedule schedule = Schedule.Create(name);
		// Send program data to database, collect ID, add program to array list of programs.
		int ID = FileIO.CreateProgram(name, schedule.getID());
		Program program = new Program(ID, name, schedule);
		return program;
	}
	
	public static void Read(int ID, String name, int schedule_id) {
		Schedule schedule = getSchedule(schedule_id);
		new Program(ID, name, schedule);
	}
	
	public static void Update(int index, int ID, String name, Schedule schedule) {
		FileIO.UpdateProgram(ID, name);
		// The update is successful, and we can replace the classroom in the array list.
		new Program(ID, name, schedule);		
	}
	public static void Delete(int ID) {
		FileIO.DeleteProgram(ID);
		ArrayList<Program> programs = ObjectManager.getPrograms();
		for (int i = 0; i < programs.size(); i++) {
			if (programs.get(i).getID() == ID)
				programs.remove(i);
		}	
	}
	private static Schedule getSchedule(int schedule_id) {
		ArrayList<Schedule> schedules = ObjectManager.getSchedules();
		for (int i = 0; i < schedules.size(); i++) {
			if (schedules.get(i).getID() == schedule_id)
				return schedules.get(i);
		}
		return null;
	}
}
