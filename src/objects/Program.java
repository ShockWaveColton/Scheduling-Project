package objects;

import javax.swing.JOptionPane;

import engine.ObjectManager;

public class Program {
	private String name;

	public Program(String name) {
		this.name = name;
	}

	public static void Add() {
		String name = JOptionPane.showInputDialog("What is the program name?\nBe sure to include the term");
		Program program = new Program(name);
		ObjectManager.addProgramToList(program);
	}
	public String getName() {
		return name;
	}
}
