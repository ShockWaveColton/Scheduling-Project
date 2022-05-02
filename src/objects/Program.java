package objects;

import java.util.ArrayList;

import engine.FileIO;
import engine.ObjectManager;

// Member variables:
public class Program {
	private int    ID;
	private String name;
	private int schedule;

	// Constructor:
	public Program(int ID, String name, int schedule) {
		this.ID   = ID;
		this.name = name;
		this.schedule = schedule;
		ObjectManager.AddProgramToList(this);
	}
	
	// Accessors:
	public int      getID()         { return ID; }
	public String   getName()       { return name; }
	public int      getSchedule()   { return schedule; }

	public static Program Create(String name, int term) {
		int schedule = Schedule.Create(name, term, true);
		// Send program data to database, collect ID, add program to array list of programs.
		int ID = FileIO.CreateProgram(name, schedule);
		Program program = null;
		program = new Program(ID, name, schedule);
		return program;
	}
	
	// Reading in data from database, converting it into objects, and loading objects into the arraylist:
	public static void Read(int ID, String name, int schedule) {
		new Program(ID, name, schedule);
	}
	
	public static void Update(int index, int ID, String name, int schedule) {
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
}
