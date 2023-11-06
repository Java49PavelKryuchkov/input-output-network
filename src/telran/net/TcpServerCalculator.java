package telran.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;
import java.util.function.BinaryOperator;

import telran.view.console.ConsoleInputOutput;
import telran.view.console.InputOutput;
import telran.view.console.Item;

public class TcpServerCalculator {

 static final int PORT = 5000;

	public static void main(String[] args) throws Exception{
		ApplProtocol protocol = new CalculatorProtocol();
		TcpServer server = new TcpServer(PORT, protocol);
		server.run();
		
	}


}
