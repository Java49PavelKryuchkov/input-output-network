package telran.view.console;

public class NumberOperationsMenu {
public static Item getOperationsItem(String name) {
	return Item.of(name, io -> {
		Item[] items = {
		Item.of("Add two numbers", NumberOperationsMenu::addTwoNumbers),
		Item.of("Subtract two numbers", NumberOperationsMenu::substractTwoNumbers),
		Item.of("Divide two numbers", NumberOperationsMenu::divideTwoNumbers),
		Item.of("Multiply two numbers", NumberOperationsMenu::multiplyTwoNumbers),
		Item.ofExit()
	};
	Menu menu = new Menu(name, items);
	menu.perform(io);
	});
}
static int[] getTwoNumbers(InputOutput io) {
	int firstNumber = io.readInt("Enter first number", "no number");
	int secondNumber = io.readInt("Enter second number","no number");
	return new int[] {firstNumber, secondNumber};
}
static void addTwoNumbers(InputOutput io) {
	int [] numbers = getTwoNumbers(io);
	io.writeLine(numbers[0] + numbers[1]);
}
static void substractTwoNumbers(InputOutput io) {
	int[] numbers = getTwoNumbers(io);
	io.writeLine(numbers[0] - numbers[1]);
}
static void divideTwoNumbers(InputOutput io) {
	int[] numbers = getTwoNumbers(io);
	io.writeLine(numbers[0] / numbers[1]);
}
static void multiplyTwoNumbers(InputOutput io) {
	int[] numbers = getTwoNumbers(io);
	io.writeLine(numbers[0] / numbers[1]);
}
}
