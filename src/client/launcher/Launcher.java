package client.launcher;

import client.network.ClientConnection;

public class Launcher {

	public static void main(String[] args) {
		ClientConnection connection = new ClientConnection("localhost", 10000);
		connection.start();
	}

}
