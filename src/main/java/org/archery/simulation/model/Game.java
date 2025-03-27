package org.archery.simulation.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class that simulates a series of archery games between two teams.
 */
public class Game {

	private final List<Match> matches; // List of simulated matches
	private int maleTeamVictories; // Counter for male team victories
	private int femaleTeamVictories; // Counter for female team victories
	private Team team1; // Team 1
	private Team team2; // Team 2
	private int totalTeam1Score; // Total score of team 1
	private int totalTeam2Score; // Total score of team 2

	/**
	 * Constructor for the game simulation.
	 *
	 * @param numberOfGames Number of games to simulate.
	 */
	public Game(int numberOfGames) {
		this.matches = new ArrayList<>(); // Initialize the matches list
		this.maleTeamVictories = 0; // Initialize male victories counter
		this.femaleTeamVictories = 0; // Initialize female victories counter
		this.totalTeam1Score = 0; // Initialize total score for team 1
		this.totalTeam2Score = 0; // Initialize total score for team 2
		runSimulation(numberOfGames); // Start the simulation
	}

	/**
	 * Executes the simulation of the games.
	 *
	 * @param numberOfGames Number of games to simulate.
	 */
	private void runSimulation(int numberOfGames) {
		for (int i = 0; i < numberOfGames; i++) {
			initTeams(); // Initialize teams for each game
			Match currentMatch = new Match(team1, team2); // Create a new match
			matches.add(currentMatch); // Add the match to the list of matches
			simulateRounds(currentMatch); // Simulate the rounds of the current Match
			currentMatch.calculateWinnerByGender(); // Determine the winner by gender
		}
	}

	/**
	 * Initializes the teams for each game.
	 */
	private void initTeams() {
		team1 = createTeam(1, createArcherList(1, 5)); // Create team 1 with archers 1 to 5
		team2 = createTeam(2, createArcherList(6, 10)); // Create team 2 with archers 6 to 10
	}

	/**
	 * Creates a list of archer IDs from the specified range.
	 *
	 * @param start The starting ID of the archers.
	 * @param end The ending ID of the archers.
	 * @return A list of archer IDs.
	 */
	private List<Integer> createArcherList(int start, int end) {
		return IntStream.rangeClosed(start, end)
				.boxed()
				.collect(Collectors.toList());
	}

	/**
	 * Creates a team of archers.
	 *
	 * @param teamId     Identifier for the team.
	 * @param archerIds  List of identifiers for the archers.
	 * @return The created team.
	 */
	private Team createTeam(int teamId, List<Integer> archerIds) {
		List<Archer> archers = new ArrayList<>(); // List to hold archers
		for (Integer id : archerIds) {
			archers.add(new Archer(id)); // Create and add archers to the team
		}
		return new Team(archers, teamId); // Return the created team
	}

	/**
	 * Simulates the rounds of a match.
	 *
	 * @param match The match to simulate.
	 */
	private void simulateRounds(Match match) {
		for (int round = 0; round < 10; round++) { // Simulate 10 rounds
			simulateArchersRound(match); // Simulate the archers' round
			match.giveRandomShot(); // Randomly determine who gets an extra shot
			if (round >= 2) {
				match.giveExtraLaunchByThreeLaunch(); // Give an extra throw after 2 rounds
			}
			match.calculateWinnerArcherByRound(); // Determine the winner of the round based on archers
			match.calculateWinnerTeamByRound(); // Determine the winning team for the round
			match.increaseRounds(); // Increment the round count
			match.resetRoundPoints(); // Regain points lost in the current round
		}
	}

	/**
	 * Simulates the archers' round in a match.
	 *
	 * @param match The match in which the rounds are simulated.
	 */
	private void simulateArchersRound(Match match) {
		for (Team team : match.getTeams()) { // Iterate over each team
			for (Archer archer : team.getArchers()) { // Iterate over each archer in the team
				archer.executeLaunch(); // Simulate the archer's shot
			}
		}
	}

