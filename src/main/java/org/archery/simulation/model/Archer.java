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

	private static LinearCongruentialGenerator rng; // Generador de números pseudoaleatorios para varios cálculos
	private static Random random; // Generador de números aleatorios para varios cálculos
	private int archerId; // Identificador único del arquero
	private int initialResistance; // Nivel inicial de resistencia del arquero
	private int experience; // Puntos de experiencia acumulados por el arquero
	private double initialLuck; // Valor inicial de suerte del arquero
	private Gender gender; // Género del arquero, que puede afectar la precisión de los disparos
	
	// Estadísticas del arquero
	private int totalResistance; // Resistencia total disponible para la ronda actual
	private int roundScore; // Puntuación obtenida en la ronda actual
	private int totalScore; // Puntuación acumulada total a lo largo de todas las rondas
	private int roundsWon; // Número total de rondas ganadas por el arquero
	
	// Estadísticas relacionadas con los disparos aleatorios
	private int randomShotsWon; // Número total de disparos aleatorios ganados
	private int consecutiveRandomShotsWon; // Conteo de disparos aleatorios ganados de forma consecutiva
	private int totalLuck; // Suerte total acumulada a lo largo de la simulación
	

	/**
	 * Constructor that initializes the archer's attributes.
	 *
	 * @param id Unique identifier for the archer.
	 */
	public Archer(int id) {
		random = new Random();
		rng = new LinearCongruentialGenerator();
		this.archerId = id; // Asigna el ID único del arquero
		initialResistance = rng.nextInt(25, 45); // Establece la resistencia inicial entre 25 y 45
		experience = 10; // La experiencia por defecto comienza en 10
		initialLuck = generateLuck(); // Genera el valor inicial de suerte para el arquero
		gender = new Gender(rng.nextInt(0, 2)); // Asigna aleatoriamente un género (binario)

		// Inicializa las estadísticas del arquero
		totalResistance = initialResistance;
		roundScore = 0;
		totalScore = 0;
		roundsWon = 0;

		// Inicializa las estadísticas de disparos aleatorios
		randomShotsWon = 0;
		consecutiveRandomShotsWon = -1; // Se establece en -1 para indicar que aún no ha ganado rondas
		totalLuck = 0;
	}


	/**
 * Realiza lanzamientos mientras el arquero tenga resistencia disponible.
 * Calcula las puntuaciones de los lanzamientos y actualiza la resistencia y la suerte en consecuencia.
 */
	public void executeLaunch() {
		while (this.totalResistance > 0) {
			int launch = calculateScoreLaunch(); // Calcula la puntuación del lanzamiento actual
			totalScore += launch; // Actualiza la puntuación total
			roundScore += launch; // Actualiza la puntuación de la ronda actual
			totalResistance -= 5; // Reduce la resistencia en 5 después de cada lanzamiento
		}
		regainResistanceEachRound(); // Regenera la resistencia al final de la ronda
		restartLuck(); // Restablece la suerte después de cada ronda
	}

	
	 // Calcula la puntuación de un lanzamiento basado en la precisión del arquero.
	 // @return La puntuación obtenida en el lanzamiento.
	public int calculateScoreLaunch() {
		Map<Shot, Double> precisionMap = gender.getPrecisionMap(); // Obtiene el mapa de precisión según el género
		double randomLaunch = rng.nextDouble(); // Genera un número aleatorio entre 0 y 1

		// Determina la puntuación basada en las probabilidades de precisión
		if (randomLaunch <= precisionMap.get(Shot.CENTRAL)) {
			return Shot.CENTRAL.getScore(); // Impacto en el blanco central
		} else if (randomLaunch <= precisionMap.get(Shot.CENTRAL) + precisionMap.get(Shot.INTERMEDIATE)) {
			return Shot.INTERMEDIATE.getScore(); // Impacto en el blanco intermedio
		} else if (randomLaunch <= precisionMap.get(Shot.CENTRAL) + precisionMap.get(Shot.INTERMEDIATE) + precisionMap.get(Shot.OUTSIDE)) {
			return Shot.OUTSIDE.getScore(); // Impacto en el blanco exterior
		} else {
			return Shot.ERROR.getScore(); // Disparo fallido
		}
	}

	// Aumenta el número de disparos aleatorios ganados y gestiona la racha de victorias consecutivas.
    public void increaseRandomShotsWon(int round) {
        if (consecutiveRandomShotsWon == -1) {
            consecutiveRandomShotsWon = round; // Inicializa la racha
            randomShotsWon++;
        } else if (consecutiveRandomShotsWon + 1 == round) {
            consecutiveRandomShotsWon = round; // Continúa la racha
            randomShotsWon++;
        } else {
            randomShotsWon = 1; // Restablece el conteo de disparos aleatorios ganados
            consecutiveRandomShotsWon = round;
        }
    }

    // Genera un valor de suerte aleatorio entre 1 y 3.
    private double generateLuck() {
        return 1 + random.nextFloat() * (3 - 1); // Genera suerte entre 1 y 3
    }

    // Restablece la suerte del arquero a un nuevo valor generado aleatoriamente.
    private void restartLuck() {
        initialLuck = generateLuck(); // Genera un nuevo valor de suerte
    }

    // Aumenta la suerte total acumulada del arquero.
    public void increaseTotalLuck() {
        totalLuck++; // Incrementa la suerte acumulada total
    }

    // Regenera la resistencia del arquero al final de cada ronda, considerando la fatiga.
    private void regainResistanceEachRound() {
        totalResistance = initialResistance - generateFatigue(); // Regenera la resistencia considerando la fatiga
        initialResistance = totalResistance; // Actualiza la resistencia inicial para la siguiente ronda
    }

    // Genera un valor de fatiga aleatorio entre 1 y 2.
    public int generateFatigue() {
        return random.nextInt(2) + 1; // Genera fatiga entre 1 y 2
    }

    // Aumenta los puntos de experiencia del arquero.
    public void gainExperience() {
        experience += 3; // Suma 3 puntos a la experiencia
    }

    // Reduce la resistencia del arquero en función de su nivel de experiencia.
    public void decreaseStaminaByExperience() {
        this.totalResistance--; // Reduce la resistencia total en 1
    }

    // Restablece la puntuación de la ronda actual a cero.
    public void resetRoundScore() {
        roundScore = 0; // Restablece la puntuación de la ronda
    }

    // Aumenta el número de rondas ganadas por el arquero.
    public void increaseRoundsWon() {
        roundsWon++; // Incrementa el número de rondas ganadas
    }
}
