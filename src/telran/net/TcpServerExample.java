package telran.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;

public class TcpServerExample {

	private static final int PORT = 5000;

	public static void main(String[] args) throws Exception{
		ServerSocket serverSocket = new ServerSocket(PORT);
		System.out.println("Server is listening to the port" + PORT);
		while(true) {
			Socket socket = serverSocket.accept();
			clientRun(socket);
		}
	}

	private static void clientRun(Socket socket) {
		try(
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintStream writer = new PrintStream(socket.getOutputStream())){
			while(true) {
				String line = reader.readLine();
				if(line == null) {
					System.out.println("client closed connection normally");
					break;
				}	
				String response = getResponse(line);
				writer.println(response);
			}
				}catch(Exception e) {
					System.out.println("client closed abnormally connection");
				}
	}

	private static String getResponse(String line) {
		String response = "Wrong request structure, usage: <request type>#<string>";
		String[] tokens = line.split("#");
		if(tokens.length == 2) {
			response = switch(tokens[0]) {
			case "length" -> Integer.toString(tokens[1].length());
			case "reverse" -> new StringBuilder(tokens[1]).reverse().toString();
			default -> "wrong request type";
			};
		}
		return response;
	}

}
