package client.gui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import client.network.ClientConnection;
import javafx.application.Platform;
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
	boolean loginStatus;

	// Components to bind to FXML
	public TextField textfield_Username = null;


	public PasswordField passwordfield_Password = null;
	public Button button_Login = null;
	public Button button_Cancel = null;
	public Label label_Status = null;

	// Accepts a reference to the connection from the connecting frame
	public GuiLogin(ClientConnection connection) throws IOException, URISyntaxException {
		this.connection = connection;
		loadFXML();
		init();
	}

	// Loads in all the GUI from FXMLLoader
	private void init() throws IOException {
		VBox root = loader.<VBox>load();
		Scene scene = new Scene(root);
		loginWindow = new Stage();
		loginWindow.setScene(scene);
		loginWindow.setTitle("Login");
		loginWindow.setOnCloseRequest((e)->{
			handleExit();
		});
	}

	private void handleExit() {
		try {
			connection.handleExit();
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
		String username = textfield_Username.getText();
		String password = passwordfield_Password.getText();
		connection.login(username, password, this);
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
	
	public boolean isLoginStatus() {
		return loginStatus;
	}
	
	// Updates the label for login status and spawns a Client GUI if success
	public void updateLoginStatus(String status) {
		String statusText;
		if (("Success").equalsIgnoreCase(status)) {
			loginStatus = true;
			statusText = "Successfully logged in!";
			Platform.runLater(() -> {
				try {
					label_Status.setText(statusText);
					Thread.sleep(1000);
					loginWindow.close();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			});
		} else {
			loginStatus = false;
			statusText = "Failed to log in!";
			Platform.runLater(() -> {
				label_Status.setText(statusText);
			});
		}

	}

}
