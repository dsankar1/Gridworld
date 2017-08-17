/*
 * Daryan Sankar
 * CS 4242
 * Project 03
 * 04/29/2017
 */
package gridworld;

import java.awt.Color;
import java.awt.Graphics2D;

public class Map {

	public static final int WIDTH = 20, HEIGHT = 20, SQUARE_LENGTH = 40;
	public static final int CLEAR = 0, PIT = -1, GOAL = 1;	
	
 	private static final int[][] MAP = {
			{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1},
			{-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1},
			{-1, 0, 0, 0, 0, 0, 0, 0,-1,-1,-1,-1, 0, 0, 0, 0, 0, 0, 0,-1},
			{-1, 0, 0, 0, 0, 0, 0, 0,-1,-1,-1,-1, 0, 0, 0, 0, 0, 0, 0,-1},
			{-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1},
			{-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1},
			{-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1},
			{-1, 0, 0,-1,-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1,-1, 0, 0,-1},
			{-1, 0, 0,-1,-1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0,-1,-1, 0, 0,-1},
			{-1, 0, 0,-1,-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1,-1, 0, 0,-1},
			{-1, 0, 0,-1,-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1,-1, 0, 0,-1},
			{-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1},
			{-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1},
			{-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1},
			{-1, 0, 0, 0, 0, 0, 0, 0,-1,-1,-1,-1, 0, 0, 0, 0, 0, 0, 0,-1},
			{-1, 0, 0, 0, 0, 0, 0, 0,-1,-1,-1,-1, 0, 0, 0, 0, 0, 0, 0,-1},
			{-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1},
			{-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1},
			{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1}};
	
	public static int get(Position s) {
		return MAP[s.getY()][s.getX()];
	}
	
	public static void render(Graphics2D g2d) {			
		int x = 0, y = 0;
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				if (MAP[i][j] == CLEAR) {
					g2d.setColor(Color.LIGHT_GRAY);
				}
				else if (MAP[i][j] == PIT) {
					g2d.setColor(Color.DARK_GRAY);
					g2d.fillRect(x, y, SQUARE_LENGTH, SQUARE_LENGTH);
					g2d.setColor(Color.GRAY);
				}
				else {
					g2d.setColor(Color.LIGHT_GRAY);
					g2d.fillOval(x + 5, y + 5, SQUARE_LENGTH - 10, SQUARE_LENGTH - 10);
				}
				g2d.drawRect(x, y, SQUARE_LENGTH, SQUARE_LENGTH);
				x += SQUARE_LENGTH;
			}
			x = 0;
			y += SQUARE_LENGTH;
		}
	}
		
}

