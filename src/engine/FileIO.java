package engine;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import objects.Classroom;
import objects.Instructor; 

public class FileIO {
	private static String databaseName = "";
	private static boolean fileLoaded; 
	private static Connection conn; 
	private static Statement stmt; 

	private static void Connect(String fileName) throws ClassNotFoundException, SQLException {
		conn = null; 
		stmt = null;
    	Class.forName("org.sqlite.JDBC");            
		conn = DriverManager.getConnection("jdbc:sqlite:"+ fileName);
    	if (conn != null) { 
			conn.setAutoCommit(false);
		}
        stmt = conn.createStatement();
	}
	private static void Disconnect() {
        try {
			stmt.close();
	        conn.close();
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
	   	 	File testFile = new File (databaseName);
	   	 	if(testFile.exists()) {
	   	 		JOptionPane.showMessageDialog(null, "File '" + databaseName + "' already exists. Select another name.");
	 		} else {
					try {
						Connect(databaseName);
						String sql;
			        	sql = "CREATE TABLE classrooms " +
			                    "(classroom_id      INTEGER PRIMARY KEY AUTOINCREMENT," +
			                    " classroom_wing    TEXT NOT NULL," +
			                    " classroom_number  TEXT NOT NULL," +
			                    " classroom_labType INTEGER NOT NULL)";
			        	stmt.executeUpdate(sql);
			    		conn.commit();
			        	sql = "CREATE TABLE courses " +
			                    "(course_id         INTEGER PRIMARY KEY AUTOINCREMENT," +
			                    " course_lab        INTEGER NOT NULL," +
			                    " course_code       TEXT NOT NULL UNIQUE," +
			                    " course_name       TEXT NOT NULL," +
			                    " course_hours      INTEGER NOT NULL," +
			                    " course_program    TEXT NOT NULL," +
			                    " course_sections   INTEGER NOT NULL," + 
			                    " course_instructor TEXT NOT NULL)";
			        	stmt.executeUpdate(sql); 
			    		conn.commit();
			        	sql = "CREATE TABLE instructors " +
			                    "(instructor_id        INTEGER PRIMARY KEY AUTOINCREMENT," +
			                    " instructor_wNumber   TEXT NOT NULL UNIQUE," +  
			                    " instructor_firstName TEXT NOT NULL," +
			                    " instructor_lastName  TEXT NOT NULL," +
			                    " instructor_phone     TEXT NOT NULL," +
			                    " instructor_email     TEXT NOT NULL UNIQUE)";
			        	stmt.executeUpdate(sql); 
			    		conn.commit();
			        	sql = "CREATE TABLE programs " +
			                    "(program_id       INTEGER PRIMARY KEY AUTOINCREMENT," +
			        			" program_name	   TEXT NOT NULL UNIQUE," +  
			                    " program_semester TEXT NOT NULL)";
			        	stmt.executeUpdate(sql); 
			    		conn.commit(); 
			    		Disconnect();
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
					}
					if(testFile.exists()) {
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
	public static int Load() {
//    	final JFileChooser fileChooser = new JFileChooser(".\\");
//    	int selectFile = fileChooser.showOpenDialog(null);
//    	if (selectFile == JFileChooser.APPROVE_OPTION) {
            File file = new File(".\\Sched.db");//fileChooser.getSelectedFile();
            databaseName = file.getName();
            try {
                Connect(file.toString());
                ResultSet query;
                query = stmt.executeQuery("SELECT * FROM classrooms");
                while ( query.next()) {
                	int ID    = query.getInt   ("classroom_id");
                	String wing   = query.getString("classroom_wing"); 
                	String number = query.getString("classroom_number");
                	int labType   = query.getInt   ("classroom_labType");
                	Classroom.Read(ID, wing, number, labType);
                }
                query = stmt.executeQuery("SELECT * FROM instructors");
                while ( query.next()) {
                	int ID           = query.getInt   ("instructor_id");
                	String wNumber   = query.getString("instructor_wNumber");
                	String firstName = query.getString("instructor_firstName"); 
                	String lastName  = query.getString("instructor_lastName");
                	String phone     = query.getString("instructor_phone");
                	String email     = query.getString("instructor_email");
                	Instructor.Read(ID, wNumber, firstName, lastName, phone, email);
                }
 	   	 		fileLoaded = true;
                JOptionPane.showMessageDialog(null, "Loaded database file " + databaseName + " successfully!");
                Disconnect();
                return 0;
            } catch (Exception e) {
            	e.printStackTrace();
            	JOptionPane.showMessageDialog(null, "Something went wrong.");
			}
//        } else {
//        	JOptionPane.showMessageDialog(null, "Load file failed. Try again.");
//        }
		return -1;
	}
	public static int AddClassroom(String wing, String number, int lab) {
		int ID = 0;
		try {
		Connect(databaseName);
		String sql;
		
    	sql = "INSERT INTO classrooms (classroom_wing, classroom_number, classroom_labType)" +
              " VALUES ('"+ wing +"', '"+ number +"', '" + lab + "') ";
    	stmt.executeUpdate(sql);
    	conn.commit();
    	ResultSet query;
        query = stmt.executeQuery("SELECT * FROM Classrooms WHERE classroom_wing = '" + wing + "' AND classroom_number = '" + number + "'");
        ID = query.getInt("classroom_id");
		Disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ID;		
	}
	public static void UpdateClassroom(int ID, String wing, String number, int labType) {
		try {
			Connect(databaseName);
			String sql = "UPDATE classrooms" +
					" SET classroom_wing = '" + wing + "', classroom_number = '" + number + "', classroom_labType = '" + labType + "'" +
					" WHERE classroom_id = '" + ID + "'";
	    	stmt.executeUpdate(sql);
			conn.commit();
			Disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Something went wrong.");
		}
	}
	public static void DeleteClassroom(int ID) {
		try {
			Connect(databaseName);
			String sql = "DELETE FROM classrooms" +
					" WHERE classroom_id = '" + ID +"'";
	    	stmt.executeUpdate(sql);
			conn.commit();
			Disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Something went wrong.");
		}
	}
	public static int AddInstructor(String wNumber, String firstName, String lastName, String phone, String email) {
		int ID = 0;
		try {
		Connect(databaseName);
		String sql;		
    	sql = "INSERT INTO instructors (instructor_wNumber, instructor_firstName, instructor_lastName, instructor_phone, instructor_email)" +
              " VALUES ('"+ wNumber +"', '"+ firstName +"', '" + lastName + "', '"+ phone +"', '"+email+"')";
    	stmt.executeUpdate(sql);
    	conn.commit();
    	ResultSet query;
        query = stmt.executeQuery("SELECT * FROM instructors WHERE instructor_wNumber = '" + wNumber + "'");
        ID = query.getInt("instructor_id");
		Disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ID;		
	}
	public static void UpdateInstructor(int ID, String wNumber, String firstName, String lastName, String phone, String email) {
		try {
			Connect(databaseName);
			String sql = "UPDATE instructors" +
					" SET instructor_wNumber = '" + wNumber + "', instructor_firstName = '" + firstName + "', instructor_lastName = '" + lastName + "', instructor_phone = '" + phone + "', instructor_email = '" + email + "'" +
					" WHERE instructor_id = '" + ID + "'";
	    	stmt.executeUpdate(sql);
			conn.commit();
			Disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Something went wrong.");
		}
	}
	public static void DeleteInstructor(int ID) {
		try {
			Connect(databaseName);
			String sql = "DELETE FROM instructors" +
					" WHERE instructor_id = '" + ID +"'";
	    	stmt.executeUpdate(sql);
			conn.commit();
			Disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Something went wrong.");
		}
	}

	public static boolean isFileLoaded() {
		return fileLoaded;
	}
} 