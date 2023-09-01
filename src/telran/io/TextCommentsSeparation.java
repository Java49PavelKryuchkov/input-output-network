package telran.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class TextCommentsSeparation {

	private static final String COMMENTS = "comments";
	private static final String TEXT = "text";

	public static void main(String[] args) throws IOException {
		if(args.length < 3) {
			System.out.println("Usage: must be three arguments (source, textFile, commentsFile)");
		}else {
			try(BufferedReader reader = new BufferedReader(new FileReader(args[0]));
					PrintWriter comments = new PrintWriter(args[1]);
					PrintWriter text = new PrintWriter(args[2]);
					){
				Map<String, List<String>> map = reader.lines().collect(Collectors.groupingBy(s -> s.trim().startsWith("//")
						? COMMENTS : TEXT));
				map.getOrDefault(COMMENTS, Collections.emptyList()).forEach(comments::println);
				map.getOrDefault(TEXT, Collections.emptyList()).forEach(text::println);
			}
		 catch(IOException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
}
}
