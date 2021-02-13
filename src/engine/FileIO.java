package engine;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import objects.Classroom; 

public class FileIO {
	private static String databaseName;
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
	public static void New() {
		databaseName = JOptionPane.showInputDialog("What is the name of the new database file?");
		databaseName += ".db";
   	 	File testFile = new File (databaseName);
   	 	if(testFile.exists()) {
   	 		JOptionPane.showMessageDialog(null, "File '" + databaseName + "' already exists. Select another name.");
 		} else {
 			try {
 				Connect(databaseName);
				String sql;
	        	sql = "CREATE TABLE Classrooms " +
	                    "(Wing	  TEXT NOT NULL, " +
	                    " RoomNum TEXT NOT NULL) ";
	        	stmt.executeUpdate(sql);
	    		conn.commit();
	        	sql = "CREATE TABLE Courses " +
	                    "(Name    TEXT NOT NULL," +
	                    " Code    TEXT NOT NULL UNIQUE)";
	        	stmt.executeUpdate(sql); 
	    		conn.commit(); 
	        	sql = "CREATE TABLE Instructors " + 
	                    "(W_Number  TEXT NOT NULL UNIQUE, " +  
	                    " FirstName TEXT NOT NULL, " +  
	                    " LastName  TEXT NOT NULL, " +  
	                    " E_Mail    TEXT NOT NULL UNIQUE)";
	        	stmt.executeUpdate(sql); 
	    		conn.commit(); 
	        	sql = "CREATE TABLE Programs " + 
	                    "(Name	   TEXT NOT NULL UNIQUE, " +  
	                    " Semester TEXT NOT NULL) ";  
	        	stmt.executeUpdate(sql); 
	    		conn.commit(); 
	    		Disconnect();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
 			if(testFile.exists()) {
 	   	 		JOptionPane.showMessageDialog(null, "Database file '" + databaseName + "' created successfully.");
 	   	 		fileLoaded = true;
 			}else {
 				JOptionPane.showMessageDialog(null, "Something went wrong. Try again.");
	    	 }
    	 }
    }
	public static void Load() {
    	final JFileChooser fileChooser = new JFileChooser(".\\");
    	int selectFile = fileChooser.showOpenDialog(null);
    	if (selectFile == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            databaseName = file.getName();
            try {
                Connect(file.toString());
                ResultSet query;
                query = stmt.executeQuery("SELECT * FROM Classrooms ORDER BY Wing, RoomNum");
                while ( query.next()) {
                	String wing = query.getString("Wing"); 
                	String number = query.getString("RoomNum");
                	Classroom.Load(wing, number);
                }
 	   	 		fileLoaded = true;
                System.out.println("Loaded database file " + databaseName + " successfully!");
                Disconnect();
            } catch (Exception e) {
            	e.printStackTrace();
            	JOptionPane.showMessageDialog(null, "Something went wrong.");
			}
        } else {
        	JOptionPane.showMessageDialog(null, "Load file failed. Try again.");
        }
	}
	public static void Add(String table, String field1, String field2) {
		try {
		Connect(databaseName);
		String sql;
    	sql = "INSERT INTO " + table + " (Wing, RoomNum)" +
              " VALUES ('"+ field1 +"', '"+ field2 +"') ";
    	stmt.executeUpdate(sql);
		conn.commit();
		Disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void Update(int selectedIndex, String wing, String roomNum) {
		try {
			Connect(databaseName);
			String sql;
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