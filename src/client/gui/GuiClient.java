package client.gui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.fxml.FXMLLoader;

public class GuiClient {
	URL mainLayout = null;
	public GuiClient() throws IOException {
		try {
			mainLayout = (getClass().getResource("/MainLayout.fxml").toURI()).toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(mainLayout);
		loader.setController(this);
	}

}
