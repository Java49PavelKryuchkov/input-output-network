package telran.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;

import telran.view.console.*;

public class TcpClientCalculator {
	static final String HOST = "localhost";
	static final int PORT = 5000;
	public static void main(String[] args) throws Exception{
			try(TcpHandler handler = new TcpHandler(HOST, PORT)){
			InputOutput io = new ConsoleInputOutput();
			 Menu menu = new Menu("Calculator Application", Item.of("send request", io1 -> {
				 HashSet<String> requests = new HashSet<>(Arrays.asList("add", "minus",
						 "multiply", "divide"));
				 String requestType = io1.readString("Enter operation type " + requests,
						 "Wrong operation", requests);
				 double op1 = io1.readDouble("Enter first number", "Wrong number");
				 double op2 = io1.readDouble("Enter second number", "Wrong number");
				 double res = handler.send(requestType, new double[] {op1, op2});
				 io1.writeLine(res);
			 }), Item.ofExit());
			 menu.perform(io);

};
}
}