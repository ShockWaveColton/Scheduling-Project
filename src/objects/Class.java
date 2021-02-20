package objects;

public class Class {
//	private Program program;
	private Course course;
	private Instructor instructor;
	private Classroom classroom;
	
	public Class(Program program, Course course, Instructor instructor, Classroom classroom) {
//		this.program = program;
		this.course = course;
		this.instructor = instructor;
		this.classroom = classroom;
	}
	
	public String Display() {
		return "oi!";
				//"Course: " + this.course.getName() + "\nInstructor: " + this.instructor.getName() + "\nClassroom: " + this.classroom.getName();
	}
}