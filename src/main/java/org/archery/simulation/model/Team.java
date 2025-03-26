package org.archery.simulation.model;

import lombok.Getter;
import lombok.Setter;
import java.util.Comparator;
import java.util.List;

/**
 * The Team class represents a team of archers in the archery match simulation.
 * Each team has a set of archers, a total score, and a count of rounds won.
 */
@Getter
@Setter
public class Team {

	/**
	 * The unique identifier for the team.
	 */
	private int teamId;

	/**
	 * A list of archers that are part of the team.
	 */
	private List<Archer> archers;

	/**
	 * The accumulated score of the team.
	 */
	private int score;

	/**
	 * The number of rounds won by the team.
	 */
	private int roundsWon;

	/**
	 * Constructor that initializes the team with an ID and a list of archers.
	 *
	 * @param archers List of archers that make up the team.
	 * @param id      Unique identifier for the team.
	 */
	public Team(List<Archer> archers, int id) {
		this.teamId = id;
		this.archers = archers;
		this.score = 0;         // Initialize the score to zero at team creation
		this.roundsWon = 0;     // Initialize the rounds won to zero at team creation
	}

	/**
	 * Retrieves the luckiest archer based on their initial luck value.
	 *
	 * @return The luckiest archer or null if there are no archers.
	 */
	private Archer getLuckiestArcher() {
		return archers.stream()
				.max(Comparator.comparingDouble(Archer::getInitialLuck))
				.orElse(null); // Returns the archer with the highest initial luck
	}

	/**
	 * Grants an additional executeLaunch to the luckiest archer and updates the team's score.
	 *
	 * @param round The current round in which the additional executeLaunch is granted.
	 */
	public void giveLaunchToLuckiestArcher(int round) {
		Archer archer = getLuckiestArcher(); // Get the luckiest archer
		if (archer != null) { // Ensure the archer is not null
			score += archer.calculateScoreLaunch(); // Update team score with the archer's executeLaunch score
			archer.increaseRandomShotsWon(round);   // Increase the random shots won by the archer
			archer.increaseTotalLuck();              // Increment the total luck of the archer
		}
	}

	/**
	 * Grants an additional executeLaunch to archers who have won three consecutive launches.
	 */
	public void giveExtraLaunchForThreeConsecutiveExtraLaunch() {
		for (Archer archer : archers) {
			if (archer.getRandomShotsWon() == 3) // Check if the archer has won three consecutive launches
				score += archer.calculateScoreLaunch(); // Update team score with the archer's executeLaunch score
		}
	}

	/**
	 * Retrieves the archer with the highest score in the current round.
	 *
	 * @return The archer with the highest score in the round or null if there are no archers.
	 */
	public Archer getHighestScoringArcher() {
		return archers.stream()
				.max(Comparator.comparingInt(Archer::getRoundScore))
				.orElse(null); // Returns the archer with the highest round score
	}

	/**
	 * Retrieves the archer who has won the most rounds.
	 *
	 * @return The archer with the most rounds won or null if there are no archers.
	 */
	public Archer getMostWinningArcher() {
		return archers.stream()
				.max(Comparator.comparingInt(Archer::getRoundsWon))
				.orElse(null); // Returns the archer with the most rounds won
	}

	/**
	 * Calculates the total score for the current round by summing the scores of all archers.
	 *
	 * @return The total score for the round.
	 */
	public int obtainRoundScore() {
		return archers.stream()
				.mapToInt(Archer::getRoundScore)
				.sum(); // Returns the sum of all archers' round scores
	}



	/**
	 * Retrieves the luckiest archer in the team based on their total accumulated luck.
	 *
	 * @return The archer with the most luck or null if there are no archers.
	 */
	public Archer obtainLukiestArcher() {
		return archers.stream()
				.max(Comparator.comparingInt(Archer::getTotalLuck))
				.orElse(null); // Returns the archer with the highest total luck
	}

	/**
	 * Retrieves the most experienced archer in the team.
	 *
	 * @return The archer with the most experience or null if there are no archers.
	 */
	public Archer obtainMostExperiencedArcher() {
		return archers.stream()
				.max(Comparator.comparingInt(Archer::getExperience))
				.orElse(null); // Returns the archer with the highest experience
	}

	/**
	 * Increases the count of rounds won by the team.
	 */
	public void increaseRoundsWon() {
		roundsWon++; // Increments the rounds won by one
	}




	/**
	 * Resets the round score for all archers in the team.
	 * This is executed after each round to start fresh.
	 */
	public void resetRoundPoints() {
		archers.forEach(Archer::resetRoundScore); // Resets the round score for each archer
	}
}
