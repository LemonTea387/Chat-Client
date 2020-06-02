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

	public GuiLogin(ClientConnection connection) throws IOException, URISyntaxException {
			this.connection = connection;
			loadFXML();
			init();
	}



	private void init() throws IOException {
		VBox root = loader.<VBox>load();
		Scene scene = new Scene(root);
		loginWindow = new Stage();
		loginWindow.setScene(scene);
		loginWindow.setTitle("Login");
	}

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

	@FXML
	private void onLoginClick() {
		String username = textfield_Username.getText();
		String password = passwordfield_Password.getText();
	}

	@FXML
	private void onCancelClick() {
		try {
			connection.handleExit();
			loginWindow.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Stage getStage() {
		return loginWindow;
	}
}
