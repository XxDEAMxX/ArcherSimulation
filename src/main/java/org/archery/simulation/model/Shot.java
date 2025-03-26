package org.archery.simulation.model;

import lombok.Getter;

/**
 * The Shot enum represents the different types of shots that an archer can make.
 * Each shot type is associated with a score based on its accuracy.
 */
@Getter
public enum Shot {

	/**
	 * CENTRAL: A perfect shot that hits the center of the target.
	 * Awards the maximum score of 10 points.
	 */
	CENTRAL(10),

	/**
	 * INTERMEDIATE: A shot that lands in the intermediate zone of the target.
	 * Awards a score of 9 points.
	 */
	INTERMEDIATE(9),

	/**
	 * OUTSIDE: A shot that lands in the outer zone of the target.
	 * Awards a score of 8 points.
	 */
	OUTSIDE(8),

	/**
	 * ERROR: Represents a missed or erroneous shot,
	 * resulting in a score of 0 points.
	 */
	ERROR(0);

	/**
	 * The score associated with the shot type.
	 */
	private final int score;

	/**
	 * Private constructor to assign the score to each shot type.
	 *
	 * @param score the score associated with the shot type.
	 */
	private Shot(int score) {
		this.score = score; // Initializes the score field with the provided score value.
	}
}
