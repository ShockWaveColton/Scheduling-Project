package objects;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import engine.ObjectManager;

//The Course Class file.
//Note that the constructor is private, I wanted the class file to control all of the creation,
//rather than giving some of that power to the window class.
//use <course object name>.Add() to create a new classroom object and add that classroom to the arraylist.

public class Course {
	private String code;
	private String name;

	private Course(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() { return this.code; }
	public String getName() { return this.name; }

	public static void Add(String code, String name) {
		Course course = new Course(code, name);
		ArrayList<Course> courses = ObjectManager.getCourses();
		courses.add(course);
	}
}
