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
		String name = wing + "-" + number;
		Classroom classroom = new Classroom(name);
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
	public static void Update(int selectedIndex, String wing, String roomNum) {
		FileIO.Update(selectedIndex,wing,roomNum);
	}

	public String getName() { return this.name; }

}