	/**
	 * Obtains the statistics of victories by gender.
	 *
	 * @return The gender with the most victories.
	 */
	public String getGenderWinStatistics() {
		calculateVictoriesByGender(); // Calculate victories by gender
		return maleTeamVictories > femaleTeamVictories ? "Hombre" : "Mujer"; // Return the gender with more victories
	}

	/**
	 * Calculates the victories by gender from the simulated games.
	 */
	private void calculateVictoriesByGender() {
		for (Match match : matches) {
			if ("Male".equals(match.getWinnerGender().getName())) {
				maleTeamVictories++; // Increment male victories counter
			} else {
				femaleTeamVictories++; // Increment female victories counter
			}
		}
	}

	/**
	 * Obtains the winning team of the simulation.
	 *
	 * @return A string with the winning team and their total score.
	 */
	public String getWinningTeam() {
		calculateTeamScores(); // Calculate total scores for teams
		Team winningTeam = totalTeam1Score > totalTeam2Score ? team1 : team2; // Determine the winning team
		int winningPoints = Math.max(totalTeam1Score, totalTeam2Score); // Get the winning points
		return String.format("Equipo %d con %s puntuación", winningTeam.getTeamId(), formatPoints(winningPoints)); // Format and return the winning team info
	}

	/**
	 * Calculates the scores of the teams from the simulated matchs.
	 */
	private void calculateTeamScores() {
		for (Match match : matches) {
			totalTeam1Score += match.obtainTeamScore(0); // Add team 1's score
			totalTeam2Score += match.obtainTeamScore(1); // Add team 2's score
		}
	}

	/**
	 * Formats the points in a string with thousand separators.
	 *
	 * @param points Points to format.
	 * @return The formatted string representation of the points.
	 */
	private String formatPoints(int points) {
		return new DecimalFormat("#,###,###,##0").format(points); // Format points with thousand separators
	}

	/**
	 * Obtains the list of lucky archers by match.
	 *
	 * @return A string with information about the lucky archers.
	 */
	public String getLuckyArchers() {
		StringBuilder luckyArchers = new StringBuilder(); // StringBuilder to accumulate results
		for (int i = 0; i < matches.size(); i++) {
			Archer luckyArcher = matches.get(i).obtainMostLuckArcher(); // Get the lucky archer from the match
			luckyArchers.append(String.format("Match %d : Arquero %d Total ejecutar Lanzamiento ganado : %d%n",
					i + 1, luckyArcher.getArcherId(), luckyArcher.getTotalLuck())); // Append lucky archer info
		}
		return luckyArchers.toString(); // Return the lucky archers info
	}

	/**
	 * Obtains the list of experienced archers by match.
	 *
	 * @return A string with information about the experienced archers.
	 */
	public String getExperiencedArchers() {
		StringBuilder experiencedArchers = new StringBuilder(); // StringBuilder to accumulate results
		for (int i = 0; i < matches.size(); i++) {
			Archer experiencedArcher = matches.get(i).getMostExperiencedArcher(); // Get the experienced archer from the match
			experiencedArchers.append(String.format("Match %d : Arquero %d Experiencia total adquirida : %d%n",
					i + 1, experiencedArcher.getArcherId(), experiencedArcher.getExperience())); // Append experienced archer info
		}
		return experiencedArchers.toString(); // Return the experienced archers info
	}

	/**
	 * Obtains the winning gender by match.
	 *
	 * @return A string with the winning gender of each game.
	 */
	public String getGendersByMatch() {
		StringBuilder gendersByMatch = new StringBuilder(); // StringBuilder to accumulate results
		for (int i = 0; i < matches.size(); i++) {
			gendersByMatch.append(String.format("Match %d : %s%n",
					i + 1, matches.get(i).getWinnerGender().getName())); // Append the winning gender info
		}
		return gendersByMatch.toString(); // Return the winning genders info
	}

	/**
	 * Obtains the list of all archers from the simulated games.
	 *
	 * @return A list of archers.
	 */
	public List<Archer> getAllArchers() {
		List<Archer> archers = new ArrayList<>(); // List to hold all archers
		for (Match match : matches) {
			archers.addAll(match.obtainAllArchers()); // Add all archers from each match
		}
		return archers; // Return the list of all archers
	}
}
