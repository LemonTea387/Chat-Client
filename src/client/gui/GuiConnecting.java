package client.gui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import client.network.ClientConnection;
import client.network.state.ConnectionState;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiConnecting {
	private URL layoutFile;
	private FXMLLoader loader;
	Stage stage;
	ClientConnection connection;
	
	// Creates a new GUIConnecting stage and attempts to connect
	public GuiConnecting() throws URISyntaxException, IOException {
		loadFXML();
		init();
		attemptConnect();
	}

	// Loads all the GUI from FXMLLoader
	private void init() throws IOException {
		Parent root = loader.load();
		Scene scene = new Scene(root);
		stage = new Stage();
		stage.setScene(scene);

	}

	// Loads FXML as a layout to FXMLLoader
	private void loadFXML() throws MalformedURLException, URISyntaxException {
		// TODO Auto-generated method stub
		layoutFile = (getClass().getResource("/ConnectingLayout.fxml").toURI()).toURL();
		loader = new FXMLLoader();
		loader.setLocation(layoutFile);
		loader.setController(this);

	}

	// Attempt to perform a connection
	private void attemptConnect() throws URISyntaxException, IOException {
		connection = new ClientConnection("localhost", 10000, this);
		connection.start();
	}

	// Getter for Stage to allow launcher calls
	public Stage getStage() {
		return stage;
	}

	// Getter for Connection to parse ClientConnection to other windows
	public ClientConnection getConnection() {
		return connection;
	}

	// Method to update the GUI once connection is properly established
	public void handleConnectionStatus(ConnectionState state) {
		if (state == ConnectionState.CONNECTION_OKAY) {
			Platform.runLater(() -> {
				stage.close();
			});
		}
	}


}
