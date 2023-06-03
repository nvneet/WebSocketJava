package nav.com.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

	public static void main(String[] args) throws IOException {

		Socket socket = null;
		InputStreamReader inputStreamReader = null;
		OutputStreamWriter outputStreamWriter = null;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		ServerSocket serverSocket = null;
		serverSocket = new ServerSocket(1234);

		try {

			while (true) {
				socket = serverSocket.accept();
				
				inputStreamReader = new InputStreamReader(socket.getInputStream());
				outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
				
				bufferedReader = new BufferedReader(inputStreamReader);
				bufferedWriter = new BufferedWriter(outputStreamWriter);
				
				while(true) {
					String msgFromClient = bufferedReader.readLine();

					System.out.println("Client : " + msgFromClient);
					bufferedWriter.write("MSG Received.");
					bufferedWriter.newLine();
					bufferedWriter.flush();
					if (msgFromClient.equalsIgnoreCase("bye"))
						break;
				}
				socket.close();
				inputStreamReader.close();
				outputStreamWriter.close();
				bufferedReader.close();
				bufferedWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
