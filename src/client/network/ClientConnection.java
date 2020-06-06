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
import java.util.ArrayList;

import client.gui.GuiClient;
import client.gui.GuiConnecting;
import client.gui.GuiLogin;
import client.listeners.MessageListener;
import client.network.state.ConnectionState;

/**
 * 
 * @author LemonTea387
 *
 */
public class ClientConnection extends Thread {
	private String url;
	int port;
	private Socket connectionSocket;
	private InputStream input;
	private OutputStream output;
	private BufferedReader br;
	private BufferedWriter bw;
	private ArrayList<MessageListener> msgListeners = new ArrayList<>();

	ConnectionState state;
	GuiConnecting guiConnecting;
	GuiLogin guiLogin;
	GuiClient guiClient;
	boolean loggedIn;
	
	public ClientConnection(String url, int port, GuiConnecting guiConnecting) {
		this.guiConnecting = guiConnecting;
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
	
	/**
	 * Specialize method to login with supplied credentials
	 * @param username Supply username credential
	 * @param password Supply password credential
	 * @param guiInCharge 
	 */
	// Login with credentials provided
	public void login(String username, String password, GuiLogin guiInCharge) {
		guiLogin = guiInCharge;
		send("login " + username + " " + password);
	}
	
	/**
	 * Called when program is about to exit, closes the client connection
	 * @throws IOException When streams and socket failed to close()
	 * 
	 */
	// Handler for when connection should be closed
	public void handleExit() throws IOException {
		send("quit");
		input.close();
		output.close();
		connectionSocket.close();
	}
	
	/**
	 * 
	 * @return ConnectionState
	 */
	public ConnectionState getConnectionState() {
		return state;
	}

	// Handles all the communication of client and server
	private void handleCommunication() {
		String input;
		String[] tokens;
		String cmd, attribute, content;
		try {
			while (!connectionSocket.isClosed() && (input = br.readLine().trim()) != null) {
				tokens = input.split(" ", 3);
				if (tokens.length == 3) {
					cmd = tokens[0];
					attribute = tokens[1];
					content = tokens[2];
					if ("message".equalsIgnoreCase(cmd))
						handleReceiveMessage(attribute, content);
				} else {
					if (tokens[0].equalsIgnoreCase("Success")) {
						guiLogin.updateLoginStatus(tokens[0]);
					} else if (tokens[0].equalsIgnoreCase("Failed")) {
						guiLogin.updateLoginStatus(tokens[0]);
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Handles every message received
	private void handleReceiveMessage(String sender, String content) {
		for (MessageListener listener : msgListeners) {
			listener.onMessage(sender, content);
		}
	}

	// Handles the initial connection to the server
	private boolean handleConnection() {
		try {
			connectionSocket = new Socket(url, port);
			input = connectionSocket.getInputStream();
			output = connectionSocket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(input));
			bw = new BufferedWriter(new OutputStreamWriter(output));
			this.addMessageListener((sender, content) -> {
				System.out.println(sender + content);
			});
			state = ConnectionState.CONNECTION_OKAY;
			guiConnecting.updateConnectionStatus(state);
			return true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		state = ConnectionState.CONNECTION_FAILED;
		guiConnecting.updateConnectionStatus(state);
		return false;
	}

	// Writes a string to the output stream of clientSocket
	private void send(String command) {
		try {
			bw.write((command + "\n").toCharArray());
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addMessageListener(MessageListener listener) {
		msgListeners.add(listener);
	}

}
