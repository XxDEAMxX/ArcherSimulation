package org.archery.simulation.model;

import java.util.Comparator;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * The Team class represents a team of archers in the archery match simulation.
 * Each team has a set of archers, a total score, and a count of rounds won.
 */
@Getter
@Setter
public class Team {
//   Identificador único del equipo.
	private int teamId;
// Lista de arqueros que forman parte del equipo.
	private List<Archer> archers;
// Puntuación acumulada del equipo.
	private int score;
// Número de rondas ganadas por el equipo.
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

	// Obtiene el arquero con mayor suerte basado en su valor de suerte inicial.
    // @return El arquero con más suerte o null si no hay arqueros.
    private Archer getLuckiestArcher() {
        return archers.stream()
                .max(Comparator.comparingDouble(Archer::getInitialLuck))
                .orElse(null); // Devuelve el arquero con la mayor suerte inicial
    }
	// Concede un lanzamiento adicional al arquero con más suerte y actualiza la puntuación del equipo.
    // @param round La ronda actual en la que se otorga el lanzamiento adicional.
    public void giveLaunchToLuckiestArcher(int round) {
        Archer archer = getLuckiestArcher(); // Obtiene el arquero con más suerte
        if (archer != null) { // Asegura que el arquero no sea nulo
            score += archer.calculateScoreLaunch(); // Actualiza la puntuación del equipo con el puntaje del lanzamiento
            archer.increaseRandomShotsWon(round); // Aumenta la cantidad de lanzamientos aleatorios ganados por el arquero
            archer.increaseTotalLuck(); // Incrementa la suerte total del arquero
        }
    }
    // Concede un lanzamiento adicional a los arqueros que han ganado tres lanzamientos consecutivos.
    public void giveExtraLaunchForThreeConsecutiveExtraLaunch() {
        for (Archer archer : archers) {
            if (archer.getRandomShotsWon() == 3) // Verifica si el arquero ha ganado tres lanzamientos consecutivos
                score += archer.calculateScoreLaunch(); // Actualiza la puntuación del equipo con el puntaje del lanzamiento
        }
    }
    // Obtiene el arquero con la puntuación más alta en la ronda actual.
    // @return El arquero con la puntuación más alta en la ronda o null si no hay arqueros.
    public Archer getHighestScoringArcher() {
        return archers.stream()
                .max(Comparator.comparingInt(Archer::getRoundScore))
                .orElse(null); // Devuelve el arquero con la puntuación más alta en la ronda
    }
    // Obtiene el arquero que ha ganado más rondas.
    // @return El arquero con más rondas ganadas o null si no hay arqueros.
    public Archer getMostWinningArcher() {
        return archers.stream()
                .max(Comparator.comparingInt(Archer::getRoundsWon))
                .orElse(null); // Devuelve el arquero con más rondas ganadas
    }
    // Calcula la puntuación total de la ronda actual sumando las puntuaciones de todos los arqueros.
    // @return La puntuación total de la ronda.
    public int obtainRoundScore() {
        return archers.stream()
                .mapToInt(Archer::getRoundScore)
                .sum(); // Devuelve la suma de las puntuaciones de los arqueros en la ronda
    }
    // Obtiene el arquero con más suerte en el equipo según su suerte acumulada total.
    // @return El arquero con más suerte o null si no hay arqueros.
    public Archer obtainLukiestArcher() {
        return archers.stream()
                .max(Comparator.comparingInt(Archer::getTotalLuck))
                .orElse(null); // Devuelve el arquero con la mayor suerte acumulada
    }
    // Obtiene el arquero con más experiencia en el equipo.
    // @return El arquero con más experiencia o null si no hay arqueros.
    public Archer obtainMostExperiencedArcher() {
        return archers.stream()
                .max(Comparator.comparingInt(Archer::getExperience))
                .orElse(null); // Devuelve el arquero con mayor experiencia
    }
    // Incrementa la cantidad de rondas ganadas por el equipo.
    public void increaseRoundsWon() {
        roundsWon++; // Incrementa en uno las rondas ganadas
    }
    // Restablece la puntuación de la ronda para todos los arqueros del equipo.
    // Esto se ejecuta después de cada ronda para comenzar desde cero.
    public void resetRoundPoints() {
        archers.forEach(Archer::resetRoundScore); // Restablece la puntuación de la ronda de cada arquero
    }

}
