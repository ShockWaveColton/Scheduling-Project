package engine;

import java.util.ArrayList;

import objects.Classroom;
import objects.Course;
import objects.Instructor;
import objects.Program;
import objects.Schedule;

public class ObjectManager {
	static ArrayList<Program> programs;
	static ArrayList<Course> courses;
	static ArrayList<Instructor> instructors;
	static ArrayList<Classroom> classrooms;
	static ArrayList<Schedule> schedules;
	public ObjectManager() {
		programs = new ArrayList<>();
		courses = new ArrayList<>();
		instructors = new ArrayList<>();
		classrooms = new ArrayList<>();
		schedules = new ArrayList<>();
	}
	
	//Accessors:
	public static ArrayList<Program> getPrograms() {return programs;}
	public static ArrayList<Course> getCourses() {return courses;}
	public static ArrayList<Instructor> getInstructors() {return instructors;}
	public static ArrayList<Classroom> getClassrooms() {return classrooms;}
	public static ArrayList<Schedule> getSchedules() {return schedules;}
	
	//Add to Lists:
	public static void addClassroomToList(Classroom classroom) {
		classrooms.add(classroom);
		Schedule.Create(classroom.getWing() + "-" + classroom.getRoom());
	} 

	public static void addCourseToList(Course course) {
		courses.add(course);
	}

	public static void addInstructorToList(Instructor instructor) {
		instructors.add(instructor);
		Schedule.Create(instructor.getW_Number());
	}
	
	public static void addProgramToList(Program program) {
		programs.add(program);
		Schedule.Create(program.getName());
	}
	
	public static void addScheduleToList(Schedule schedule) {
		schedules.add(schedule);
	} 
}