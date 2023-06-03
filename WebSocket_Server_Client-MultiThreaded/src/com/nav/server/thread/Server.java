package com.nav.server.thread;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Server {
	
	ServerSocket serverSocket;
	

	public static void main(String[] args) throws IOException {

		ServerSocket serverSocket = new ServerSocket(1234);
		Server server = new Server(serverSocket);
		server.startServer();
	}
	
	public Server(ServerSocket serverSocket) {
		
		this.serverSocket = serverSocket;
	}
	
	/*
	 * Method to start server for new clients
	 */
	public void startServer() {
		try {			
			while(!serverSocket.isClosed()) {

				Socket socket = serverSocket.accept();
				System.out.println("A new client has connected !");
				ClientHandler clientHandler = new ClientHandler(socket);
				
				Thread thread = new Thread(clientHandler);
				thread.start();
			}
			
		} catch (IOException e) {
			closeServerSocket();
		}
	}
	
	/*
	 * separate method for closing server in case of exception
	 */
	private void closeServerSocket() {
		try {
			if(serverSocket != null)
				serverSocket.close();
		} catch (IOException e) {
			System.out.println("Exception while closing the socket: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
