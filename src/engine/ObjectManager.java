package engine;

import java.util.ArrayList;

import objects.Classroom;
import objects.Course;
import objects.Instructor;
import objects.Lesson;
import objects.Program;
import objects.Schedule;

public class ObjectManager {
	static ArrayList<Program> programs;
	static ArrayList<Course> courses;
	static ArrayList<Instructor> instructors;
	static ArrayList<Classroom> classrooms;
	static ArrayList<Schedule> schedules;
	static ArrayList<Lesson> lessons;
	public ObjectManager() {
		programs    = new ArrayList<>();
		courses     = new ArrayList<>();
		instructors = new ArrayList<>();
		classrooms  = new ArrayList<>();
		schedules   = new ArrayList<>();
		lessons     = new ArrayList<>();
	}
	
	//Accessors:
	public static ArrayList<Program>    getPrograms()        { return programs; }
	public static ArrayList<Course>     getCourses()         { return courses; }
	public static ArrayList<Instructor> getInstructors()     { return instructors; }
	public static ArrayList<Classroom>  getClassrooms()      { return classrooms; }
	public static ArrayList<Schedule>   getSchedules()       { return schedules; }
	public static ArrayList<Lesson>     getLessons()         { return lessons; }
	
	//Add to Lists:
	public static void AddProgramToList   (Program program)       { programs.add   (program);   }
	public static void AddInstructorToList(Instructor instructor) {	instructors.add(instructor);}
	public static void AddClassroomToList (Classroom classroom)   { classrooms.add (classroom); } 
	public static void AddCourseToList    (Course course)         { courses.add    (course);	}
	public static void AddScheduleToList  (Schedule schedule)     { schedules.add  (schedule);	}
	public static void AddLessonToList    (Lesson lesson )        { lessons.add    (lesson);	}
	
	//Update in Lists:
	public static void UpdateProgramInList   (int index, Program program)       { programs.set   (index, program);   }
	public static void UpdateInstructorInList(int index, Instructor instructor) { instructors.set(index, instructor);}
	public static void UpdateClassroomInList (int index, Classroom classroom)   { classrooms.set (index, classroom); } 
	public static void UpdateCourseInList    (int index, Course course)         { courses.set    (index, course);    }
	public static void UpdateScheduleInList  (int index, Schedule schedule)     { schedules.set  (index, schedule);  }
	public static void UpdateLessonInList    (int index, Lesson lesson )        { lessons.set    (index, lesson);    }

	//Remove from Lists:
	public static void RemoveProgramFromList(Program program)          { programs.remove   (program);   }
	public static void RemoveInstructorFromList(Instructor instructor) { instructors.remove(instructor);}
	public static void RemoveClassroomFromList(Classroom classroom)    { classrooms.remove (classroom); } 
	public static void RemoveCourseFromList(Course course)             { courses.remove    (course);    }
	public static void RemoveScheduleFromList(Schedule schedule)       { schedules.remove  (schedule);  }
	public static void RemoveLessonFromList(Lesson lesson )            { lessons.remove    (lesson);    }

	//Remove all data from all lists:
	public static void ClearData() {
		programs   .clear();
		instructors.clear();
		classrooms .clear();
		courses    .clear();
		schedules  .clear();
		lessons    .clear();
	}
}