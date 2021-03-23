package objects;

import java.util.ArrayList;

import engine.FileIO;
import engine.ObjectManager;

// Member variables:
public class Lesson {
	private int ID;
	private Course course;
	private Instructor instructor;
	private Classroom classroom;
	
	// Constructor:
	private Lesson(int ID, Course course, Instructor instructor, Classroom classroom) {
		this.ID         = ID;
		this.course     = course;
		this.instructor = instructor;
		this.classroom  = classroom;
	}
	
	// Accessors:
	public int        getID()         { return this.ID; }
	public Course     getCourse()     { return this.course; }
	public Classroom  getClassroom()  { return this.classroom; }
	public Instructor getInstructor() { return this.instructor; }
	
	public static Lesson Create(Course course, Instructor instructor, Classroom classroom) {		
		// Check to see if lesson already exists:
		ArrayList<Lesson> lessons = ObjectManager.getLessons();
		for (int i = 1; i < lessons.size(); i++) {
			Course     tempCourse     = lessons.get(i).getCourse();
			Classroom  tempClassroom  = lessons.get(i).getClassroom();
			Instructor tempInstructor = lessons.get(i).getInstructor();
			if (course == tempCourse && classroom == tempClassroom && instructor == tempInstructor)
				return lessons.get(i);
		}
		// Lesson does not exist, create a new one:
		int ID = FileIO.CreateLesson(course, instructor, classroom);
		return new Lesson(ID, course, instructor, classroom);
	}
	
	public static void Read(int ID, int course_id, int instructor_id, int classroom_id) {
		Course course = null;
		ArrayList<Course> courses = ObjectManager.getCourses();
		for (int i = 0; i < courses.size(); i++) {
			if (courses.get(i).getID() == course_id) {
				course = courses.get(i);
				break;				
			}
		}
		
		Instructor instructor = null;
		ArrayList<Instructor> instructors = ObjectManager.getInstructors();
		for (int i = 0; i < instructors.size(); i++) {
			if (instructors.get(i).getID() == instructor_id) {
				instructor = instructors.get(i);
				break;
			}
		}

		Classroom classroom = null;
		ArrayList<Classroom> classrooms = ObjectManager.getClassrooms();
		for (int i = 0; i < classrooms.size(); i++) {
			if (classrooms.get(i).getID() == classroom_id) {
				classroom = classrooms.get(i);
				break;
			}
		}		
		ObjectManager.AddLessonToList(new Lesson(ID, course, instructor, classroom));
	}
}