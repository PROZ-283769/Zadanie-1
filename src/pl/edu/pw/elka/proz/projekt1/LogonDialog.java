package pl.edu.pw.elka.proz.projekt1;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.util.Pair;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
/**
 * Class representing login window.
 * The window has choicebox for environment.
 * If login is sucessfull, it returns pair of
 * environment and username.
 * 
 * Uses GridPane to position elements.
 * 
 * @author Michał Sokólski
 *
 */
public class LogonDialog {
	
	private Dialog<ButtonType> dialog;
	private ChoiceBox<String> choiceBoxEnvironment;
	private ComboBox<String> comboBoxUser;
	private ButtonType logonButtonType;
	private ButtonType cancelButtonType;
	private PasswordField password;
	private Map<String, Environment> envMap;

	
	/**
	 * Default constructor for login window.
	 * 
	 * @param title
	 * 		  title of the login window.
	 * @param header
	 * 		  header of the login window.
	 */
	LogonDialog(String title, String header){
		
		initializeEnvironments();

		dialog = new Dialog<>();
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		
		createLayout();

	}

	/**
	 * Initializes the database with 3 environments:
	 * Production, Testing and Development.
	 * 
	 * All environments have 3 different users.
	 * 
	 * Environments are stored in envMap.
	 */
	public void initializeEnvironments() {

		Environment productionEnv = new Environment("Produkcyjne");
		productionEnv.addUser("Adam.Kowalski", "asdf001");
		productionEnv.addUser("Piotr.Nowak", "asdf000");
		productionEnv.addUser("Alicja.Losowa", "haslohaslo");

		Environment testingEnv = new Environment("Testowe");
		testingEnv.addUser("Andrzej.Konieczny", "12345");
		testingEnv.addUser("Rafał.Niekonieczny", "innehaslo");
		testingEnv.addUser("Jan.Niezbędny", "hunter2");

		Environment developmentEnv = new Environment("Deweloperskie");
		developmentEnv.addUser("Lucjan.Nowak", "slabe_haslo");
		developmentEnv.addUser("Piotr.Testowy", "azxzsw34");
		developmentEnv.addUser("Maria.Antonina", "nielubieadmina");

		envMap = new HashMap<String, Environment>();
		
		envMap.put(productionEnv.toString(), productionEnv);
		envMap.put(testingEnv.toString(), testingEnv);
		envMap.put(developmentEnv.toString(), developmentEnv);
	}

	/**
	 * Creates window layout.
	 */
	public void createLayout() {
		// Set the button types.
		logonButtonType = new ButtonType("Logon", ButtonData.OK_DONE);
		cancelButtonType = new ButtonType("Anuluj", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(logonButtonType, cancelButtonType);

		Node buttonLogon = dialog.getDialogPane().lookupButton(logonButtonType);

		// Create environment choicebox.
		ObservableList<String> observableEnvironmentsList = FXCollections.observableArrayList();
		observableEnvironmentsList.addAll(envMap.keySet());
		choiceBoxEnvironment = new ChoiceBox<String>(observableEnvironmentsList);
		choiceBoxEnvironment.getSelectionModel().selectFirst();
		choiceBoxEnvironment.valueProperty().addListener((observable, oldVal, newVal) -> {
			comboBoxUser.getItems().clear();
			comboBoxUser.getItems().addAll(envMap.get(newVal).getUsernames());
		});

		// Create username combobox
		comboBoxUser = new ComboBox<String>();
		comboBoxUser.getItems().addAll(envMap.get("Deweloperskie").getUsernames());
		comboBoxUser.setEditable(true);
		comboBoxUser.setPromptText("Nazwa użytkownika");

		password = new PasswordField();
		password.setPromptText("Haslo");

		// Enable/Disable logon button depending on whether a username and password was
		// entered.
		BooleanBinding booleanBind = comboBoxUser.valueProperty().asString().isEqualTo("")
				.or(password.textProperty().isEmpty());

		buttonLogon.disableProperty().bind(booleanBind);

		// fill the grid
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 80, 10, 10));

		grid.add(new Label("Środowisko:"), 0, 0);
		grid.add(choiceBoxEnvironment, 1, 0);
		grid.add(new Label("Login:"), 0, 1);
		grid.add(comboBoxUser, 1, 1);
		grid.add(new Label("Hasło:"), 0, 2);
		grid.add(password, 1, 2);
		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> comboBoxUser.requestFocus());

	}
	

	 /**
	  * Converts the result to a username-environemnt-pair when the login button is
	  * clicked and the password matches.
	  * 
	  * @param buttonType
	  * 	   Button that was clicked
	  * @return Pair of environment and username if login was successful, null if not.
	  */
	private Pair<Environment, String> resultConverter(Optional<ButtonType> buttonType){
		if (buttonType.get() == logonButtonType) {	
			String env = choiceBoxEnvironment.getValue();
			if (envMap.get(env).userExists(comboBoxUser.getValue())
					&& envMap.get(env).verifyLogin(comboBoxUser.getValue(),password.getText()))
				return new Pair<Environment, String>(envMap.get(env), comboBoxUser.getValue());
		}
		return null;
	};
	

	/**
	 * Redefinition of showAndWait method for dialog.
	 * 
	 * @return Pair of environment and username.
	 */
	public Optional<Pair<Environment, String>> showAndWait() {
		return Optional.ofNullable(resultConverter(dialog.showAndWait()));
}
}
