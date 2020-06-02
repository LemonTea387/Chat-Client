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

	public GuiConnecting() throws URISyntaxException, IOException {
		loadFXML();
		init();
		attemptConnect();
	}

	private void init() throws IOException {
		Parent root = loader.load();
		Scene scene = new Scene(root);
		stage = new Stage();
		stage.setScene(scene);

	}

	private void loadFXML() throws MalformedURLException, URISyntaxException {
		// TODO Auto-generated method stub
		layoutFile = (getClass().getResource("/ConnectingLayout.fxml").toURI()).toURL();
		loader = new FXMLLoader();
		loader.setLocation(layoutFile);
		loader.setController(this);

	}

	private void attemptConnect() throws URISyntaxException, IOException {
		connection = new ClientConnection("localhost", 10000, this);
		connection.start();
	}

	public Stage getStage() {
		return stage;
	}

	public ClientConnection getConnection() {
		return connection;
	}

	public void handleConnectionStatus(ConnectionState state) {
		if (state == ConnectionState.CONNECTION_OKAY) {
			Platform.runLater(() -> {
				stage.close();
			});
		}
	}
}
