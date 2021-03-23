package engine;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import objects.Classroom;
import objects.Course;
import objects.Instructor;
import objects.Program;

// The purpose of this class is to convert all of the JCombobox entries to human-readable format,
// While still allowing the JCombobox to contain the entire object, and not simply a string representation.
public class ListCellRendererOverride extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		// Do not load any values if the ComboBox is empty:
		if (value == null)
			return this;
		else {
	        if (value instanceof Program)
				value = ((Program)value).getName();
	        else if (value instanceof Instructor)
	        	value = ((Instructor)value).getFullName();
	        else if (value instanceof Classroom)
	        	value = ((Classroom)value).getName();
	        else if (value instanceof Course)
	        	value = ((Course)value).getFullName();	        
	        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	        return this;
        }			
    }
}
