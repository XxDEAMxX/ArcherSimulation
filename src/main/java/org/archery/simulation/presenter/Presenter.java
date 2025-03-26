package org.archery.simulation.presenter;

import org.archery.simulation.model.Game;
import org.archery.simulation.view.MainView;

/**
 * The Presenter class acts as an intermediary between the model's simulation logic and the view.
 * It is responsible for initializing the simulation and providing necessary data to the view.
 */
public class Presenter {

	/**
	 * Instance of the game simulation, controlling the behavior and data of the game.
	 */
	private Game simulation;

	/**
	 * Constructor of the Presenter that initializes the simulation and the view with game data.
	 *
	 * @param games The number of games to simulate.
	 */
	public Presenter(int games) {
		// Initializes the simulation with the specified number of games.
		simulation = new Game(games);

		// Creates a new view and provides simulation results to the view.
		new MainView(
				simulation.getLuckyArchers(),        // List of the luckiest archers.
				simulation.getExperiencedArchers(),  // List of the most experienced archers.
				simulation.getWinningTeam(),         // The winning team.
				simulation.getGendersByMatch(),       // Winning gender in each game.
				simulation.getGenderWinStatistics(), // Statistics of wins by gender.
				simulation.getAllArchers()           // List of all archers.
		);
	}
}
