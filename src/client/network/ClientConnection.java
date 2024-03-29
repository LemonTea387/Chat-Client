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
import java.util.List;

import client.gui.GuiClient;
import client.listeners.MessageListener;
import client.listeners.OnlineListener;
import client.network.state.ConnectionState;

/**
 * 
 * @author LemonTea387
 *
 */
public class ClientConnection {
	private String url;
	int port;
	private Socket connectionSocket;
	private InputStream input;
	private OutputStream output;
	private BufferedReader br;
	private BufferedWriter bw;
	private List<MessageListener> msgListeners = new ArrayList<MessageListener>();
	private List<OnlineListener> onListeners = new ArrayList<OnlineListener>();

	ConnectionState state;
	GuiClient guiClient;

	public ClientConnection(String url, int port) {
		this.url = url;
		this.port = port;
	}

	/**
	 * Specialize method to login with supplied credentials
	 * 
	 * @param username Supply username credential
	 * @param password Supply password credential
	 */
	// Login with credentials provided
	public boolean login(String username, String password) {
		String response = "";
		try {
			send("login " + username + " " + password);
			String input = br.readLine();
			if (input != null) {
				response += input;
			}
			System.out.println("response : " + response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if ("Success".equalsIgnoreCase(response)) {
			return true;
		}
		return false;
	}

	/**
	 * Called when program is about to exit, closes the client connection
	 * 
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
	 * @return Socket
	 */
	public Socket getSocket() {
		return connectionSocket;
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
				} else if ("Online".equalsIgnoreCase(tokens[0]) || "Offline".equalsIgnoreCase(tokens[0]))
					handleOnlineStatus(tokens[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleOnlineStatus(String username) {
		
	}

	// Handles every message received
	private void handleReceiveMessage(String sender, String content) {
		for (MessageListener listener : msgListeners) {
			listener.onMessage(sender, content);
		}
	}

	// Handles the initial connection to the server
	public boolean handleConnection() {
		try {
			connectionSocket = new Socket(url, port);
			input = connectionSocket.getInputStream();
			output = connectionSocket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(input));
			bw = new BufferedWriter(new OutputStreamWriter(output));

			this.addMessageListener((sender, content) -> {
				System.out.println(sender + content);
			});

			this.addOnlineListener(() -> {

			});
			return true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

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

	private void addOnlineListener(OnlineListener listener) {
		onListeners.add(listener);
	}

}
