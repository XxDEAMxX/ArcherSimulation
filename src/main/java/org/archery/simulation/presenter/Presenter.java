package org.archery.simulation.presenter;

import org.archery.simulation.model.Game;
import org.archery.simulation.view.MainView;

// La clase Presenter actúa como un intermediario entre la lógica de simulación del modelo y la vista.
// Es responsable de inicializar la simulación y proporcionar los datos necesarios a la vista.
public class Presenter {

	// Instancia de la simulación del juego, que controla el comportamiento y los datos del juego.
	private Game simulation;

	// Constructor de la clase Presenter que inicializa la simulación y la vista con los datos del juego.
	//
	// @param games El número de juegos a simular.
	public Presenter(int games) {
		// Inicializa la simulación con el número de juegos especificado.
		simulation = new Game(games);

		// Crea una nueva vista y proporciona los resultados de la simulación a la vista.
		new MainView(
				simulation.getLuckyArchers(),        // Lista de los arqueros más afortunados.
				simulation.getExperiencedArchers(),  // Lista de los arqueros más experimentados.
				simulation.getWinningTeam(),         // El equipo ganador.
				simulation.getGendersByMatch(),      // Género ganador en cada juego.
				simulation.getGenderWinStatistics(), // Estadísticas de victorias por género.
				simulation.getAllArchers(),          // Lista de todos los arqueros.
				simulation.getPoints()               // Puntos obtenidos.
		);
	}
}
