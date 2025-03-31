package org.archery.simulation.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Match {
	private final Team[] teams; // Array que contiene los dos equipos que participan en el partido
	private int rounds; // Número total de rondas en el partido
	private Gender winnerGender; // Género del arquero ganador

	// Constructor que inicializa los equipos y la cantidad de rondas en 1
	public Match(Team team1, Team team2) {
		this.teams = new Team[]{team1, team2}; // Inicializa el array con los dos equipos
		this.rounds = 1; // Comienza el partido con una ronda
	}

	// Ejecuta un disparo aleatorio para cada arquero del equipo con más suerte en la ronda actual
	public void giveRandomShot() {
		for (Team team : teams) {
			team.giveLaunchToLuckiestArcher(rounds); // Permite disparar al arquero con más suerte
		}
	}

	// Determina el arquero ganador de la ronda según su puntaje y aumenta su experiencia
	public void calculateWinnerArcherByRound() {
		Archer winnerRoundArcher = determineTiebreaker(
				teams[0].getHighestScoringArcher(), // Arquero con mayor puntaje del equipo 1
				teams[1].getHighestScoringArcher()  // Arquero con mayor puntaje del equipo 2
		);
		winnerRoundArcher.gainExperience(); // Aumenta la experiencia del arquero ganador
	}

	// Resuelve un empate entre dos arqueros comparando sus puntajes hasta que haya un ganador
	private Archer determineTiebreaker(Archer archerA, Archer archerB) {
		if (archerA == null || archerB == null) {
			throw new IllegalArgumentException("Los arqueros no pueden ser nulos"); // Validación de entrada
		}
		int scoreA = archerA.getRoundScore(); // Obtiene el puntaje del arquero A
		int scoreB = archerB.getRoundScore(); // Obtiene el puntaje del arquero B
		// Continua hasta que se resuelva el empate
		while (scoreA == scoreB) {
			scoreA = calculateNewScore(archerA); // Calcula un nuevo puntaje para el arquero A
			scoreB = calculateNewScore(archerB); // Calcula un nuevo puntaje para el arquero B
		}
		Archer winner = scoreA > scoreB ? archerA : archerB; // Determina el arquero ganador
		winner.increaseRoundsWon(); // Incrementa las rondas ganadas por el ganador
		return winner; // Retorna el arquero ganador
	}

	// Calcula un nuevo puntaje para el arquero dado basado en su desempeño
	private int calculateNewScore(Archer archer) {
		return archer.calculateScoreLaunch(); // Calcula y retorna el nuevo puntaje del arquero
	}

	// Determina el arquero ganador del partido según el número de rondas ganadas
	public Archer calculateWinnerArcher() {
		return calculateWinnerArcher(
				teams[0].getMostWinningArcher(), // Arquero con más rondas ganadas del equipo 1
				teams[1].getMostWinningArcher()  // Arquero con más rondas ganadas del equipo 2
		);
	}

	// Obtiene el arquero con más experiencia de ambos equipos
	public Archer getMostExperiencedArcher() {
		return calculateWinnerArcher(
				teams[0].obtainMostExperiencedArcher(), // Arquero más experimentado del equipo 1
				teams[1].obtainMostExperiencedArcher()  // Arquero más experimentado del equipo 2
		);
	}

	// Determina el arquero ganador entre dos según las rondas ganadas
	private Archer calculateWinnerArcher(Archer a, Archer b) {
		return a.getRoundsWon() > b.getRoundsWon() ? a : b; // Retorna el arquero con más rondas ganadas
	}

	// Calcula el equipo ganador de la ronda según los puntajes y actualiza el total
	public void calculateWinnerTeamByRound() {
		int scoreTeam1 = teams[0].obtainRoundScore(); // Puntaje del equipo 1
		int scoreTeam2 = teams[1].obtainRoundScore(); // Puntaje del equipo 2
		// Actualiza el puntaje total de los equipos
		teams[0].setScore(teams[0].getScore() + scoreTeam1);
		teams[1].setScore(teams[1].getScore() + scoreTeam2);
		// Determina el equipo ganador de la ronda
		Team teamWinner = (scoreTeam1 == scoreTeam2) ? null : (scoreTeam1 > scoreTeam2) ? teams[0] : teams[1];
		if (teamWinner != null) {
			teamWinner.increaseRoundsWon(); // Incrementa las rondas ganadas del equipo ganador
		}
	}

	// Determina el género del arquero ganador del partido
	public void calculateWinnerByGender() {
		winnerGender = calculateWinnerArcher().getGender(); // Obtiene el género del arquero ganador
	}

	// Identifica el arquero con mayor suerte entre los dos equipos
	public Archer obtainMostLuckArcher() {
		Archer luckArcherTeam1 = teams[0].obtainLukiestArcher(); // Arquero con más suerte del equipo 1
		Archer luckArcherTeam2 = teams[1].obtainLukiestArcher(); // Arquero con más suerte del equipo 2
		return compareLuckArchers(luckArcherTeam1, luckArcherTeam2); // Compara y retorna el arquero con más suerte
	}

	// Compara la suerte de dos arqueros y retorna el que tenga el puntaje más alto
	private Archer compareLuckArchers(Archer a, Archer b) {
		int luckA = a.getTotalLuck(); // Suerte del arquero A
		int luckB = b.getTotalLuck(); // Suerte del arquero B
		return (luckA == luckB) ? a : (luckA > luckB) ? a : b; // Retorna el arquero con mayor suerte
	}

	// Obtiene el puntaje de un equipo según su posición
	public int obtainTeamScore(int position) {
		return teams[position].getScore(); // Retorna el puntaje del equipo en la posición indicada
	}

	// Otorga un disparo extra a cada equipo por cada tres disparos extra consecutivos
	public void giveExtraLaunchByThreeLaunch() {
		for (Team team : teams) {
			team.giveExtraLaunchForThreeConsecutiveExtraLaunch(); // Proporciona un disparo extra a cada equipo
		}
	}

	// Incrementa el número total de rondas en el partido
	public void increaseRounds() {
		rounds++; // Aumenta el contador de rondas en 1
	}

	// Restablece los puntos de la ronda actual para cada equipo
	public void resetRoundPoints() {
		for (Team team : teams)
			team.resetRoundPoints(); // Restablece los puntos de la ronda para el equipo actual
	}

	// Obtiene una lista con todos los arqueros de ambos equipos
	public List<Archer> obtainAllArchers() {
		List<Archer> archers = new ArrayList<>();
		archers.addAll(teams[0].getArchers()); // Agrega los arqueros del equipo 1
		archers.addAll(teams[1].getArchers()); // Agrega los arqueros del equipo 2
		return archers; // Retorna la lista combinada de arqueros
	}
}
