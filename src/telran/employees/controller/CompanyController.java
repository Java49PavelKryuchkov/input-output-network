package telran.employees.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import telran.employees.dto.Employee;
import telran.employees.service.Company;
import telran.view.console.InputOutput;
import telran.view.console.Item;

public class CompanyController {
	private static final long MIN_ID = 100000;
	private static final long MAX_ID = 999999;
	private static final int MIN_SALARY = 6000;
	private static final int MAX_SALARY = 50000;
	private static final int MAX_AGE = 75;
	private static final int MIN_AGE = 20;
static Company company;
public static ArrayList<Item> getCompanyItems(Company company){
	CompanyController.company = company;
	ArrayList<Item> res = new ArrayList<>(Arrays.asList(getItems()));
	return res;
}
private static Item[] getItems() {
	return new Item[] {
			Item.of("Add new Employee", CompanyController::addEmployeeItem),
			Item.of("Remove Employee", CompanyController::removeEmployeeItem),
			Item.of("All Employees", CompanyController::getEmployeesItem),
			Item.of("Data about Employee", CompanyController::getEmployeeItem),
			Item.of(" Employees by Salary", CompanyController::getEmployeesBySalaryItem),
			Item.of("Employees by Department", CompanyController::getEmployeesByDepartmentItem),
			Item.of("Update salary", CompanyController::updateSalaryItem),
			Item.of("Departments and Salary", CompanyController::getDepartmentSalaryDistributionItem),
			Item.of("Distribution by Salary", CompanyController::getSalaryDistributionItem),
			Item.of("Employees by Age", CompanyController::getEmployeesByAgeItem),
			Item.of("Update Department", CompanyController::updateDepartmentItem)
	};
	}
private static Set<String> departments = new HashSet<>(Arrays.asList(new String[] 
		{"QA", "Development", "Audit", "Management", "Accounting"}));

static void addEmployeeItem(InputOutput io) {
	long id = io.readLong("Enter Employee identity", "Wrong identity value", MIN_ID, MAX_ID);
	String name = io.readString("Enter name", "Wrong name", str -> str.matches("[A-Z][a-z]+"));
	String department = io.readString("Enter department", "Wrong department", departments );
	int salary = io.readInt("Enter salary", "Wrong salary", MIN_SALARY, MAX_SALARY);
	LocalDate birthDate = io.readDate("Enter birth data", "Wrong birth date entered",
			getBirthdate(MAX_AGE), getBirthdate(MIN_AGE));
	boolean res = company.addEmployee(new Employee(id, name, department, salary, birthDate));
	io.writeLine(res ? String.format("Employee with id %d has been added", id) : 
		String.format("Employee with id %d already exists", id));
}
private static LocalDate getBirthdate(int age) {
	return LocalDate.now().minusYears(age);
}

static void removeEmployeeItem(InputOutput io) {
	long id = io.readLong("Enter Employee identity", "Wrong identity value", MIN_ID, MAX_ID);
	Employee empl = company.removeEmployee(id);
	io.writeLine(String.format("Employee with id %d has been deleted"
			, empl.id()));
}
static void getEmployeeItem(InputOutput io) {
	long id = io.readLong("Enter Employee identity", "Wrong identity value", MIN_ID, MAX_ID);
	Employee empl = company.getEmployee(id);
	io.writeLine(String.format("Employee with id %d is"
			, empl.name()));
}
static void getEmployeesItem(InputOutput io) {
	genericWriteLine(company.getEmployees(), io);
}
static void getDepartmentSalaryDistributionItem(InputOutput io) {
	company.getDepartmentSalaryDistribution().forEach(io::writeLine);

}
static void getSalaryDistributionItem(InputOutput io) {
	int interval = io.readInt("Please enter salary interval", "Wrong interval"
			, MIN_SALARY, MAX_SALARY);
	genericWriteLine(company.getSalaryDistribution(interval), io);
}
static void getEmployeesByDepartmentItem(InputOutput io) {
	String dep = io.readString("Enter department", "Wrong department"
			, departments);
	genericWriteLine(company.getEmployeesByDepartment(dep), io);
}
private static <T> void genericWriteLine(List<T> list, InputOutput io) {
	list.forEach(io::writeLine);
}
static void getEmployeesBySalaryItem(InputOutput io) {
	int salaryFrom = io.readInt("Enter salary from", "Wring salary", MIN_SALARY, MAX_SALARY);
	int salaryTo = io.readInt("Enter salary to", "Wring salary", MIN_SALARY, MAX_SALARY);
	genericWriteLine(company.getEmployeesBySalary(salaryFrom, salaryTo), io);
}
static void getEmployeesByAgeItem(InputOutput io) {
	int dateTo = io.readInt("Enter age from", "Wrong age");
	int dateFrom = io.readInt("Enter age to", "Wrong age");
	genericWriteLine(company.getEmployeesByAge(dateFrom, dateTo), io);
}

static void updateSalaryItem(InputOutput io) {
	long id = io.readLong("Enter Employee identity", "Wrong identity value", MIN_ID, MAX_ID);
	int newSalary = io.readInt("Enter salary to", "Wring salary", MIN_SALARY, MAX_SALARY);
	Employee empl = company.updateSalary(id, newSalary);
	io.writeLine(String.format("Employee with updated salary is %d"
			, empl.name()));
}
static void updateDepartmentItem(InputOutput io) {
	long id = io.readLong("Enter Employee identity", "Wrong identity value", MIN_ID, MAX_ID);
	String newDep = io.readString("Enter department", "Wrong department"
			, departments);
	Employee empl = company.updateDepartment(id, newDep);
	io.writeLine(String.format("Employee with id %d is"
			, empl.name()));
}

}
