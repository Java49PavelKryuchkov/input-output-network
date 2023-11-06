package telran.view.console;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DatesOperationsMenu {
public static Item getDatesOperationItems(String name) {
	return Item.of(name, io -> {
		Item[] items = {
				Item.of("Date after a given number of days", DatesOperationsMenu::dateAdding),
				Item.of("Date before a given number of days", DatesOperationsMenu::dateSubstracting),
				Item.of("Days between two dates", DatesOperationsMenu::dateBetween),
				Item.ofExit()
		};
		Menu menu = new Menu(name, items);
		menu.perform(io);
	});
	
}
static void dateAdding(InputOutput io) {
	LocalDate date = io.readDate("Enter a date", "Wring date");
	int number = io.readInt("Enter number of days", "Wrong number");
	io.writeLine(date.plusDays(number));
}
static void dateSubstracting(InputOutput io) {
	LocalDate date = io.readDate("Enter a date", "Wring date");
	int number = io.readInt("Enter number of days", "Wrong number");
	io.writeLine(date.minusDays(number));
}
static void dateBetween(InputOutput io) {
	LocalDate firstDate = io.readDate("Enter first date", "Wring date");
	LocalDate secondDate = io.readDate("Enter second date", "Wring date");
	io.writeLine(ChronoUnit.DAYS.between(firstDate, secondDate));
}
}
