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

import client.listeners.MessageListener;

public class ClientConnection extends Thread {
	private String url;
	int port;
	private Socket connectionSocket;
	private InputStream input;
	private OutputStream output;
	private BufferedReader br;
	private BufferedWriter bw;
	private ArrayList<MessageListener> msgListeners = new ArrayList<>();

	public ClientConnection(String url, int port) {
		this.url = url;
		this.port = port;
	}

	@Override
	public void run() {
		if (handleConnection()) {
			login();
			handleCommunication();
		}
		super.run();
	}

	private void login() {
		send("login guest guest");
	}

	// Handles all the communication of client and server
	private void handleCommunication() {
		String input;
		String[] tokens;
		String cmd,attribute,content;
		send("message user hello there");
		try {
			while(!connectionSocket.isClosed() && (input = br.readLine().trim())!=null) {
				tokens = input.split(" ",3);
				cmd = tokens[0];
				attribute = tokens[1];
				content = tokens[2];
				if("message".equalsIgnoreCase(cmd))	handleReceiveMessage(attribute,content);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	

	private void handleReceiveMessage(String sender,String content) {
		for(MessageListener listener : msgListeners) {
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
			this.addMessageListener(new MessageListener() {

				@Override
				public void onMessage(String sender, String content) {
					System.out.println(sender + content);
				}});
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
}
