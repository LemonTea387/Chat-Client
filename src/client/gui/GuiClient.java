package client.gui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GuiClient {
	private URL mainLayout;
	private FXMLLoader loader;
	
	Stage clientWindow;
	
	
	public GuiClient() throws IOException {
		loadFXML();
		init();
	}
	
	// Loads in all the GUI from FXMLLoader
	private void init() throws IOException {
		VBox root = loader.<VBox>load();
		Scene scene = new Scene(root);
		clientWindow = new Stage();
		clientWindow.setScene(scene);
		clientWindow.setTitle("Chat Client");
		}
	
	// Loads FXML as layout for FXMLLoader
	private void loadFXML() {
		try {
			mainLayout = (getClass().getResource("/MainLayout.fxml").toURI()).toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		loader = new FXMLLoader();
		loader.setLocation(mainLayout);
		loader.setController(this);
	}
	
	public Stage getStage() {
		return clientWindow;
	}
}
