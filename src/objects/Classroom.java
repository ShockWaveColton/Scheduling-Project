package objects;

import java.util.ArrayList;

import engine.FileIO;
import engine.ObjectManager;

// This is the Classroom Class file.
// Note that the constructor is private, I wanted the class file to control all of the creation,
// rather than giving some of that power to the window class.
// use <classroom object name>.Create() to create a new classroom object and add that classroom to the arraylist.

// Member variables
public class Classroom {
	private int ID;
	private String wing;
	private String room;
	private String name;
	private int lab;

	// Constructor
	private Classroom(int ID, String wing, String room, int lab) {
		this.ID = ID;
		this.wing = wing;
		this.room = room;
		this.name = wing + "-" + room;
		this.lab = lab;
	}

	// Accessors:
	public int getID() { return this.ID; }
	public String getWing() { return this.wing; }
	public String getRoom() { return this.room; }
	public String getName() { return this.name; }
	public int getLab() { return this.lab; }
	
	// Adding a new Classroom to the Classrooms Table in the loaded database:
	public static void Create(String wing, String room, int lab) {
		int ID = FileIO.CreateClassroom(wing, room, lab);
		ObjectManager.AddClassroomToList(new Classroom(ID, wing, room, lab));		
	}

	// Reading a Classroom from the database, storing it in the Obj.Mgr arrayList:
	public static void Read(int ID, String wing, String room, int lab) {
		ObjectManager.AddClassroomToList(new Classroom(ID, wing, room, lab));
	}
	
	// Updates database with appended classroom information, then updates the classroom Array list.
	public static void Update(int index, int ID, String wing, String room, int lab) {
		FileIO.UpdateClassroom(ID, wing, room, lab);
		ObjectManager.UpdateClassroomInList(index, new Classroom(ID, wing, room, lab));
	}
	
	// Removes the selected classroom object from the database, then from the arraylist.
	public static void Delete(int ID) {
		FileIO.DeleteClassroom(ID);
		ArrayList<Classroom> classrooms = ObjectManager.getClassrooms();
		for (int i = 0; i < classrooms.size(); i++) {
			if (classrooms.get(i).getID() == ID)
				ObjectManager.RemoveClassroomFromList(classrooms.get(i));
		}	
	}
}
