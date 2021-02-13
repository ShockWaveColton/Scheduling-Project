package objects;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import engine.ObjectManager;

//The Course Class file.
//Note that the constructor is private, I wanted the class file to control all of the creation,
//rather than giving some of that power to the window class.
//use <course object name>.Add() to create a new classroom object and add that classroom to the arraylist.

public class Instructor {
	private String name;
	
	private Instructor(String name) {
		this.name = name;
	}
	public static void Add() {
		String name = JOptionPane.showInputDialog("What is the instructors name?");
		Instructor instructor = new Instructor(name);
		ArrayList<Instructor> instructors = ObjectManager.getInstructors();
		instructors.add(instructor);
	}
	public String getName() { return this.name; }
}