package org.archery.simulation.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an archery match between two teams.
 * This class manages match rounds, shooting actions, and winner determination.
 */
@Getter
@Setter
public class Match {

	private final Team[] teams; // Array containing the two teams participating in the match
	private int rounds; // Total number of rounds in the match
	private Gender winnerGender; // Gender of the winning archer

	/**
	 * Constructs a Match instance with the specified teams and initializes the number of rounds to one.
	 *
	 * @param team1 First team competing in the match
	 * @param team2 Second team competing in the match
	 */
	public Match(Team team1, Team team2) {
		this.teams = new Team[]{team1, team2}; // Initialize the array with the two teams
		this.rounds = 1; // Start the match with one round
	}

	/**
	 * Executes a shooting action for each archer in the luckiest team of the current round.
	 */
	public void giveRandomShot() {
		for (Team team : teams) {
			team.giveLaunchToLuckiestArcher(rounds); // Allow the luckiest archer to shoot
		}
	}

	/**
	 * Determines the winning archer for the current round based on their scores.
	 * The winning archer's experience is increased.
	 */
	public void calculateWinnerArcherByRound() {
		Archer winnerRoundArcher = determineTiebreaker(
				teams[0].getHighestScoringArcher(), // Highest scoring archer from team 1
				teams[1].getHighestScoringArcher()  // Highest scoring archer from team 2
		);
		winnerRoundArcher.gainExperience(); // Increase the winning archer's experience
	}

	/**
	 * Resolves a tie between two archers by comparing their scores until a winner is found.
	 *
	 * @param archerA First archer to compare
	 * @param archerB Second archer to compare
	 * @return The archer with the higher score
	 * @throws IllegalArgumentException if either archer is null
	 */
	private Archer determineTiebreaker(Archer archerA, Archer archerB) {
		if (archerA == null || archerB == null) {
			throw new IllegalArgumentException("Archers cannot be null"); // Validate input
		}

		int scoreA = archerA.getRoundScore(); // Get score of the first archer
		int scoreB = archerB.getRoundScore(); // Get score of the second archer

		// Continue shooting until a tiebreaker is resolved
		while (scoreA == scoreB) {
			scoreA = calculateNewScore(archerA); // Calculate a new score for archer A
			scoreB = calculateNewScore(archerB); // Calculate a new score for archer B
		}

		Archer winner = scoreA > scoreB ? archerA : archerB; // Determine the winning archer
		winner.increaseRoundsWon(); // Increment the rounds won by the winner
		return winner; // Return the winning archer
	}

	/**
	 * Calculates a new score for the given archer based on their shooting performance.
	 *
	 * @param archer The archer whose score will be calculated
	 * @return The newly calculated score
	 */
	private int calculateNewScore(Archer archer) {
		return archer.calculateScoreLaunch(); // Calculate and return the archer's new score
	}

	/**
	 * Determines the overall winner archer based on the number of rounds won.
	 *
	 * @return The archer who has won the most rounds
	 */
	public Archer calculateWinnerArcher() {
		return calculateWinnerArcher(
				teams[0].getMostWinningArcher(), // Highest rounds won by an archer in team 1
				teams[1].getMostWinningArcher()  // Highest rounds won by an archer in team 2
		);
	}

	/**
	 * Obtains the most experienced archer from both teams.
	 *
	 * @return The archer with the most experience
	 */
	public Archer getMostExperiencedArcher() {
		return calculateWinnerArcher(
				teams[0].obtainMostExperiencedArcher(), // Most experienced archer in team 1
				teams[1].obtainMostExperiencedArcher()  // Most experienced archer in team 2
		);
	}

	/**
	 * Determines the winner between two archers based on the number of rounds won.
	 *
	 * @param a First archer for comparison
	 * @param b Second archer for comparison
	 * @return The archer with the most rounds won
	 */
	private Archer calculateWinnerArcher(Archer a, Archer b) {
		return a.getRoundsWon() > b.getRoundsWon() ? a : b; // Return the archer with the most rounds won
	}

	/**
	 * Calculates the winning team for the current round based on scores.
	 * Updates the total scores for both teams.
	 */
	public void calculateWinnerTeamByRound() {
		int scoreTeam1 = teams[0].obtainRoundScore(); // Get score for team 1
		int scoreTeam2 = teams[1].obtainRoundScore(); // Get score for team 2

		// Update total scores for both teams
		teams[0].setScore(teams[0].getScore() + scoreTeam1);
		teams[1].setScore(teams[1].getScore() + scoreTeam2);

		// Determine the winning team of the round
		Team teamWinner = (scoreTeam1 == scoreTeam2) ? null : (scoreTeam1 > scoreTeam2) ? teams[0] : teams[1];
		if (teamWinner != null) {
			teamWinner.increaseRoundsWon(); // Increment the rounds won by the winning team
		}
	}



	/**
	 * Determines the gender of the overall winning archer and assigns it to winnerGender.
	 */
	public void calculateWinnerByGender() {
		winnerGender = calculateWinnerArcher().getGender(); // Get the gender of the winning archer
	}

	/**
	 * Identifies the luckiest archer between the two teams.
	 *
	 * @return The archer with the highest luck
	 */
	public Archer obtainMostLuckArcher() {
		Archer luckArcherTeam1 = teams[0].obtainLukiestArcher(); // Luckiest archer from team 1
		Archer luckArcherTeam2 = teams[1].obtainLukiestArcher(); // Luckiest archer from team 2
		return compareLuckArchers(luckArcherTeam1, luckArcherTeam2); // Compare and return the luckiest archer
	}

	/**
	 * Compares the luck of two archers and returns the one with the higher luck score.
	 *
	 * @param a First archer for comparison
	 * @param b Second archer for comparison
	 * @return The archer with the greater luck
	 */
	private Archer compareLuckArchers(Archer a, Archer b) {
		int luckA = a.getTotalLuck(); // Luck of the first archer
		int luckB = b.getTotalLuck(); // Luck of the second archer
		return (luckA == luckB) ? a : (luckA > luckB) ? a : b; // Return the archer with greater luck
	}

	/**
	 * Retrieves the score of a specified team based on its position.
	 *
	 * @param position Position of the team (0 for team 1, 1 for team 2)
	 * @return The score of the specified team
	 */
	public int obtainTeamScore(int position) {
		return teams[position].getScore(); // Return the score of the team at the given position
	}

	/**
	 * Grants each team an extra launch opportunity for every three consecutive extra launches.
	 */
	public void giveExtraLaunchByThreeLaunch() {
		for (Team team : teams) {
			team.giveExtraLaunchForThreeConsecutiveExtraLaunch(); // Provide an extra launch to each team
		}
	}

	/**
	 * Increments the total number of rounds in the match.
	 */
	public void increaseRounds() {
		rounds++; // Increase the round counter by one
	}

	/**
	 * Restores the round points for each team in the match.
	 */
	public void resetRoundPoints() {
		for (Team team : teams)
			team.resetRoundPoints(); // Restores the round points for the current team
	}

	/**
	 * Compiles a list of all archers from both teams.
	 *
	 * @return A list containing all archers from the match
	 */
	public List<Archer> obtainAllArchers() {
		List<Archer> archers = new ArrayList<>();
		archers.addAll(teams[0].getArchers()); // Add archers from team 1
		archers.addAll(teams[1].getArchers()); // Add archers from team 2
		return archers; // Return the combined list of archers
	}
}
