package LoginWindow;

import javafx.application.*;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.stage.*;
import javafx.util.Pair;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Main extends Application {
	
	private Dialog<Pair<String, String>> dialog;
	private ChoiceBox<String> choiceBoxEnvironment;
	private ComboBox<String> comboBoxUser;
	
	private Map<String, String> productionMap;
	private Map<String, String> testingMap;
	private Map<String, String> developmentMap;
	private Map<String, Map<String, String>> envMap;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void init() {
		
		productionMap = new HashMap<String, String>();
		productionMap.put("Adam.Kowalski", "password11");
		productionMap.put("Piotr.Nowak", "fhnkmnyhj");
		productionMap.put("Alicja.Losowa", "haslohaslo");

		testingMap = new HashMap<String, String>();
		testingMap.put("Andrzej.Konieczny", "2345678");
		testingMap.put("Rafal.Niekonieczny", "innehaslo");
		testingMap.put("Jan.Niezbedny", "hunter2");
		
		developmentMap = new HashMap<String, String>();
		developmentMap.put("Lucjan.Nowak", "slabe_haslo");
		developmentMap.put("Piotr.Testowy", "azxzsw34");
		developmentMap.put("Maria.Antonina", "nielubieadmina");
		
		envMap = new HashMap<String, Map<String, String>>();
		envMap.put("Produkcyjne", productionMap);
		envMap.put("Testowe", testingMap);
		envMap.put("Deweloperskie", developmentMap);

	}
	
	@Override
	public void start(Stage primaryStage) {

		dialog = new Dialog<>();
		dialog.setTitle("Logowanie");
		dialog.setHeaderText("Logowanie do sytemu XYZ");

		// Set the icon (must be included in the project).
		//dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

		// Set the button types.
		ButtonType logonButtonType = new ButtonType("Logon", ButtonData.OK_DONE);
		ButtonType cancelButtonType = new ButtonType("Anuluj", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(logonButtonType, cancelButtonType);
		
		// Enable/Disable login button depending on whether a username was entered.
		Node buttonLogon = dialog.getDialogPane().lookupButton(logonButtonType);
		
		ObservableList<String> observableEnvironmentsList = FXCollections.observableArrayList();
		observableEnvironmentsList.addAll(envMap.keySet());
		choiceBoxEnvironment = new ChoiceBox<String>(observableEnvironmentsList);
		choiceBoxEnvironment.getSelectionModel().selectFirst();
		choiceBoxEnvironment.valueProperty().addListener(
			(observable, oldVal, newVal) -> { comboBoxUser.getItems().clear();
			comboBoxUser.getItems().addAll(envMap.get(newVal).keySet());
			}
		);
		
		comboBoxUser = new ComboBox<String>();
		comboBoxUser.getItems().addAll(productionMap.keySet());
		comboBoxUser.setEditable(true);
		comboBoxUser.setPromptText("Nazwa uzytkownika");
		
		PasswordField password = new PasswordField();
		password.setPromptText("Haslo");
	
		BooleanBinding booleanBind = comboBoxUser.valueProperty().asString().isEqualTo("")
                          .or(password.textProperty().isEmpty());
		
		buttonLogon.disableProperty().bind(booleanBind);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 80, 10, 10));
		
		grid.add(new Label("Srodowisko:"), 0, 0);
		grid.add(choiceBoxEnvironment, 1, 0);
		grid.add(new Label("Login:"), 0, 1);
		grid.add(comboBoxUser, 1, 1);
		grid.add(new Label("Haslo:"), 0, 2);
		grid.add(password, 1, 2);
		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> comboBoxUser.requestFocus());
		
		// Convert the result to a username-password-pair when the login button is clicked.
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == logonButtonType) {
		        return new Pair<String, String>(choiceBoxEnvironment.getValue(),comboBoxUser.getValue());
		    }
		    return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		result.ifPresent(envUsername -> {
		    System.out.println("Environment=" + envUsername.getKey() + ", Username=" + envUsername.getValue());
		});
	}
	
}
