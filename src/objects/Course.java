package objects;

import java.util.ArrayList;

import engine.ObjectManager;

// This is the Course Class file.
// Note that the constructor is private, I wanted the class file to control all of the creation,
// rather than giving some of that power to the window class.
// use <course object name>.Create() to create a new classroom object and add that classroom to the arraylist.

public class Course {
	private int ID;
	private int lab;
	private int hours;
	private String code;
	private String name;
	private int sections;
	private String program;
	private String instructor;

	private Course(String code, String name, int hours, int lab, String program, int sections, String instructor) {
		this.ID = ObjectManager.getCourses().size() + 1;
		this.lab = lab;
		this.code = code;
		this.name = name;
		this.hours = hours;
		this.program = program;
		this.sections = sections;
		this.instructor = instructor;
	}

	public int 	  getID()         { return this.ID; }
	public int    getlab() 	      { return this.lab; }
	public String getCode()       { return this.code; }
	public String getName()       { return this.name; }
	public int    getHours()      { return this.hours; }
	public String getProgram()    { return this.program; }
	public int    getSections()   { return this.sections; }
	public String getInstructor() { return this.instructor; }

	public static void Create(String code, String name, int hours, int lab, String program, int sections, String instructor) {
		Course course = new Course(code, name, hours, lab, program, sections, instructor);
		ArrayList<Course> courses = ObjectManager.getCourses();
		courses.add(course);
	}
	
	public static void Read(String code, String name, int hours, int lab, String program, int sections, String instructor) { } 
	
	public static void Update(int ID, String code, String name, int hours, int lab, String program, int sections, String instructor) { }
	
	public static void Delete(int ID) { }
}
