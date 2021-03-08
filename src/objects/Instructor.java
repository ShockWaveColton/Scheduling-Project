package objects;

import java.util.ArrayList;

import engine.FileIO;
import engine.ObjectManager;

// This is the Instructor Class file.
// Note that the constructor is private, I wanted the class file to control all of the creation,
// rather than giving some of that power to the Window class.
// use <instructor object name>.Add() to create a new instructor object and add that instructor to the arraylist.

public class Instructor {
	private int ID;
	private String wNumber;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	
	private Instructor(int ID, String wNumber, String firstName, String lastName, String email, String phone) {
		this.ID        = ObjectManager.getInstructors().size() + 1;
		this.wNumber  = wNumber;
		this.firstName = firstName;
		this.lastName  = lastName;
		this.email     = email;
		this.phone     = phone;
	}
	
	public int    getID()        { return this.ID; }
	public String getW_Number()  { return this.wNumber; }
	public String getFirstName() { return this.firstName; }
	public String getLastName()  { return this.lastName; }
	public String getEmail()     { return this.email; }
	public String getPhone()     { return this.phone; }

	public static void Create(String wNumber, String firstName, String lastName, String email, String phone) {
		int ID = FileIO.AddInstructor(wNumber, firstName, lastName, email, phone);
		// At this point adding instructor to database file is successful, so we update applications array list.
		Instructor instructor = new Instructor(ID, wNumber, firstName, lastName, email, phone);
		ArrayList<Instructor> instructors = ObjectManager.getInstructors();
		instructors.add(instructor);
	}
	
	public static void Read(int ID, String wNumber, String firstName, String lastName, String phone, String email) {
		Instructor instructor = new Instructor(ID, wNumber, firstName, lastName, phone, email);
		ArrayList<Instructor> instructors = ObjectManager.getInstructors();
		instructors.add(instructor);		
	}
	
	public static void Update(int index, int ID, String wNumber, String firstName, String lastName, String email, String phone) {
		FileIO.UpdateInstructor(ID, wNumber, firstName, lastName, email, phone);
		// At this point updating instructor in database file is successful, so we update applications array list.
		Instructor instructor = new Instructor(ID, wNumber, firstName, lastName, email, phone);
		ArrayList<Instructor> instructors = ObjectManager.getInstructors();
		instructors.set(index, instructor);
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