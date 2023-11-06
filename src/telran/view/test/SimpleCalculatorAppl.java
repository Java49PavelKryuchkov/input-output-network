package telran.view.test;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.BinaryOperator;

import telran.view.console.*;

public class SimpleCalculatorAppl {

	public static void main(String[] args) {
		InputOutput io = new ConsoleInputOutput();
		Menu menu = new Menu("Calculator", getMainItems());
		menu.perform(io);

	}

	static void calculateMath(InputOutput io, BinaryOperator<Double> operator) {
		double first = io.readDouble("Enter first number", "Must be any number");
		double second = io.readDouble("Enter second number", "Must be any number");
		io.writeLine(operator.apply(first, second));
	}
	
	static Item[] getMainItems() {
		Item[] items = {
				Item.of("Mathematical calculator", io -> {
					new Menu(null, getMathItems());
				}),
				Item.of("Date Calculator", io -> getSubMenu(io
						, 0))
				
		};
		return items;
	}
	static void getSubMenu(InputOutput io, int number) {
		int operator = io.readInt("Enter", null);
		if(operator == 1) {
			getMathItems();
		}else {
			getDateItems();
		}
		
	}
	static Item[] getDateItems() {
		Item[] items = {
				Item.of("Add days", io -> calculateDate(io, 0))
		};
		return items;
	}
	static void calculateDate(InputOutput io, int num) {
		LocalDate date = io.readDate("Enter Date", "Must be valid date");
		int number = io.readInt("Days to add", "Must be integer value");
		io.writeLine(date.plusDays(number));
	}
	static Item[] getMathItems() {
		Item[] items = { Item.of("Add numbers", io -> calculateMath(io, (a,b)->a + b)),
				Item.of("Subtract numbers", io -> calculateMath(io, (a,b)->a - b)),
				Item.of("Multiply numbers", io -> calculateMath(io, (a,b)->a * b)),
				Item.of("Divide numbers", io -> calculateMath(io, (a,b)->a / b)),
				Item.ofExit()};
		return items;
	}



}