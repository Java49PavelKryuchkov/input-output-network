package telran.io.test;
import telran.io.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.IntStream;

class LineOrientedStreams {
	private static final int N_PRINTS = 10_000_000;
	String hello = "Hello";
	
	@Test
	void performancePrintStream() throws IOException{
		try(PrintStream stream = new PrintStream("BigFile")){
			IntStream.range(0, N_PRINTS).forEach(i -> stream.print("hello"));
		}
	}
	@Test
	void sumNumberLines() throws IOException{
		int res = 0;
//		try(BufferedReader reader = new BufferedReader(new FileReader("numbers.txt"))){
//			String line = null;
//			while((line = reader.readLine()) != null) {
//				res+= Integer.parseInt(line);
//			}
//		}
//		try(BufferedReader reader = new BufferedReader(new FileReader("numbers.txt"))){
//			res = reader.lines().mapToInt(Integer::parseInt).sum();
//		}
		res = Files.lines(Path.of("numbers.txt")).mapToInt(Integer::parseInt).sum();
		assertEquals(35, res);
	}

}
