package objects;

import java.util.ArrayList;

import engine.FileIO;
import engine.ObjectManager;

// This is the Course Class file.
// Note that the constructor is private, I wanted the class file to control all of the creation,
// rather than giving some of that power to the window class.
// use <course object name>.Create() to create a new classroom object and add that classroom to the arraylist.

// Member variables:
public class Course {
	private int ID;
	private int lab;
	private int hours;
	private String code;
	private String name;
	private int section;
	private int program;
	private int semester;
	private int classroom;
	private int instructor;
	private String fullName;

	// Constructor:
	private Course(int ID, String code, String name, int section, int hours, int lab, int program, int semester, int instructor, int classroom) {
		this.ID = ID;
		this.lab = lab;
		this.code = code;
		this.name = name;
		this.hours = hours;
		this.section = section;
		this.program = program;		
		this.semester = semester;
		this.classroom = classroom;
		this.instructor = instructor;
		this.fullName = code + "-" + section;
	}

	// Accessors:
	public int 	  getID()         { return this.ID; }
	public int    getLab() 	      { return this.lab; }
	public String getCode()       { return this.code; }
	public String getName()       { return this.name; }
	public int    getHours()      { return this.hours; }
	public int    getSection()    { return this.section; }
	public int    getProgram()    { return this.program; }
	public int    getSemester()   { return this.semester; }
	public String getFullName()   { return this.fullName; }
	public int	  getClassroom()  { return this.classroom; }
	public int    getInstructor() { return this.instructor; }

	public static void Create(String code, String name, int section, int hours, int lab, int program, int semester, int instructor, int classroom) {
		int ID = FileIO.CreateCourse(code, name, section, hours, lab, program, semester, instructor, classroom);
		ObjectManager.AddCourseToList(new Course(ID, code, name, section, hours, lab, program, semester, instructor, classroom));
	}
	
	public static void Read(int ID, String code, String name, int hours, int labType, int section, int program, int semester, int instructor, int classroom) {
		ObjectManager.AddCourseToList(new Course(ID, code, name, hours, labType, section, program, semester, instructor, classroom));
	} 
	
	public static void Update(int index, int ID, String code, String name, int section, int hours, int lab, int program, int semester, int instructor, int classroom) {
		FileIO.UpdateCourse(ID, code, name, section, hours, lab, program, semester, instructor, classroom);
		// At this point updating instructor in database file is successful, so we update applications array list.
		ObjectManager.UpdateCourseInList(index, new Course(ID, code, name, section, hours, lab, program, semester, instructor, classroom));		          
	}
	
	public static void Delete(int ID) {
		FileIO.DeleteCourse(ID);
		ArrayList<Course> courses = ObjectManager.getCourses();
		for (int i = 0; i < courses.size(); i++) {
			if (courses.get(i).getID() == ID)
				courses.remove(i);
		}
	}
}
