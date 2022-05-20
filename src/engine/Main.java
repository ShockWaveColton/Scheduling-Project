package engine;

import objects.Report;

public class Main {
	
	public static Window window;
	
	public static void main(String[] args) {
		window = new Window(new ObjectManager());
	}
	
	//Returns the window we created to avoid static abuse, and allow us to use non static functions inside of our drawschedule function
	public static Window getWindow() {
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


/*
 *  Issues found by students:
 *  
 *  - Calculating the hours of an instructor causes it to do it twice
 *     Currently dividing final hours by 2 to get an estimate. I would like help with a fix on this
 *  - Not all courses may show from the dropdown in programs, seems to be affected by which instructor you selected before (maybe)
 *  - Loading a new database does not clear the selected items from the dropdown box and seems to create errors if new database is empty
 *  - Adding courses to schedule while in Program section instantly changes to Instructor section (only shows courses with same instructor)
 *  - Selecting an instructor from a dropdown, from an edit also selects it as normal (may be intentional)
 * 	- Side panel will close/shrink after adding courses and changing terms. (Found adding courses to Dale then going to Term 2 and back to Term 1)
 */
