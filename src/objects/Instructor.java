package objects;

import java.util.ArrayList;

import engine.FileIO;
import engine.ObjectManager;

// This is the Instructor Class file.
// Note that the constructor is private, I wanted the class file to control all of the creation,
// rather than giving some of that power to the Window class.
// use <instructor object name>.Create() to create a new instructor object and add that instructor to the arraylist.

// Member variables:
public class Instructor {
	private int      ID;
	private String   wNumber;
	private String   firstName;
	private String   lastName;
	private String   fullName;
	private String   email;
	private String   phone;
	private int      baseSchedule;
	
	// Constructor:
	private Instructor(int ID, String wNumber, String firstName, String lastName, String email, String phone, int schedule) {
		this.ID           = ID;
		this.wNumber      = wNumber;
		this.firstName    = firstName;
		this.lastName     = lastName;
		this.fullName     = firstName + " " + lastName;
		this.phone        = phone;
		this.email        = email;
		this.baseSchedule = schedule;
	}
	
	// Accessors:
	public int      getID()       { return this.ID; }
	public String   getW_Number() { return this.wNumber; }
	public String   getFirstName(){ return this.firstName; }
	public String   getLastName() { return this.lastName; }
	public String   getFullName() { return this.fullName; }
	public String   getPhone()    { return this.phone; }
	public String   getEmail()    { return this.email; }
	public int      getSchedule() { return this.baseSchedule; }

	public static Instructor Create(String wNumber, String firstName, String lastName, String phone, String email, int term) {
		int schedule = Schedule.Create(wNumber, term, false);
		int ID = FileIO.CreateInstructor(wNumber, firstName, lastName, phone, email, schedule);
		// At this point adding instructor to database file is successful, so we update applications array list.
		Instructor instructor = new Instructor(ID, wNumber, firstName, lastName, phone, email, schedule);
		ArrayList<Instructor> instructors = ObjectManager.getInstructors();
		instructors.add(instructor);
		return instructor;
	}
	
	public static void Read(int ID, String wNumber, String firstName, String lastName, String phone, String email, int schedule_id) {
		ObjectManager.AddInstructorToList(new Instructor(ID, wNumber, firstName, lastName, phone, email, schedule_id));
	}
	
	public static void Update(int index, int ID, String wNumber, String firstName, String lastName, String phone, String email, int schedule) {		
		FileIO.UpdateInstructor(ID, wNumber, firstName, lastName, phone, email, schedule);
		// At this point updating instructor in database file is successful, so we update applications array list.
		ObjectManager.UpdateInstructorInList(index, new Instructor(ID, wNumber, firstName, lastName, phone, email, schedule));
		
	}

	public static void Delete(int ID) {
		FileIO.DeleteInstructor(ID);
		ArrayList<Instructor> instructors = ObjectManager.getInstructors();
		for (int i = 0; i < instructors.size(); i++) {
			if (instructors.get(i).getID() == ID)
				instructors.remove(i);
		}
	}
}