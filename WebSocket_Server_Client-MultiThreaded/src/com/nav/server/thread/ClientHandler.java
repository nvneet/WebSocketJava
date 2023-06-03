package com.nav.server.thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

	public static ArrayList<ClientHandler> clietHandlers = new ArrayList<>();
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;

	private String clientUsername;

	public ClientHandler(Socket socket) {
		try {

			this.socket = socket;
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.clientUsername = bufferedReader.readLine();
			clietHandlers.add(this);
			broadcastMessage("SERVER: " + clientUsername + " has entered the chat!");

		} catch (IOException e) {
			closeSocketnIOStreams(socket, bufferedReader, bufferedWriter);
		}
	}

	@Override
	public void run() {
		String messageFromClient;

		while (socket.isConnected()) {
			try {
				messageFromClient = bufferedReader.readLine();
				broadcastMessage(messageFromClient);
			} catch (IOException e) {
				closeSocketnIOStreams(socket, bufferedReader, bufferedWriter);
				break;
			}
		}
	}

	private void broadcastMessage(String messageFromClient) {

		clietHandlers.forEach(clientHandler -> {
			try {
				if (!clientHandler.clientUsername.equals(clientUsername)) {
					clientHandler.bufferedWriter.write(messageFromClient);
					clientHandler.bufferedWriter.newLine();
					clientHandler.bufferedWriter.flush();
				}
			} catch (IOException e) {

			}
		});
	}

	public void removeClientHandler() {
		clietHandlers.remove(this);
		broadcastMessage("SERVER: " + clientUsername + " has left the chat.");
	}

	private void closeSocketnIOStreams(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
		removeClientHandler();
		try {
			if (socket != null)
				socket.close();
			if (bufferedReader != null)
				bufferedReader.close();
			if (bufferedWriter != null)
				bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
