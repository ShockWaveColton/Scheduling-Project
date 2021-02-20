package objects;

import java.util.ArrayList;

import engine.FileIO;
import engine.ObjectManager;

// The Classroom Class file.
// Note that the constructor is private, I wanted the class file to control all of the creation,
// rather than giving some of that power to the window class.
// use <classroom object name>.Add() to create a new classroom object and add that classroom to the arraylist.

public class Classroom {
	private int ID;
	private String wing;
	private String room;
	private int lab;

	private Classroom(String wing, String room, int lab) {
		this.ID = ObjectManager.getClassrooms().size() + 1;
		this.wing = wing;
		this.room = room;
		this.lab = lab;
	}

	public int getID() { return this.ID; }
	public String getWing() { return this.wing; }
	public String getRoom() { return this.room; }	
	public int getLab() { return this.lab; }
	
	// Adding a new Classroom to the Classrooms Table in the loaded database:
	public static void Create(String wing, String room, int lab) {
		FileIO.Add("Classrooms", wing, room, lab);
		//By this point the insertion was successful, and we add the new classroom to the arraylist for viewing.
		Classroom classroom = new Classroom(wing, room, lab);
		ArrayList<Classroom> classrooms = ObjectManager.getClassrooms();
		classrooms.add(classroom);
	}
	// Reading a Classroom from the database, storing it in the Obj.Mgr arrayList:
	public static void Read(String wing, String room, int lab) {
		Classroom classroom = new Classroom(wing, room, lab);
		ArrayList<Classroom> classrooms = ObjectManager.getClassrooms();
		classrooms.add(classroom);		
	}
	
	// Updates database with appended classroom information, then updates the classroom Arraylist.
	public static void Update(int selectedIndex, String wing, String room, int lab) {
		FileIO.Update("Classrooms", selectedIndex, wing, room);
		// The update is successful, and we can replace the classroom in the arraylist.
		Classroom classroom = new Classroom(wing, room, lab);
		ArrayList<Classroom> classrooms = ObjectManager.getClassrooms();
		classrooms.set(selectedIndex -1, classroom);
	}
	
	// Removes the selected classroom object from the database, then from the arraylist.
	public static void Delete(int ClassroomID) {
		FileIO.Delete("Classrooms", ClassroomID);
		ArrayList<Classroom> classrooms = ObjectManager.getClassrooms();
		for (int i = 0; i < classrooms.size(); i++) {
			if (classrooms.get(i).getID() == ClassroomID) {
				classrooms.remove(i);
				return;				
			}
		}	
	}
}
