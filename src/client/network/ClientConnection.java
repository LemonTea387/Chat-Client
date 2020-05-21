package client.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientConnection extends Thread {
	private String url;
	int port;
	private Socket connectionSocket;
	private InputStream input;
	private OutputStream output;
	private BufferedReader br;
	private BufferedWriter bw;

	public ClientConnection(String url, int port) {
		this.url = url;
		this.port = port;
	}

	@Override
	public void run() {
		if (handleConnection()) {
			handleCommunication();
		}
		super.run();
	}

	// Handles all the communication of client and server
	private void handleCommunication() {
		
	}

	// Handles the initial connection to the server
	private boolean handleConnection() {
		try {
			connectionSocket = new Socket(url, port);
			input = connectionSocket.getInputStream();
			output = connectionSocket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(input));
			bw = new BufferedWriter(new OutputStreamWriter(output));
			return true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void send(String message) {
		try {
			bw.write((message + "\n").toCharArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
