package org.archery.simulation.model;

import lombok.Getter;

// El enum Shot representa los diferentes tipos de disparos que un arquero puede realizar.
// Cada tipo de disparo está asociado a una puntuación según su precisión.
@Getter
public enum Shot {

	// CENTRAL: Un disparo perfecto que acierta en el centro del objetivo.
	// Otorga la puntuación máxima de 10 puntos.
	CENTRAL(10),

	// INTERMEDIO: Un disparo que cae en la zona intermedia del objetivo.
	// Otorga una puntuación de 9 puntos.
	INTERMEDIATE(9),

	// EXTERIOR: Un disparo que cae en la zona exterior del objetivo.
	// Otorga una puntuación de 8 puntos.
	OUTSIDE(8),

	// ERROR: Representa un disparo fallido o erróneo,
	// lo que resulta en una puntuación de 0 puntos.
	ERROR(0);

	// La puntuación asociada a cada tipo de disparo.
	private final int score;

	// Constructor privado para asignar la puntuación a cada tipo de disparo.
	private Shot(int score) {
		this.score = score; // Inicializa el campo score con el valor proporcionado.
	}
}
