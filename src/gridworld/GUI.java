/*
 * Daryan Sankar
 * CS 4242
 * Project 03
 * 04/29/2017
 */
package gridworld;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI extends JPanel implements MouseListener {

	private static final long serialVersionUID = 1L;
	private Rectangle[][] rects = null;
	private Rectangle cursor = null;
	private Agent agent = null;
	
	public GUI(Agent agent) {
		this.agent = agent;
		cursor = new Rectangle(-1, -1, 1, 1);
		rects = new Rectangle[Map.HEIGHT][Map.WIDTH];
		
		int x = 0, y = 0;
		for (int i = 0; i < Map.HEIGHT; i++) {
			for (int j = 0; j < Map.WIDTH; j++) {
				rects[i][j] = new Rectangle(x, y, Map.SQUARE_LENGTH, Map.SQUARE_LENGTH);
				x += Map.SQUARE_LENGTH;
			}
			x = 0;
			y += Map.SQUARE_LENGTH;
		}
		setBackground(Color.GRAY);
		createWindow();
	}
	
	private void createWindow() {
		int width = Map.SQUARE_LENGTH * Map.WIDTH + 6;
		int height = Map.SQUARE_LENGTH * Map.HEIGHT + 40;
		
		JFrame frame = new JFrame("Grid World");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.add(this);
		frame.addMouseListener(this);
		frame.setVisible(true);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint
		(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		Map.render(g2d);
		agent.render(g2d);
		
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		cursor.x = e.getX() - 2;
		cursor.y = e.getY() - 38;
		for (int i = 0; i < rects.length; i++) {
			for (int j = 0; j < rects[i].length; j++) {
				Position s = new Position(j, i);
				if (cursor.intersects(rects[i][j]) && Map.get(s) == Map.CLEAR) {
					agent.initializeSA(s);
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		cursor.x = e.getX() - 2;
		cursor.y = e.getY() - 38;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		cursor.x = e.getX() - 2;
		cursor.y = e.getY() - 38;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		cursor.x = e.getX() - 2;
		cursor.y = e.getY() - 38;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		cursor.x = -1;
		cursor.y = -1;
	}
	
}
