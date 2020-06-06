package client.gui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import client.network.ClientConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GuiLogin {
	private URL layoutFile;
	private FXMLLoader loader;

	ClientConnection connection;
	Stage loginWindow;

	// Components to bind to FXML
	public TextField textfield_Username = null;
	public PasswordField passwordfield_Password = null;
	public Button button_Login = null;
	public Button button_Cancel = null;
	public Label label_Status = null;

	// Accepts a reference to the connection from the connecting frame
	public GuiLogin() throws IOException, URISyntaxException {
		loadFXML();
		init();
	}

	private boolean attemptConnect() {
		connection = new ClientConnection("localhost", 10000);
		if (connection.handleConnection()) {
			return true;
		} else {
			System.out.println("Connection failed!");
			label_Status.setText("Connection failed!");
			return false;
		}
	}

	// Loads in all the GUI from FXMLLoader
	private void init() throws IOException {
		VBox root = loader.<VBox>load();
		Scene scene = new Scene(root);
		loginWindow = new Stage();
		loginWindow.setScene(scene);
		loginWindow.setTitle("Login");
		loginWindow.setOnCloseRequest((e) -> {
			handleExit();
		});
	}

	// Handler for exits in this stage of program
	private void handleExit() {
		try {
			if (connection != null && connection.getSocket() != null) {
				connection.handleExit();
			}
			loginWindow.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Loads FXML as layout for FXMLLoader
	private void loadFXML() {
		try {
			layoutFile = (getClass().getResource("/LoginFieldsLayout.fxml").toURI()).toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		loader = new FXMLLoader();
		loader.setLocation(layoutFile);
		loader.setController(this);
	}

	// Button Function for "Login" Button
	@FXML
	private void onLoginClick() {
		if (attemptConnect()) {
			String username = textfield_Username.getText().trim();
			String password = passwordfield_Password.getText();
			if (connection.login(username, password)) {
				label_Status.setText("Successfully Logged in!");
				loginWindow.close();
			} else {
				label_Status.setText("Failed to login!");
			}
		}
	}

	// Button Function for "Cancel" Button
	@FXML
	private void onCancelClick() {
		handleExit();
	}

	// Getter for stage to be called from launcher
	public Stage getStage() {
		return loginWindow;
	}

}
