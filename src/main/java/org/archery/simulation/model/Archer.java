package org.archery.simulation.model;

import java.util.Map;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents an archer in an archery simulation.
 * The archer has attributes like id, stamina, experience, luck, and gender.
 * It provides methods to perform shots, calculate scores, and manage stamina.
 */
@Getter
@Setter
public class Archer {

	private static PseudoRandomGenerator rng; // Random number generator for various calculations
	private static Random random; // Random number generator for various calculations
	private int archerId; // Unique identifier for the archer
	private int initialStamina; // Initial stamina level of the archer
	private int experience; // Experience points accumulated by the archer
	private double initialLuck; // Initial luck value of the archer
	private Gender gender; // Gender of the archer, which may affect shot precision

	// Archer's statistics
	private int totalStamina; // Total stamina available for the current round
	private int roundScore; // Score achieved in the current round
	private int totalScore; // Total cumulative score across all rounds
	private int roundsWon; // Total number of rounds won by the archer

	// Statistics related to random shots
	private int randomShotsWon; // Total number of random shots won
	private int consecutiveRandomShotsWon; // Count of consecutive random shots won
	private int totalLuck; // Total accumulated luck throughout the simulation

	/**
	 * Constructor that initializes the archer's attributes.
	 *
	 * @param id Unique identifier for the archer.
	 */
	public Archer(int id) {
		random = new Random();
		rng = new PseudoRandomGenerator();
		this.archerId = id; // Assigns the archer's unique ID
		initialStamina = rng.nextInt(25, 45); // Sets initial stamina between 25 and 45
		experience = 10; // Default experience starts at 10
		initialLuck = generateLuck(); // Generates initial luck value for the archer
		gender = new Gender(random.nextInt(2)); // Randomly assigns a gender (binary)

		// Initializes archer's statistics
		totalStamina = initialStamina;
		roundScore = 0;
		totalScore = 0;
		roundsWon = 0;

		// Initializes random shot statistics
		randomShotsWon = 0;
		consecutiveRandomShotsWon = -1; // Set to -1 to indicate no rounds won yet
		totalLuck = 0;
	}

	/**
	 * Performs shots as long as the archer has stamina left.
	 * Calculates shot scores and updates stamina and luck accordingly.
	 */
	public void executeLaunch() {
		while (this.totalStamina > 0) {
			int launch = calculateScoreLaunch(); // Calculates score for the current shot
			totalScore += launch; // Updates the total score
			roundScore += launch; // Updates the score for the current round
			totalStamina -= 5; // Reduces stamina by 5 after each shot
		}
		regainStaminaEachRound(); // Regenerates stamina at the end of the round
		restartLuck(); // Resets luck after each round
	}

	/**
	 * Calculates the score for a shot based on the archer's precision.
	 *
	 * @return The score obtained from the shot.
	 */
	public int calculateScoreLaunch() {
		Map<Shot, Double> precisionMap = gender.getPrecisionMap(); // Retrieves the precision map based on gender
		double randomLaunch = random.nextDouble(); // Generates a random number between 0 and 1

		// Determines the score based on precision probabilities
		if (randomLaunch <= precisionMap.get(Shot.CENTRAL)) {
			return Shot.CENTRAL.getScore(); // Central target hit
		} else if (randomLaunch <= precisionMap.get(Shot.CENTRAL) + precisionMap.get(Shot.INTERMEDIATE)) {
			return Shot.INTERMEDIATE.getScore(); // Intermediate target hit
		} else if (randomLaunch <= precisionMap.get(Shot.CENTRAL) + precisionMap.get(Shot.INTERMEDIATE) + precisionMap.get(Shot.OUTSIDE)) {
			return Shot.OUTSIDE.getScore(); // Outside target hit
		} else {
			return Shot.ERROR.getScore(); // Missed shot
		}
	}



	/**
	 * Increases the number of random shots won and manages the streak of consecutive wins.
	 *
	 * @param round The current round number.
	 */
	public void increaseRandomShotsWon(int round) {
		if (consecutiveRandomShotsWon == -1) {
			consecutiveRandomShotsWon = round; // Initializes the streak
			randomShotsWon++;
		} else if (consecutiveRandomShotsWon + 1 == round) {
			consecutiveRandomShotsWon = round; // Continues the streak
			randomShotsWon++;
		} else {
			randomShotsWon = 1; // Resets the count of random shots won
			consecutiveRandomShotsWon = round;
		}
	}

	/**
	 * Generates a random luck value between 1 and 3.
	 *
	 * @return The generated luck value.
	 */
	private double generateLuck() {
		return 1 + random.nextFloat() * (3 - 1); // Generates luck between 1 and 3
	}

	/**
	 * Resets the archer's luck to a new randomly generated value.
	 */
	private void restartLuck() {
		initialLuck = generateLuck(); // Generates new luck value
	}

	/**
	 * Increases the total accumulated luck for the archer.
	 */
	public void increaseTotalLuck() {
		totalLuck++; // Increments total accumulated luck
	}

	/**
	 * Regenerates the archer's stamina at the end of each round, factoring in fatigue.
	 */
	private void regainStaminaEachRound() {
		totalStamina = initialStamina - generateFatigue(); // Regenerates stamina with fatigue consideration
		initialStamina = totalStamina; // Updates the initial stamina for the next round
	}

	/**
	 * Generates a random fatigue value between 1 and 2.
	 *
	 * @return The generated fatigue value.
	 */
	public int generateFatigue() {
		return random.nextInt(2) + 1; // Generates fatigue between 1 and 2
	}

	/**
	 * Increases the archer's experience points.
	 */
	public void gainExperience() {
		experience += 3; // Adds 3 points to experience
	}

	/**
	 * Reduces the archer's stamina based on their experience level.
	 */
	public void decreaseStaminaByExperience() {
		this.totalStamina--; // Decreases total stamina by 1
	}

	/**
	 * Resets the score for the current round to zero.
	 */
	public void resetRoundScore() {
		roundScore = 0; // Resets the round score
	}
	/**
	 * Increases the number of rounds won by the archer.
	 */
	public void increaseRoundsWon() {
		roundsWon++; // Increments the count of rounds won
	}

	
	
}

class PseudoRandomGenerator {
	private long x;
	private long w;
	private long s;

	public PseudoRandomGenerator() {
		x = System.nanoTime();
		w = 0;
		s = 0xb5ad4eceda1ce2a9L;
	}

	public int nextInt(int min, int max) {
		return min + (int) (nextLong() % (max - min + 1));
	}

	public double nextDouble() {
		return (nextLong() >>> 11) * 1.1102230246251565E-16;
	}

	private long nextLong() {
		x *= x;
		x += (w += s);
		x = (x >> 32) | (x << 32);
		return x;
	}
}