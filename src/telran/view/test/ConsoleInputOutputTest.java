package telran.view.test;

import static org.junit.jupiter.api.Assertions.*;
import telran.view.console.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import telran.view.console.InputOutput;

class ConsoleInputOutputTest {
	ConsoleInputOutput io = new ConsoleInputOutput();

	@Test
	void testReadObject() {
		int[] numbers = io.readObject("Enter two integer numbers separated by #",
				"Must be two numbers", str -> {
					String[] strAr = str.split("#");
					return new int[] {Integer.parseInt(strAr[0]), Integer.parseInt(strAr[1])};
				});
		io.writeLine("Sum of entered numbers is " + (numbers[0] + numbers[1]));
}
	@Test
	void testReadInt() {
		int number = io.readInt("enter integer number", "must be smth");
		io.writeLine("entered number" + number);
	}
	@Test
	void testReadIntRange() {
		int number = io.readInt("enter range of integers, min and max", "must be two integers", 4, 5);
		io.writeLine("entered rahne is" + number);
	}
	@Test
	void testReadLong() {
		long lng = io.readLong("enter long number", "must be long type");
		io.writeLine("entered number is" + lng);
	}
	@Test
	void testStringPredicate(){
		String value = io.readString("Enter string with 4 symbols", "must match predicate",
				str -> str.length() == 4);
		io.writeLine("entered symbols are" + value);
	}
	@Test
	void testStringPredicateSet() {
		String res = io.readString("Enter either apple or orange",
				"Neither apple nor orange",
				new HashSet(Arrays.asList("apple", "orange")));
	}
	@Test
	void testReadDate() {
		LocalDate date = io.readDate("enter date", "must be in date format");
	}
	@Test
	void testReadDateRange() {
		LocalDate date = io.readDate("enter range of two dates", "must be correct range",
				LocalDate.now(), LocalDate.now().plusDays(1));
	}
}
