package engine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFileChooser;
//import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import objects.Classroom;
import objects.Course;
import objects.Instructor;
import objects.Lesson;
import objects.Program;
import objects.Schedule; 

public class FileIO {
	private static String databaseName = "";
	private static boolean fileLoaded; 
	private static Connection conn = null; 
	private static Statement stmt = null;
	private static File fileInUse = null;
	
	private final static String csvPath = "schedule.csv";

	private static void Connect(String fileName) throws ClassNotFoundException, SQLException {
		if (conn == null) {
	    	Class.forName("org.sqlite.JDBC");            
			conn = DriverManager.getConnection("jdbc:sqlite:"+ fileName);
	    	if (conn != null) {
				conn.setAutoCommit(false);
			}
	        stmt = conn.createStatement();			
		}
	}
	private static void Disconnect() {
        try {
			stmt.close();
	        conn.close();
	        conn = null;
        } catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	public static int NewDatabase() {
		databaseName = JOptionPane.showInputDialog("What is the name of the new database file?");
		databaseName += ".db";
		if (databaseName.equals("null.db")) {
			JOptionPane.showMessageDialog(null, "Create new database cancelled.");
		} else {
	   	 	File tempFile = new File (databaseName);
				fileInUse = tempFile;
	   	 	if(tempFile.exists()) {
	   	 		JOptionPane.showMessageDialog(null, "File '" + databaseName + "' already exists. Select another name.");
	 		} else {
					try {
						Connect(databaseName);
						System.out.println("connected");
						String sql;
			        	sql = "CREATE TABLE programs " +
			                    "(program_id       INTEGER PRIMARY KEY AUTOINCREMENT," +
			        			" program_name	   TEXT    NOT NULL UNIQUE," +
			                    " program_schedule INTEGER NOT NULL," +
			                    " FOREIGN KEY(program_schedule) REFERENCES schedules (schedule_id))";
			        	stmt.executeUpdate(sql); 
			    		conn.commit(); 
			        	sql = "CREATE TABLE instructors " +
			                    "(instructor_id        INTEGER PRIMARY KEY AUTOINCREMENT," +
			                    " instructor_wNumber   TEXT    NOT NULL," +  
			                    " instructor_firstName TEXT    NOT NULL," +
			                    " instructor_lastName  TEXT    NOT NULL," +
			                    " instructor_phone     TEXT    NOT NULL," +
			                    " instructor_email     TEXT    NOT NULL," +
			                    " instructor_schedule  INTEGER NOT NULL," +
			                    " FOREIGN KEY(instructor_schedule) REFERENCES schedules (schedule_id))";
			        	stmt.executeUpdate(sql); 
			    		conn.commit();
			        	sql = "CREATE TABLE classrooms" +
			                    "(classroom_id      INTEGER PRIMARY KEY AUTOINCREMENT," +
			                    " classroom_wing    TEXT NOT NULL," +
			                    " classroom_number  TEXT NOT NULL," +
			                    " classroom_labType INTEGER NOT NULL," +
			                    " UNIQUE(classroom_wing, classroom_number))";
			        	stmt.executeUpdate(sql);
			    		conn.commit();
			        	sql = "CREATE TABLE courses " +
			                    "(course_id         INTEGER PRIMARY KEY AUTOINCREMENT," +
			                    " course_code       TEXT    NOT NULL," +
			                    " course_name       TEXT    NOT NULL," +
			                    " course_section    INTEGER NOT NULL," + 
			                    " course_hours      INTEGER NOT NULL," +
			                    " course_lab        INTEGER NOT NULL," +
			                    " course_program    INTEGER NOT NULL," +
			                    " course_semester   INTEGER NOT NULL," +
			                    " course_instructor INTEGER NOT NULL," +
			                    " course_classroom  INTEGER NOT NULL," +
			                  //" FOREIGN KEY(course_program)    REFERENCES programs   (program_id),"+  // Not a foreign key, else I have to make a lookup table, and then there are multiple courses.
			                    " FOREIGN KEY(course_classroom)  REFERENCES classrooms (classroom_id),"+
			                    " FOREIGN KEY(course_instructor) REFERENCES instructors(instructor_id))";
			        	stmt.executeUpdate(sql); 
			    		conn.commit();
			    		sql = "CREATE TABLE lessons " +
					    		"(lesson_id         INTEGER PRIMARY KEY AUTOINCREMENT," + 
					    		" lesson_course     INTEGER NOT NULL," +
					    		" lesson_instructor INTEGER NOT NULL," +
					    		" lesson_classroom  INTEGER NOT NULL," +
					    		" FOREIGN KEY(lesson_course)     REFERENCES courses     (course_id),"+
					    		" FOREIGN KEY(lesson_instructor) REFERENCES instructors (instructor_id),"+
					    		" FOREIGN KEY(lesson_classroom)  REFERENCES classrooms  (classroom_id))";
			        	stmt.executeUpdate(sql); 
			    		conn.commit();			    		
			    		sql = "CREATE TABLE schedules " +
					    		"(schedule_id   INTEGER     PRIMARY KEY AUTOINCREMENT," +			    				
					    		" schedule_name	VARCHAR(50) NOT NULL," +
					    		" schedule_term	INTEGER     NOT NULL," +
					    		" schedule_11   INTEGER," +
					    		" schedule_12   INTEGER," +
					    		" schedule_13   INTEGER," +
					    		" schedule_14   INTEGER," +
					    		" schedule_15   INTEGER," +
					    		" schedule_16   INTEGER," +
					    		" schedule_17   INTEGER," +
					    		" schedule_18   INTEGER," +
					    		" schedule_19   INTEGER," +
					    		" schedule_21   INTEGER," +
					    		" schedule_22   INTEGER," +
					    		" schedule_23   INTEGER," +
					    		" schedule_24   INTEGER," +
					    		" schedule_25   INTEGER," +
					    		" schedule_26   INTEGER," +
					    		" schedule_27   INTEGER," +
					    		" schedule_28   INTEGER," +
					    		" schedule_29   INTEGER," +
					    		" schedule_31   INTEGER," +
					    		" schedule_32   INTEGER," +
					    		" schedule_33   INTEGER," +
					    		" schedule_34   INTEGER," +
					    		" schedule_35   INTEGER," +
					    		" schedule_36   INTEGER," +
					    		" schedule_37   INTEGER," +
					    		" schedule_38   INTEGER," +
					    		" schedule_39   INTEGER," +
					    		" schedule_41   INTEGER," +
					    		" schedule_42   INTEGER," +
					    		" schedule_43   INTEGER," +
					    		" schedule_44   INTEGER," +
					    		" schedule_45   INTEGER," +
					    		" schedule_46   INTEGER," +
					    		" schedule_47   INTEGER," +
					    		" schedule_48   INTEGER," +
					    		" schedule_49   INTEGER," +
					    		" schedule_51   INTEGER," +
					    		" schedule_52   INTEGER," +
					    		" schedule_53   INTEGER," +
					    		" schedule_54   INTEGER," +
					    		" schedule_55   INTEGER," +
					    		" schedule_56   INTEGER," +
					    		" schedule_57   INTEGER," +
					    		" schedule_58   INTEGER," +
					    		" schedule_59   INTEGER)";
			        	stmt.executeUpdate(sql); 
			    		conn.commit();
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
					} finally {
						Disconnect();
						System.out.println("disconnected");

					}
					if(tempFile.exists()) {
			   	 		JOptionPane.showMessageDialog(null, "Database file '" + databaseName + "' created successfully.");
			   	 		fileLoaded = true;
			   	 		return 0;
					}else {
					JOptionPane.showMessageDialog(null, "Something went wrong. Try again.");
					return -1;
				}
 			}
		}
		return -1;
    }
//  Read each row of each table and load into appropriate object builders.
//  The order here is paramount. EX: Since each Program has a Schedule, Schedules must be loaded first	
	public static int LoadDatabase() {
    	final JFileChooser fileChooser = new JFileChooser(".\\");
    	int selectFile = fileChooser.showOpenDialog(null);
    	if (selectFile == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
			fileInUse = file;
            databaseName = file.getName();
            try {
				ObjectManager.ClearData();
                Connect(file.toString());
    			System.out.println("connected");
                ResultSet query;
                // get all schedules from database, pass each row in to Schedules builder.

                query = stmt.executeQuery("SELECT * FROM schedules ORDER BY schedule_id ASC");
                while ( query.next()) {
                	int ID      = query.getInt   ("schedule_id");
                	String name = query.getString("schedule_name");
                	int term    = query.getInt   ("schedule_term");
                	Schedule.ReadPass1(ID, name, term);
                }
                // get all programs from database, pass each row in to Program builder.
                query = stmt.executeQuery("SELECT * FROM programs ORDER BY program_id ASC");
                while ( query.next()) {
                	int    ID    = query.getInt   ("program_id");
                	String name  = query.getString("program_name");
                	int schedule = query.getInt   ("program_schedule");
                	Program.Read(ID, name, schedule);
                }
                // get all instructors from database, pass row in to Instructor builder.
                query = stmt.executeQuery("SELECT * FROM instructors ORDER BY instructor_firstName ASC, instructor_lastName ASC, instructor_wNumber ASC");
                while ( query.next()) {
                	int ID           = query.getInt   ("instructor_id");
                	String wNumber   = query.getString("instructor_wNumber");
                	String firstName = query.getString("instructor_firstName"); 
                	String lastName  = query.getString("instructor_lastName");
                	String phone     = query.getString("instructor_phone");
                	String email     = query.getString("instructor_email");
                	int schedule     = query.getInt   ("instructor_schedule");
                	Instructor.Read(ID, wNumber, firstName, lastName, phone, email, schedule);
                }
                // get all classrooms from database, pass row in to classroom builder.
                query = stmt.executeQuery("SELECT * FROM classrooms ORDER BY classroom_wing ASC, classroom_number ASC");
                while ( query.next()) {
                	int ID    = query.getInt   ("classroom_id");
                	String wing   = query.getString("classroom_wing"); 
                	String number = query.getString("classroom_number");
                	int labType   = query.getInt   ("classroom_labType");
                	Classroom.Read(ID, wing, number, labType);
                }
                // get all courses from database, pass row in to courses builder.
                query = stmt.executeQuery("SELECT * FROM courses ORDER BY course_code ASC, course_section ASC");
                while ( query.next()) {
                	int ID = query.getInt        ("course_id");
                	int labType = query.getInt   ("course_lab");
                	String code = query.getString("course_code");  
                	String name = query.getString("course_name");
                	int hours = query.getInt     ("course_hours");
                	int section = query.getInt   ("course_section");
                	int program = query.getInt   ("course_program");
                	int semester = query.getInt  ("course_semester");
                	int classroom = query.getInt ("course_classroom");
                	int instructor = query.getInt("course_instructor");
                	Course.Read(ID, code, name, section, hours, labType, program, semester, instructor, classroom);
                }
                query = stmt.executeQuery("SELECT * FROM lessons ORDER BY lesson_id ASC");
                while ( query.next()) {
                	int ID   = query.getInt      ("lesson_id");
                	int course = query.getInt    ("lesson_course");
                	int instructor = query.getInt("lesson_instructor");
                	int classroom = query.getInt ("lesson_classroom");
                	Lesson.Read(ID, course, instructor, classroom);
                }
                query = stmt.executeQuery("SELECT * FROM schedules ORDER BY schedule_id ASC");
                while ( query.next()) {
                	int ID      = query.getInt   ("schedule_id");
                	int scheduleData[][] = new int[5][9]; //Easier to grab 45 lessons in a loop:
                	// I don't like hardcoding for loops like this, but I don't have a variable
                	// for dimensions (since every week is 5 days x 9 hours).
                	for (int x = 0; x < 5; x++) {
                    	for (int y = 0; y < 9; y++) {
                    		scheduleData[x][y] = query.getInt("schedule_"+(x+1)+(y+1));
                    	}
                	}
                	Schedule.ReadPass2(ID, scheduleData);
                }                
 	   	 		fileLoaded = true;
                JOptionPane.showMessageDialog(null, "Loaded database file " + databaseName + " successfully!");
                return 0;
            } catch (Exception e) {
            	e.printStackTrace();
            	JOptionPane.showMessageDialog(null, "Something went wrong.");
    		} finally {
    			Disconnect();
    			System.out.println("disconnected.");
    		}
//        } else {
//        	JOptionPane.showMessageDialog(null, "Load file failed. Try again.");
//        }
    	}
		return -1;
	}
	public static int ReloadDatabase() {
			File file = fileInUse;
            databaseName = file.getName();
            try {
                Connect(file.toString());
    			System.out.println("connected");
                ResultSet query;
                // get all schedules from database, pass each row in to Schedules builder.

                query = stmt.executeQuery("SELECT * FROM schedules ORDER BY schedule_id ASC");
                while ( query.next()) {
                	int ID      = query.getInt   ("schedule_id");
                	String name = query.getString("schedule_name");
                	int term    = query.getInt   ("schedule_term");
                	Schedule.ReadPass1(ID, name, term);
                }
                // get all programs from database, pass each row in to Program builder.
                query = stmt.executeQuery("SELECT * FROM programs ORDER BY program_id ASC");
                while ( query.next()) {
                	int    ID    = query.getInt   ("program_id");
                	String name  = query.getString("program_name");
                	int schedule = query.getInt   ("program_schedule");
                	Program.Read(ID, name, schedule);
                }
                // get all instructors from database, pass row in to Instructor builder.
                query = stmt.executeQuery("SELECT * FROM instructors ORDER BY instructor_firstName ASC, instructor_lastName ASC, instructor_wNumber ASC");
                while ( query.next()) {
                	int ID           = query.getInt   ("instructor_id");
                	String wNumber   = query.getString("instructor_wNumber");
                	String firstName = query.getString("instructor_firstName"); 
                	String lastName  = query.getString("instructor_lastName");
                	String phone     = query.getString("instructor_phone");
                	String email     = query.getString("instructor_email");
                	int schedule     = query.getInt   ("instructor_schedule");
                	Instructor.Read(ID, wNumber, firstName, lastName, phone, email, schedule);
                }
                // get all classrooms from database, pass row in to classroom builder.
                query = stmt.executeQuery("SELECT * FROM classrooms ORDER BY classroom_wing ASC, classroom_number ASC");
                while ( query.next()) {
                	int ID    = query.getInt   ("classroom_id");
                	String wing   = query.getString("classroom_wing"); 
                	String number = query.getString("classroom_number");
                	int labType   = query.getInt   ("classroom_labType");
                	Classroom.Read(ID, wing, number, labType);
                }
                // get all courses from database, pass row in to courses builder.
                query = stmt.executeQuery("SELECT * FROM courses ORDER BY course_code ASC, course_section ASC");
                while ( query.next()) {
                	int ID = query.getInt        ("course_id");
                	int labType = query.getInt   ("course_lab");
                	String code = query.getString("course_code");  
                	String name = query.getString("course_name");
                	int hours = query.getInt     ("course_hours");
                	int section = query.getInt   ("course_section");
                	int program = query.getInt   ("course_program");
                	int semester = query.getInt  ("course_semester");
                	int classroom = query.getInt ("course_classroom");
                	int instructor = query.getInt("course_instructor");
                	Course.Read(ID, code, name, section, hours, labType, program, semester, instructor, classroom);
                }
                query = stmt.executeQuery("SELECT * FROM lessons ORDER BY lesson_id ASC");
                while ( query.next()) {
                	int ID   = query.getInt      ("lesson_id");
                	int course = query.getInt    ("lesson_course");
                	int instructor = query.getInt("lesson_instructor");
                	int classroom = query.getInt ("lesson_classroom");
                	Lesson.Read(ID, course, instructor, classroom);
                }
                query = stmt.executeQuery("SELECT * FROM schedules ORDER BY schedule_id ASC");
                while ( query.next()) {
                	int ID      = query.getInt   ("schedule_id");
                	int scheduleData[][] = new int[5][9]; //Easier to grab 45 lessons in a loop:
                	// I don't like hardcoding for loops like this, but I don't have a variable
                	// for dimensions (since every week is 5 days x 9 hours).
                	for (int x = 0; x < 5; x++) {
                    	for (int y = 0; y < 9; y++) {
                    		scheduleData[x][y] = query.getInt("schedule_"+(x+1)+(y+1));
                    	}
                	}
                	Schedule.ReadPass2(ID, scheduleData);
                }                
 	   	 		fileLoaded = true;
                return 0;
            } catch (Exception e) {
            	e.printStackTrace();
            	JOptionPane.showMessageDialog(null, "Something went wrong.");
    		} finally {
    			Disconnect();
    			System.out.println("disconnected.");
    		}
//        } else {
//        	JOptionPane.showMessageDialog(null, "Load file failed. Try again.");
//        }
    	
		return -1;
	}
	public static int CreateProgram(String name, int schedule_id) {
		//Add new program to database, get ID of new database, return ID to caller (to add to array list). 
		int ID = 0;
		try {
			Connect(databaseName);
			System.out.println("connected");
			String sql = "INSERT INTO programs (program_name, program_schedule)" +
					" VALUES ('"+ name +"', " + schedule_id + ");";
			stmt.executeUpdate(sql);
			conn.commit();
			ResultSet query;
			query = stmt.executeQuery("SELECT * FROM programs WHERE program_id = (SELECT MAX(program_id) FROM programs)");
			ID = query.getInt("program_id");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Disconnect();
			System.out.println("disconnected.");
		}
		return ID;
	}
	public static void UpdateProgram(int ID, String name) {
		try {
			Connect(databaseName);
			System.out.println("connected");
			String sql = "UPDATE programs" +
					" SET program_name = '" + name + "'" +
					" WHERE program_id = '" + ID + "'";
	    	stmt.executeUpdate(sql);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Something went wrong.");
		} finally {
			Disconnect();
			System.out.println("disconnected.");
		}
	}
	public static void DeleteProgram(int ID) {
		try {
			Connect(databaseName);
			System.out.println("connected");
			String sql = "DELETE FROM programs" +
					" WHERE program_id = '" + ID +"'";
	    	stmt.executeUpdate(sql);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Something went wrong.");
		} finally {
			Disconnect();
			System.out.println("disconnected.");
		}
	}
	public static int CreateClassroom(String wing, String number, int lab) {
		int ID = 0;
		try {
			Connect(databaseName);
			System.out.println("connected");
			String sql = "INSERT INTO classrooms (classroom_wing, classroom_number, classroom_labType)" +
					" VALUES ('"+ wing +"', '"+ number +"', '" + lab + "') ";
			stmt.executeUpdate(sql);
			conn.commit();
			ResultSet query;
			query = stmt.executeQuery("SELECT * FROM Classrooms WHERE classroom_wing = '" + wing + "' AND classroom_number = '" + number + "'");
			ID = query.getInt("classroom_id");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Disconnect();
			System.out.println("disconnected.");
		}
		return ID;		
	}
	public static void UpdateClassroom(int ID, String wing, String number, int labType) {
		try {
			Connect(databaseName);
			System.out.println("connected");
			String sql = "UPDATE classrooms" +
					" SET classroom_wing = '" + wing + "', classroom_number = '" + number + "', classroom_labType = '" + labType + "'" +
					" WHERE classroom_id = '" + ID + "'";
	    	stmt.executeUpdate(sql);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Something went wrong.");
		} finally {
			Disconnect();
			System.out.println("disconnected.");
		}
	}
	public static void DeleteClassroom(int ID) {
		try {
			Connect(databaseName);
			System.out.println("connected");
			String sql = "DELETE FROM classrooms" +
					" WHERE classroom_id = '" + ID +"'";
	    	stmt.executeUpdate(sql);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Something went wrong.");
		} finally {
			Disconnect();
			System.out.println("disconnected.");
		}
	}
	public static int CreateInstructor(String wNumber, String firstName, String lastName, String phone, String email, int schedule_id) {
		int ID = 0;
		try {
			Connect(databaseName);
			System.out.println("connected");
	    	String sql = "INSERT INTO instructors (instructor_wNumber, instructor_firstName, instructor_lastName, instructor_phone, instructor_email, instructor_schedule)" +
	              " VALUES ('"+ wNumber +"', '"+ firstName +"', '" + lastName + "', '"+ phone +"', '" + email + "', " + schedule_id + ")";
	    	stmt.executeUpdate(sql);
	    	conn.commit();
	    	ResultSet query;
	        query = stmt.executeQuery("SELECT * FROM instructors WHERE instructor_wNumber = '" + wNumber + "'");
	        ID = query.getInt("instructor_id");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Disconnect();
			System.out.println("disconnected.");
		}
		return ID;		
	}
	public static void UpdateInstructor(int ID, String wNumber, String firstName, String lastName, String phone, String email, int schedule_id) {
		try {
			Connect(databaseName);
			System.out.println("connected");
			String sql = "UPDATE instructors" +
					" SET instructor_wNumber = '" + wNumber + "', instructor_firstName = '" + firstName + "', instructor_lastName = '" + lastName + "', instructor_phone = '" + phone + "', instructor_email = '" + email + "', instructor_schedule = '" + schedule_id + "'" +
					" WHERE instructor_id = '" + ID + "'";
	    	stmt.executeUpdate(sql);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Something went wrong.");
		} finally {
			Disconnect();
			System.out.println("disconnected.");
		}
	}
	public static void DeleteInstructor(int ID) {
		try {
			Connect(databaseName);
			System.out.println("connected");
			String sql = "DELETE FROM instructors" +
					" WHERE instructor_id = '" + ID +"'";
	    	stmt.executeUpdate(sql);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Something went wrong.");
		} finally {
			Disconnect();
			System.out.println("disconnected.");
		}
	}
	public static int CreateCourse(String code, String name, int section, int hours, int lab, int program, int semester, int instructor, int classroom) {
		int ID = 0;
		try {
			Connect(databaseName);		 
			System.out.println("connected");
			String sql = "INSERT INTO courses (course_code, course_name, course_hours, course_lab, course_section, course_program, course_semester, course_instructor, course_classroom)" +
	              " VALUES ('" + code + "', '" + name + "', " + hours + ", "+ lab + ", " + section + ", " + program + ", " + semester + ", " + instructor + ", " + classroom + ")";
	    	stmt.executeUpdate(sql);
	    	conn.commit();
	    	ResultSet query;
	        query = stmt.executeQuery("SELECT * FROM courses WHERE course_code = '" + code + "' AND course_section = " + section);
	        ID = query.getInt("course_id");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Disconnect();
			System.out.println("disconnected.");
		}
		return ID;
	}
	public static void UpdateCourse(int ID, String code, String name, int section, int hours, int lab, int program, int semester, int instructor, int classroom) {
		try {
			Connect(databaseName);
			System.out.println("connected");
			String sql = "UPDATE courses" +
					" SET course_code = '" + code + "', course_name = '" + name + "', course_section = '" + section + "', course_hours = '" + hours + "', course_lab = '" + lab + "', course_program = '" + program + "', course_semester = '" + semester + "', course_instructor = '" + instructor + "', course_classroom = '" + classroom + "'" +
					" WHERE course_id = '" + ID + "'";
	    	stmt.executeUpdate(sql);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Something went wrong.");
		} finally {
			Disconnect();
			System.out.println("disconnected.");
		}
	}
	public static void DeleteCourse(int ID) {
		try {
			Connect(databaseName);
			System.out.println("connected");
			String sql = "DELETE FROM courses" +
					" WHERE course_id = '" + ID +"'";
	    	stmt.executeUpdate(sql);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Something went wrong.");
		} finally {
			Disconnect();
			System.out.println("disconnected.");
		}
	}
	public static int CreateSchedule(String name, boolean isProgram) {
		int ID = 0;
		try {
			Connect(databaseName);
			System.out.println("connected");
			String sql = "INSERT INTO schedules (schedule_name, schedule_term)" +
					" VALUES ('" + name + "', 1);";
	    	stmt.executeUpdate(sql);
	    	conn.commit();
			sql = "INSERT INTO schedules (schedule_name, schedule_term)" +
					" VALUES ('" + name + "', 2);";
	    	stmt.executeUpdate(sql);
	    	conn.commit();
	    	if (isProgram) {
				sql = "INSERT INTO schedules (schedule_name, schedule_term)" +
						" VALUES ('" + name + "', 3);";
		    	stmt.executeUpdate(sql);
		    	conn.commit();
				sql = "INSERT INTO schedules (schedule_name, schedule_term)" +
						" VALUES ('" + name + "', 4);";
		    	stmt.executeUpdate(sql);
		    	conn.commit();	    		
	    	}
	    	ResultSet query;
	        query = stmt.executeQuery("SELECT schedule_id FROM schedules WHERE schedule_id=(SELECT max(schedule_id) FROM schedules);");
	        if (isProgram)
		        ID = query.getInt("schedule_id") - 3; // We want the ID of the first insert
	        else
	        	ID = query.getInt("schedule_id") - 1; // See above
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Something went wrong.");
		} finally {
			Disconnect();
			System.out.println("disconnected.");
		}
		return ID;
	}
	public static void ReadSchedule(int schedule_id) {
		int ID = 0;
		String name = "";
		int term = 0;
		int[][] scheduleData = new int[5][9];
		try {
			Connect(databaseName);
			System.out.println("connected");
			ResultSet query = stmt.executeQuery("SELECT * FROM schedules WHERE schedule_id = " + schedule_id +";");
            while ( query.next()) {
            	ID   = query.getInt   ("schedule_id");
            	name = query.getString("schedule_name");
            	term = query.getInt   ("schedule_term");
            	for (int x = 1; x < 5; x++) {
                	for (int y = 1; y < 9; y++) {
                		scheduleData[x][y] = query.getInt("schedule_"+x+y);
                	}
            	}
            }
		} catch (Exception e) {
        	e.printStackTrace();
        	JOptionPane.showMessageDialog(null, "Something went wrong.");
    	} finally {
    		Disconnect();
			System.out.println("disconnected.");
    	}
    	Schedule.FullRead(ID, name, term, scheduleData);            	
	}
	// Adding a new lesson to a schedule
	public static void updateSchedule(int ID, String scheduleLocation, int scheduleValue) {
		try {
			Connect(databaseName);
			String sql = "UPDATE schedules" +
					" SET schedule_" + scheduleLocation + " = " + scheduleValue +
					" WHERE schedule_id = " + ID;
			stmt.executeUpdate(sql);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Something went wrong.");
		} finally {
			Disconnect();
		}		
	}
	
	// Export the schedule to a csv file
	public static void exportSchedule() {
		try {
			Connect(databaseName);
			
			String sql = "SELECT * FROM schedules";
			
			ResultSet result = stmt.executeQuery(sql);
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(csvPath));
			
			writer.write("schedule_id,schedule_name,schedule_term");
			
			while(result.next()) {
				int schedule_id = result.getInt("schedule_id");
				String schedule_name = result.getString("schedule_name");
				int schedule_term = result.getInt("schedule_term");
				
                String line = String.format("\"%d\",%s,%d",
                        schedule_id, schedule_name, schedule_term);
                
                writer.newLine();
                
                writer.write(line);
                
			}
			
			writer.close();
			
		}catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Something went wrong.");
		}finally {
			Disconnect();
			JOptionPane.showMessageDialog(null, "File exported succesfully!");
		}
	}
	
	// Deleting a lesson from a schedule.
	public static void updateSchedule(int ID, String scheduleLocation) {
		try {
			Connect(databaseName);
			String sql = "UPDATE schedules" +
					" SET schedule_" + scheduleLocation + " = NULL" +
					" WHERE schedule_id = " + ID;
			stmt.executeUpdate(sql);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Something went wrong.");
		} finally {
			Disconnect();
		}		
	}
	public static int CreateLesson(Course course, Instructor instructor, Classroom classroom) {
		int ID = 0;
		try {
			Connect(databaseName);
			System.out.println("connected");
			String sql = "INSERT INTO lessons (lesson_course, lesson_instructor, lesson_classroom)" +
					" VALUES (" + course.getID() + ", " + instructor.getID() + ", " + classroom.getID() + ");";
	    	stmt.executeUpdate(sql);
	    	conn.commit();
	    	ResultSet query;
	        query = stmt.executeQuery("SELECT * FROM lessons WHERE lesson_id = (SELECT MAX(lesson_id) FROM lessons)");
	        ID = query.getInt("lesson_id");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Something went wrong.");
		} finally {
			Disconnect();
			System.out.println("disconnected.");
		}
		return ID;
	}
	public static void UpdateClass(int ID, Course course, Instructor instructor, Classroom classroom) {
		try {
			Connect(databaseName);
			System.out.println("connected");
			//TODO: update class data here.
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Something went wrong.");
		} finally {
			Disconnect();
			System.out.println("disconnected.");
		}
	}
	public static void DeleteClass(int ID) {
		try {
			Connect(databaseName);
			System.out.println("connected");
			String sql = "DELETE FROM classes" +
					" WHERE class_id = '" + ID +"'";
	    	stmt.executeUpdate(sql);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Something went wrong.");
		} finally {
			Disconnect();
			System.out.println("disconnected.");
		}
	}
	public static boolean isFileLoaded() { return fileLoaded; }
}  