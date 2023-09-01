package telran.io;
import java.io.*;

public class InputOutputLines {
public static void main(String[] args) {
	if(args.length < 2) {
		System.out.println("Usage: must be two arguments (source, destination)");
	}else {
		try(
				BufferedReader reader = getInput(args[0]);
				PrintStream output = getOutput(args[1]);
				){
			inputOutput(reader, output);
		}
		catch(IOException e) {
					System.out.println(e.getMessage());
				}
	}
}

private static void inputOutput(BufferedReader input, PrintStream output) throws IOException{
	String line = null;
	while((line = input.readLine()) != null) {
		output.println(line);
	}
}

private static BufferedReader getInput(String source) throws FileNotFoundException{
	BufferedReader res = source.equalsIgnoreCase("console") ? new BufferedReader(new InputStreamReader(System.in)) : 
		new BufferedReader(new FileReader(source));
	return res;
}

private static PrintStream getOutput(String destination) throws FileNotFoundException{
	PrintStream res = destination.equalsIgnoreCase("console") ? System.out : 
		new PrintStream(destination);
	return res;
}
}
