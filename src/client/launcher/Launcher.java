package client.launcher;

import client.gui.GuiClient;
import client.gui.GuiLogin;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		GuiLogin loginWindow = new GuiLogin();
		loginWindow.getStage().showAndWait();
		GuiClient clientWindow = new GuiClient(loginWindow.getConnection());
		clientWindow.getStage().show();
	}

}
