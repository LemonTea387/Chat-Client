package client.launcher;

import client.gui.GuiClient;
import client.gui.GuiConnecting;
import client.gui.GuiLogin;
import client.network.state.ConnectionState;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		GuiConnecting connecting = new GuiConnecting();
		connecting.getStage().showAndWait();
		if (connecting.getConnection().getConnectionState() == ConnectionState.CONNECTION_OKAY) {
			GuiLogin login = new GuiLogin(connecting.getConnection());
			login.getStage().showAndWait();
			if (login.isLoginStatus()) {
				GuiClient client = new GuiClient(connecting.getConnection());
				client.getStage().show();
			}
		}
	}

}
