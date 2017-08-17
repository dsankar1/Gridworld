/*
 * Daryan Sankar
 * CS 4242
 * Project 03
 * 04/29/2017
 */
package gridworld;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Table {

	private double[][][] table = null;
	private int width, height, actions;
	
	public Table(int width, int height, int actions) {
		this.width = width;
		this.height = height;
		this.actions = actions;
		table = new double[height][width][actions];
	}
	
	// returns the number of columns in the table
	public int getWidth() {
		return width;
	}
	
	// returns the the number of rows in the table
	public int getHeight() {
		return height;
	}
	
	// returns the number of different actions
	public int getActions() {
		return actions;
	}
	
	// sets every value in the table to 0
	public void clear() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Arrays.fill(table[i][j], 0);
			}
		}
	}
	
	// returns the value for position s, action a
	public double get(Position s, int a) {
		return table[s.getY()][s.getX()][a];
	}
	
	// sets the value for position s, action a
	public void set(Position s, int a, double value) {
		int x = s.getX();
		int y = s.getY();
		table[y][x][a] = value;
	}
	
	// assigns every element of the table a small random value between 0 and .1
	public static void Randomize(Table Q) {
		Position s = new Position(0, 0);
		
		for (int i = 0; i < Q.getHeight(); i++) {
			for (int j = 0; j < Q.getWidth(); j++) {
				s.setX(j);
				s.setY(i);
				for (int a = 0; a < Q.getActions(); a++) {
					double rand = ThreadLocalRandom.current().nextDouble(.1);
					Q.set(s, a, rand);
				}
			}
		}
	}
	
}
