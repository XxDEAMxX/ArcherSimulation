package org.archery.simulation.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the gender of an archer in the archery simulation.
 * It includes information about the accuracy of shots based on gender.
 */
@Getter
@Setter
public class Gender {
    private String name; // The name of the gender (e.g., "Male" or "Female")
    private Map<Shot, Double> precisionMap; // Map associating each type of shot with its corresponding accuracy

    /**
     * Constructor for the Gender class that initializes the accuracy map based on the gender.
     *
     * @param gender Value representing the gender (0 for male, 1 for female).
     */
    public Gender(int gender) {
        precisionMap = new HashMap<>(); // Initializes the precision map
        assignGender(gender); // Assigns the gender and corresponding accuracy
    }

    /**
     * Assigns the name and accuracy statistics based on gender.
     *
     * @param gender Value representing the gender (0 for male, 1 for female).
     */
    public void assignGender(int gender) {
        if (gender == 0) {
            name = "Hombre"; // Sets the name "Male" for male gender
            // Establishes the accuracy for different shot types for the male gender
            precisionMap.put(Shot.CENTRAL, 0.2); // Accuracy for central shot
            precisionMap.put(Shot.INTERMEDIATE, 0.33); // Accuracy for intermediate shot
            precisionMap.put(Shot.OUTSIDE, 0.40); // Accuracy for outside shot
            precisionMap.put(Shot.ERROR, 0.07); // Accuracy for missed shot
        } else {
            name = "Mujer"; // Sets the name "Female" for female gender
            // Establishes the accuracy for different shot types for the female gender
            precisionMap.put(Shot.CENTRAL, 0.3); // Accuracy for central shot
            precisionMap.put(Shot.INTERMEDIATE, 0.38); // Accuracy for intermediate shot
            precisionMap.put(Shot.OUTSIDE, 0.27); // Accuracy for outside shot
            precisionMap.put(Shot.ERROR, 0.05); // Accuracy for missed shot
        }
    }
}
