/*
 * Daryan Sankar
 * CS 4242
 * Project 03
 * 04/29/2017
 */
package gridworld;

public class Main {

	public static void main(String[] args) {
		Agent agent = new Agent();
		Agent.loadQTable(agent, "q-table.txt");
		//Agent.storeArrowGrid(agent, "arrows.txt");
		//agent.train(1000);
		//Agent.storeTable(agent, "q-table2.txt");
		new GUI(agent);
		agent.start();
	}
	
}
