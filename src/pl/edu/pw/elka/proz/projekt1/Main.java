package pl.edu.pw.elka.proz.projekt1;


import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Optional;

/**
 * Small class implementing the login window
 * 
 * @author Michał Sokólski
 * @version 1.0.0
 */
public class Main extends Application {
	
	/**
	 * Main function of class
	 * 
	 * @param primaryStage
	 *            Standard JavaFX stage
	 */
	@Override
	public void start(Stage primaryStage) {

		Optional<Pair<Environment, String>> result = (new LogonDialog("Logowanie", "Logowanie do systemu STYLEman"))
				.showAndWait();

		if(result.isPresent()){
			System.out.println("Environment=" + result.get().getKey() + ", Username=" + result.get().getValue());
		};

	}
	
	public static void main(String[] args) {
		launch(args);
	}

}