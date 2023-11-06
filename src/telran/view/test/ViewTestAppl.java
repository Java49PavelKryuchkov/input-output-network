package telran.view.test;

import telran.view.console.ConsoleInputOutput;
import telran.view.console.InputOutput;
import telran.view.console.*;

public class ViewTestAppl {
	public static void main(String[] args) {
		InputOutput io = new ConsoleInputOutput();
		Menu menu = new Menu("Operations", getItems());
		menu.perform(io);
	}
	private static Item[] getItems() {
		Item[] items = {
				NumberOperationsMenu.getOperationsItem("Number operations"),
				DatesOperationsMenu.getDatesOperationItems("Dates operations")
		};
		return items;
	}
}
