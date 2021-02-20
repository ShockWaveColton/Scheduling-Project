package objects;

import java.util.ArrayList;

import engine.FileIO;
import engine.ObjectManager;

// The Classroom Class file.
// Note that the constructor is private, I wanted the class file to control all of the creation,
// rather than giving some of that power to the window class.
// use <classroom object name>.Add() to create a new classroom object and add that classroom to the arraylist.

public class Classroom {
	private String name;

	private Classroom(String name) {
		this.name = name;
	}
	
	// Adding a new Classroom to the Classrooms Table in the loaded database:
	public static void Add(String wing, String number) {
		FileIO.Add("Classrooms", wing, number);
		//By this point the insertion was successful, and we add the new classroom to the arraylist for viewing.
		Classroom classroom = new Classroom(wing + "-" + number);
		ArrayList<Classroom> classrooms = ObjectManager.getClassrooms();
		classrooms.add(classroom);
	}
	// Reading a Classroom from the database, storing it in the Obj.Mgr arrayList:
	public static void Load(String wing, String number) {
		String name = wing + "-" + number;
		Classroom classroom = new Classroom(name);
		ArrayList<Classroom> classrooms = ObjectManager.getClassrooms();
		classrooms.add(classroom);		
	}
	
	// Updates database with appended classroom information, then updates the classroom Arraylist.
	public static void Update(int selectedIndex, String wing, String roomNum) {
		FileIO.Update("Classrooms", selectedIndex, wing, roomNum);
		// The update is successful, and we can replace the classroom in the arraylist.
		Classroom classroom = new Classroom(wing + "-" + roomNum);
		ArrayList<Classroom> classrooms = ObjectManager.getClassrooms();
		classrooms.set(selectedIndex -1, classroom);
	}
	public static void Delete(int selectedIndex) {
		
	}

	public String getName() { return this.name; }

}
