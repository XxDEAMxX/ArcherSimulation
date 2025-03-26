package org.archery.simulation;

import org.archery.simulation.presenter.Presenter;

/**
 * The Main class is the entry point of the archery simulation application.
 * It is responsible for starting the simulation through the `Presenter`, which manages
 * the simulation logic and presents the results.
 */
public class Main {

    /**
     * The main method of the application, which runs when the program starts.
     * It initializes an instance of the `Presenter` to begin the simulation of 20,000 games.
     */
    public static void main(String[] args) {
        // Starts the simulation of 20,000 games.
        new Presenter(20000);
    }
}
