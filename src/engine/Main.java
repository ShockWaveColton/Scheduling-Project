package engine;

public class Main {
<<<<<<< HEAD

	public static Window window;
	public static void main(String[] args) {
		window = new Window(new ObjectManager());
	}

	public static Window getWindow()
	{
=======
	
	public static Window window;
	
	public static void main(String[] args) {
		window = new Window(new ObjectManager());
	}
	
	//Returns the window we created to avoid static abuse, and allow us to use non static functions inside of our drawschedule function
	public static Window getWindow() {
>>>>>>> a42ce943cb539e6e3f09580ec0eeae382005c348
		return window;
	}
}

/*
 * known bugs/issues: See TODOs in window.java for approximate issue locations.
 * - Deleting a lesson from program schedule does not remove it from appropriate instructor schedule.
 * - Deleting a lesson from instructor crashes the app, because it does not yet fetch the associated program.
 * - Overwriting a lesson with any other lesson does not remove the original lessons from the OTHER schedules.
 * - Selecting any schedule block should trigger the Right Panel to reload.
 * 		Currently only selecting from the course list drop down does this
 * 		to workaround, pick schedule block first, then course from combobox.
 * 
 *  Features I would like to add:
 *  - Exporting data. under File >> Export, generate a .csv file for excel containing all the schedule data
 *  	For easy distribution of schedule data to other people.
 *  - Reports. Again under File >> Reports. Open a new JFrame that calculates the total hours
 *  	Of each instructor and program by term/semester.
 *  	Maybe include a list of courses with unscheduled hours, the sky is the limit. 
 */