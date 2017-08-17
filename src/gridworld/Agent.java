/*
 * Daryan Sankar
 * CS 4242
 * Project 03
 * 04/29/2017
 */
package gridworld;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Agent extends Thread {

	public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3, ACTIONS = 4;
	private double epsilon = .9, decay = .00000001, alpha = .01, gamma = .9, lambda = .9;
	private boolean running = false;
	private Table Q, e;
	private Position s;
	private int a;
	
	public Agent() {
		Q = new Table(Map.HEIGHT, Map.WIDTH, ACTIONS);
		Table.Randomize(Q);
		e = new Table(Map.HEIGHT, Map.WIDTH, ACTIONS);
		initializeSA(new Position(0, 0));
		
		running = true;
	}
	
	// trains the agent until epsilon is equal to or less than .10
	// **80 million iterations with initial epsilon .9 and decay of .00000001
	public void train() {
		System.out.println("Beginning training process...");
		int count = 0;
		while (epsilon > .1) {
			initializeSA();
			while (!isTerminal(s)) {
				SARSA();
			}
			epsilon -= decay;
			count++;
			e.clear();
			System.out.println("Episode Count: " + count);
		}
	}
	
	// trains agent for number of episodes provided
	public void train(int episodes) {
		System.out.println("Beginning training process...");
		int count = 0;
		while (count < episodes) {
			initializeSA();
			while (!isTerminal(s)) {
				SARSA();
			}
			epsilon -= decay;
			count++;
			e.clear();
			System.out.println("Episode Count: " + count);
		}
	}
	
	// SARSA-lambda algorithm core
	private void SARSA() {
		Position sPrime = nextState(s, a);// take action a
		int reward = Map.get(sPrime);// observe reward of s'
		int aPrime = getAction(sPrime, epsilon);// choose actions a'
		
		double Qsa = Q.get(s, a);
		double QsaPrime = Q.get(sPrime, aPrime);
		double omega = reward + (gamma * QsaPrime) - Qsa;// calculate Omega
		
		double eValue = e.get(s, a) + 1L;
		e.set(s, a, eValue);// add one to e(s, a)
		
		// update all Q(s, a) and e(s, a)
		updateTables(omega);
		
		// set s to s' and a to a'
		s = sPrime;
		a = aPrime;
	}
	
	public void run() {
		epsilon = 0.0;
		initializeSA(new Position(0, 0));
		
		while (running) {
			while (!isTerminal(s)) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				SARSA();
				if (Map.get(s) == Map.PIT) {
					System.out.println("I've fallen and I can't get up.");
				}
				else if (Map.get(s) == Map.GOAL) {
					System.out.println("Goal found.");
				}
				e.clear();
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	// randomly initializes the s and a global variables
	public void initializeSA() {
		Position rand = new Position(0, 0); 
		while (Map.get(rand) != Map.CLEAR) {
			int x = ThreadLocalRandom.current().nextInt(0, Map.WIDTH);
			int y = ThreadLocalRandom.current().nextInt(0, Map.HEIGHT);
			rand.setX(x);
			rand.setY(y);
		}
		s = rand;
		a = getAction(s, epsilon);
	}
	
	// sets global variable s to the position provided and generates action
	public void initializeSA(Position s) {
		this.s = s;
		a = getAction(s, epsilon);
	}
	
	// returns the Q-table
	public Table getQTable() {
		return Q;
	}
	
	// sets the Q-table
	public void setQTable(Table Q) {
		this.Q = Q;
	}
	
	public void render(Graphics2D g2d) {		
		g2d.setColor(Color.BLACK);
		g2d.fillOval((s.getX() * Map.SQUARE_LENGTH) + 5, (s.getY() * Map.SQUARE_LENGTH) + 5, 
				Map.SQUARE_LENGTH - 10, Map.SQUARE_LENGTH - 10);
	}
	
	// returns true if agent is in terminal state, false if not
	private boolean isTerminal(Position s) {
		return !(Map.get(s) == Map.CLEAR);
	}
	
	// returns next state given a current state and action
	private Position nextState(Position s, int a) {
		int x = s.getX();
		int y = s.getY();
		
		switch(a) {
		case UP: y--;
			break;
		case DOWN: y++;
			break;
		case LEFT: x--;
			break;
		case RIGHT: x++;
			break;
		default: System.out.println("Error occurred, shutting down.");
			System.exit(0);
			break;
		}
		Position next = new Position(x, y);
		return next;
	}
	
	// returns and integer representing an action which is either decided randomly or greedily
	private int getAction(Position s, double epsilon) {
		double rand = ThreadLocalRandom.current().nextDouble(1);
		ArrayList<Integer> options = getActionOptions(s);
		int index = 0;
		
		if (rand < epsilon) {
			index = ThreadLocalRandom.current().nextInt(0, options.size());
		}
		else {
			double value = Integer.MIN_VALUE;
			for (int i = 0; i < options.size(); i++) {
				if (Q.get(s, options.get(i)) > value) {
					value = Q.get(s, options.get(i));
					index = i;
				}
			}
		}
		return options.get(index);
	}
	
	// returns an ArrayList of movement options
	private ArrayList<Integer> getActionOptions(Position s) {
		ArrayList<Integer> options = new ArrayList<Integer>();
		
		if (s.getY() > 0) {
			options.add(UP);
		}
		if (s.getY() < (Map.HEIGHT - 1)) {
			options.add(DOWN);
		}
		if (s.getX() > 0) {
			options.add(LEFT);
		}
		if (s.getX() < (Map.WIDTH - 1)) {
			options.add(RIGHT);
		}
		return options;
	}
	
	// updates all elements of both the Q table and the e table.
	private void updateTables(double omega) {
		Position s = new Position(0, 0);
		
		for (int i = 0; i < Q.getHeight(); i++) {
			for (int j = 0; j < Q.getWidth(); j++) {
				s.setX(j);
				s.setY(i);
				for (int a = 0; a < Q.getActions(); a++) {
					double Qsa = Q.get(s, a);
					double esa = e.get(s, a);
					
					double qValue = Qsa + (alpha * omega * esa);
					Q.set(s, a, qValue);
					
					double eValue = (gamma * lambda * esa);
					e.set(s, a, eValue);
				}
			}
		}
	}
	
	// Stores the given Q-table into a text file with given filename
	public static void storeQTable(Agent agent, String filename) {
		Table Q = agent.getQTable();
		try {
			PrintWriter writer = new PrintWriter(filename);
			Position s = new Position(0, 0);
			for (int i = 0; i < Q.getHeight(); i++) {
				for (int j = 0; j < Q.getWidth(); j++) {
					s.setX(j);
					s.setY(i);
					for (int a = 0; a < Q.getActions(); a++) {
						double value = Q.get(s, a);
						String strValue = Double.toString(value);
						strValue = strValue + " ";
						writer.print(strValue);
					}
					writer.println();
				}
			}
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println("File wasn't found.");
			System.exit(0);
		}
	}
	
	// loads table from a text file
	// **Throws error if sizes of text file and Q-table don't match
	public static void loadQTable(Agent agent, String filename) {
		Table Q = agent.getQTable();
		ArrayList<String> lines = Agent.ReadFile(filename);
		ArrayList<ArrayList<Double>> states = new ArrayList<ArrayList<Double>>();
		
		for (String line : lines) {
			ArrayList<Double> actions = Agent.ExtractDoubles(line);
			states.add(actions);
		}
		
		int x = 0, y = 0;
		for (int i = 0; i < states.size(); i++) {
			Position s = new Position(x, y);
			int a = 0;
			for (Double value : states.get(i)) {
				Q.set(s, a, value);
				a++;
			}
			
			x++;
			if (x >= Q.getWidth()) {
				x = 0;
				y++;
			}
		}
	}
	
	public static void storeArrowGrid(Agent agent, String filename) {
		Table Q = agent.getQTable();
		try {
			PrintWriter writer = new PrintWriter(filename);
			for (int i = 0; i < Q.getHeight(); i++) {
				for (int j = 0; j < Q.getWidth(); j++) {
					Position s = new Position(j, i);
					if (Map.get(s) == Map.CLEAR) {
						int action = agent.getAction(s, 0.0);
						switch(action) {
						case Agent.UP: writer.print("^ ");
							break;
						case Agent.DOWN: writer.print("v ");
							break;
						case Agent.LEFT: writer.print("< ");
							break;
						case Agent.RIGHT: writer.print("> ");
							break;
						default: System.out.println("Error occured...");
								System.exit(0);
							break;
						}
					}
					else if (Map.get(s) == Map.GOAL) {
						writer.print("O ");
					}
					else {
						writer.print("X ");
					}
				}
				writer.println();
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static ArrayList<String> ReadFile(String filename) {
		ArrayList<String> lines = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/" + filename))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	line = line.trim();
		    	lines.add(line);
		    }
		}
		catch(Exception e) {
			System.out.println("File wasn't found.");
		}
		return lines;
	}
	
	private static ArrayList<Double> ExtractDoubles(String line) {
		ArrayList<Double> data = new ArrayList<Double>();
		line = line + " ";
		
		int start = 0;
		int end = line.indexOf(" ", start);
		while (end > start) {
			String subString = line.substring(start, end);
			data.add(Double.parseDouble(subString));
			start = end + 1;
			end = line.indexOf(" ", start);
		}
		return data;
	}
	
}
