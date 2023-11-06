package telran.view.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class ConsoleInputOutput implements InputOutput{
private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
private PrintStream output = System.out;
	@Override
	public String readString(String prompt) {
		writeLine(prompt);
		try {
			String res = input.readLine();
			return res;
		}catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void write(Object obj) {
		output.println(obj);
	}

}
