package org.archery.simulation.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
// Representa el género de un arquero en la simulación de arquería.
// Incluye información sobre la precisión de los disparos según el género.
@Getter
@Setter
public class Gender {
    private String name; // Nombre del género (por ejemplo, "Hombre" o "Mujer")
    private Map<Shot, Double> precisionMap; // Mapa que asocia cada tipo de disparo con su precisión correspondiente
    // Constructor de la clase Gender que inicializa el mapa de precisión según el género.
    // @param gender Valor que representa el género (0 para hombre, 1 para mujer).
    public Gender(int gender) {
        precisionMap = new HashMap<>(); // Inicializa el mapa de precisión
        assignGender(gender); // Asigna el género y su precisión correspondiente
    }
    // Asigna el nombre y las estadísticas de precisión en función del género.
    // @param gender Valor que representa el género (0 para hombre, 1 para mujer).
    public void assignGender(int gender) {
        if (gender == 0) {
            name = "Hombre"; // Establece el nombre "Hombre" para el género masculino
            // Define la precisión para los diferentes tipos de disparo en hombres
            precisionMap.put(Shot.CENTRAL, 0.2); // Precisión para disparo central
            precisionMap.put(Shot.INTERMEDIATE, 0.33); // Precisión para disparo intermedio
            precisionMap.put(Shot.OUTSIDE, 0.40); // Precisión para disparo exterior
            precisionMap.put(Shot.ERROR, 0.07); // Precisión para disparo fallido
        } else {
            name = "Mujer"; // Establece el nombre "Mujer" para el género femenino
            // Define la precisión para los diferentes tipos de disparo en mujeres
            precisionMap.put(Shot.CENTRAL, 0.3); // Precisión para disparo central
            precisionMap.put(Shot.INTERMEDIATE, 0.38); // Precisión para disparo intermedio
            precisionMap.put(Shot.OUTSIDE, 0.27); // Precisión para disparo exterior
            precisionMap.put(Shot.ERROR, 0.05); // Precisión para disparo fallido
        }
    }
}
