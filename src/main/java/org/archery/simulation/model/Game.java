package org.archery.simulation.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// Clase que simula una serie de juegos de tiro con arco entre dos equipos.
public class Game {
	// Lista de partidos simulados
	private final List<Match> matches; 
	// Contador de victorias del equipo masculino
	private int maleTeamVictories; 
	// Contador de victorias del equipo femenino
	private int femaleTeamVictories; 
	// Equipo 1
	private Team team1; 
	// Equipo 2
	private Team team2; 
	// Puntuación total del equipo 1
	private int totalTeam1Score; 
	// Puntuación total del equipo 2
	private int totalTeam2Score; 
	// Constructor para la simulación del juego.
	// @param numberOfGames Número de juegos a simular.
	public Game(int numberOfGames) {
		// Inicializa la lista de partidos
		this.matches = new ArrayList<>(); 
		// Inicializa el contador de victorias del equipo masculino
		this.maleTeamVictories = 0; 
		// Inicializa el contador de victorias del equipo femenino
		this.femaleTeamVictories = 0; 
		// Inicializa la puntuación total del equipo 1
		this.totalTeam1Score = 0; 
		// Inicializa la puntuación total del equipo 2
		this.totalTeam2Score = 0; 
		// Inicia la simulación
		runSimulation(numberOfGames); 
	}
	// Ejecuta la simulación de los juegos.
	// @param numberOfGames Número de juegos a simular.
	private void runSimulation(int numberOfGames) {
		for (int i = 0; i < numberOfGames; i++) {
			initTeams(); // Inicializa los equipos para cada juego
			Match currentMatch = new Match(team1, team2); // Crea un nuevo partido
			matches.add(currentMatch); // Agrega el partido a la lista de partidos
			simulateRounds(currentMatch); // Simula las rondas del partido actual
			currentMatch.calculateWinnerByGender(); // Determina el ganador por género
		}
	}
	// Inicializa los equipos para cada juego.
	private void initTeams() {
		team1 = createTeam(1, createArcherList(1, 5)); // Crea el equipo 1 con arqueros del 1 al 5
		team2 = createTeam(2, createArcherList(6, 10)); // Crea el equipo 2 con arqueros del 6 al 10
	}
	// Crea una lista de identificadores de arqueros a partir del rango especificado.
	// @param start El ID inicial de los arqueros.
	// @param end El ID final de los arqueros.
	// @return Una lista de identificadores de arqueros.
	private List<Integer> createArcherList(int start, int end) {
		return IntStream.rangeClosed(start, end)
				.boxed()
				.collect(Collectors.toList());
	}
	// Crea un equipo de arqueros.
	// @param teamId Identificador del equipo.
	// @param archerIds Lista de identificadores de los arqueros.
	// @return El equipo creado.
	private Team createTeam(int teamId, List<Integer> archerIds) {
		List<Archer> archers = new ArrayList<>(); // Lista para almacenar arqueros
		for (Integer id : archerIds) {
			archers.add(new Archer(id)); // Crea y añade arqueros al equipo
		}
		return new Team(archers, teamId); // Retorna el equipo creado
	}
	// Simula las rondas de un partido.
	// @param match El partido a simular.
	private void simulateRounds(Match match) {
		for (int round = 0; round < 10; round++) { // Simula 10 rondas
			simulateArchersRound(match); // Simula la ronda de los arqueros
			match.giveRandomShot(); // Determina aleatoriamente quién obtiene un disparo extra
			if (round >= 2) {
				match.giveExtraLaunchByThreeLaunch(); // Otorga un lanzamiento extra después de 2 rondas
			}
			match.calculateWinnerArcherByRound(); // Determina el arquero ganador de la ronda
			match.calculateWinnerTeamByRound(); // Determina el equipo ganador de la ronda
			match.increaseRounds(); // Incrementa el contador de rondas
			match.resetRoundPoints(); // Restaura los puntos perdidos en la ronda actual
		}
	}
	// Simula la ronda de los arqueros en un partido.
	// @param match El partido en el que se simulan las rondas.
	private void simulateArchersRound(Match match) {
		for (Team team : match.getTeams()) { // Itera sobre cada equipo
			for (Archer archer : team.getArchers()) { // Itera sobre cada arquero en el equipo
				archer.executeLaunch(); // Simula el disparo del arquero
			}
		}
	}
	// Obtiene las estadísticas de victorias por género.
	// @return El género con más victorias.
	public String getGenderWinStatistics() {
		calculateVictoriesByGender(); // Calcula las victorias por género
		return maleTeamVictories > femaleTeamVictories ? "Hombre" : "Mujer"; // Retorna el género con más victorias
	}
	// Calcula las victorias por género a partir de los juegos simulados.
	private void calculateVictoriesByGender() {
		for (Match match : matches) {
			if ("Hombre".equals(match.getWinnerGender().getName())) {
				maleTeamVictories++; // Incrementa el contador de victorias masculinas
			} else {
				femaleTeamVictories++; // Incrementa el contador de victorias femeninas
			}
		}
	}
	// Obtiene el equipo ganador de la simulación.
	// @return Una cadena con el equipo ganador y su puntaje total.
	public String getWinningTeam() {
		calculateTeamScores(); // Calcula los puntajes totales de los equipos
		Team winningTeam = totalTeam1Score > totalTeam2Score ? team1 : team2; // Determina el equipo ganador
		return String.format("Equipo %d", winningTeam.getTeamId()); // Formatea y retorna la información del equipo ganador
	}
	// Obtiene los puntos del equipo ganador.
	public String getPoints() {
		calculateTeamScores(); // Calcula los puntajes totales de los equipos
		int winningPoints = Math.max(totalTeam1Score, totalTeam2Score); // Obtiene los puntos del equipo ganador
		return String.format(formatPoints(winningPoints)); // Formatea y retorna la información del equipo ganador
	}
	// Calcula los puntajes de los equipos a partir de los partidos simulados.
	private void calculateTeamScores() {
		for (Match match : matches) {
			totalTeam1Score += match.obtainTeamScore(0); // Suma el puntaje del equipo 1
			totalTeam2Score += match.obtainTeamScore(1); // Suma el puntaje del equipo 2
		}
	}
	// Formatea los puntos en una cadena con separadores de miles.
	// @param points Puntos a formatear.
	// @return Representación en cadena de los puntos formateados.
	private String formatPoints(int points) {
		return new DecimalFormat("#,###,###,##0").format(points); // Formatea los puntos con separadores de miles
	}
	// Obtiene la lista de arqueros con más suerte por partido.
	// @return Una cadena con información sobre los arqueros afortunados.
	public String getLuckyArchers() {
		StringBuilder luckyArchers = new StringBuilder(); // StringBuilder para acumular resultados
		for (int i = 0; i < matches.size(); i++) {
			Archer luckyArcher = matches.get(i).obtainMostLuckArcher(); // Obtiene el arquero con más suerte del partido
			luckyArchers.append(String.format("Partido %d : Arquero %d Total de lanzamientos ganados : %d%n",
					i + 1, luckyArcher.getArcherId(), luckyArcher.getTotalLuck())); // Agrega la información del arquero afortunado
		}
		return luckyArchers.toString(); // Retorna la información de los arqueros afortunados
	}
	// Obtiene la lista de arqueros con más experiencia por partido.
	// @return Una cadena con información sobre los arqueros experimentados.
	public String getExperiencedArchers() {
		StringBuilder experiencedArchers = new StringBuilder(); // StringBuilder para acumular resultados
		for (int i = 0; i < matches.size(); i++) {
			Archer experiencedArcher = matches.get(i).getMostExperiencedArcher(); // Obtiene el arquero con más experiencia del partido
			experiencedArchers.append(String.format("Partido %d : Arquero %d Experiencia total adquirida : %d%n",
					i + 1, experiencedArcher.getArcherId(), experiencedArcher.getExperience())); // Agrega la información del arquero experimentado
		}
		return experiencedArchers.toString(); // Retorna la información de los arqueros experimentados
	}
	// Obtiene el género ganador por partido.
	// @return Una cadena con el género ganador de cada partido.
	public String getGendersByMatch() {
		StringBuilder gendersByMatch = new StringBuilder(); // StringBuilder para acumular resultados
		for (int i = 0; i < matches.size(); i++) {
			gendersByMatch.append(String.format("Partido %d : %s%n",
					i + 1, matches.get(i).getWinnerGender().getName())); // Agrega la información del género ganador
		}
		return gendersByMatch.toString(); // Retorna la información de los géneros ganadores
	}
	// Obtiene la lista de todos los arqueros de los juegos simulados.
	// @return Una lista de arqueros.
	public List<Archer> getAllArchers() {
		List<Archer> archers = new ArrayList<>(); // Lista para almacenar todos los arqueros
		for (Match match : matches) {
			archers.addAll(match.obtainAllArchers()); // Agrega todos los arqueros de cada partido
		}
		return archers; // Retorna la lista de todos los arqueros
	}

}
