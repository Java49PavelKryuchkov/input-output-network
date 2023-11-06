package telran.employees.service;
import java.io.*;
import static org.junit.jupiter.api.DynamicTest.stream;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import telran.employees.dto.DepartmentSalary;
import telran.employees.dto.Employee;
import telran.employees.dto.SalaryDistribution;
import telran.employees.dto.*;
public class CompanyImpl implements Company {
LinkedHashMap<Long, Employee> employees = new LinkedHashMap<>();
TreeMap<Integer, Collection<Employee>> employeeSalary = new TreeMap<>();
HashMap<String, Collection<Employee>> employeeDep = new HashMap<>();
TreeMap<LocalDate, Collection<Employee>> employeeAge = new TreeMap<>();
ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
Lock readLock = readWriteLock.readLock();
Lock writeLock = readWriteLock.writeLock();
	@Override
	public boolean addEmployee(Employee empl) {
		try {
			writeLock.lock();
			boolean res = false;
			Employee emplRes = employees.putIfAbsent(empl.id(), empl);
			if (emplRes == null) {
				res = true;
				addEmployeeSalary(empl);
				addEmployeeDepartment(empl);
				addEmployeeAge(empl);
			}
			return res;
		} finally {
			writeLock.unlock();
		}
	}
	private <T> void addToIndex(Employee empl, T key, Map<T, Collection<Employee>> map) {
		map.computeIfAbsent(key, k -> new HashSet<>()).add(empl);
	}

	private void addEmployeeAge(Employee empl) {
		LocalDate age = empl.birthDate();
		addToIndex(empl, age, employeeAge);
		
	}

	private void addEmployeeDepartment(Employee empl) {
		String dep = empl.department();
		addToIndex(empl, dep, employeeDep);
		
	}

	private void addEmployeeSalary(Employee empl) {
		int salary = empl.salary();
		addToIndex(empl, salary, employeeSalary);
		
	}

	@Override
	public Employee removeEmployee(long id) {
		try {
			writeLock.lock();
			Employee res = employees.remove(id);
			if (res != null) {
				removeEmployeeSalary(res);
				removeEmployeeDep(res);
				removeEmployeeAge(res);
			}
			return res;
		} finally {
			writeLock.unlock();
		}
	}

	private void removeEmployeeAge(Employee empl) {
		LocalDate currentDate = LocalDate.now();
		int age = Period.between(empl.birthDate(), currentDate).getYears();
		Collection<Employee> ageCol = employeeAge.get(age);
		ageCol.remove(empl);
		if(ageCol.isEmpty()) {
			employeeAge.remove(age);
		}
		
	}

	private void removeEmployeeDep(Employee empl) {
		String dep = empl.department();
		Collection<Employee> employeeCol = employeeDep.get(dep);
		employeeCol.remove(empl);
		if(employeeCol.isEmpty()) {
			employeeDep.remove(dep);
		}
		
	}

	private void removeEmployeeSalary(Employee res) {
		int salary = res.salary();
		Collection<Employee> employeeCol = employeeSalary.get(salary);
		employeeCol.remove(res);
		if(employeeCol.isEmpty()) {
			employeeSalary.remove(salary);
		}
	}

	@Override
	public Employee getEmployee(long id) {
		try {
			readLock.lock();
			return employees.get(id);
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public List<Employee> getEmployees() {
		try {
			readLock.lock();
			return new ArrayList<>(employees.values());
		} finally {
			readLock.unlock();
		}
		
	}

	@Override
	public List<DepartmentSalary> getDepartmentSalaryDistribution() {
		return employees.values().stream().collect(Collectors.groupingBy((Employee::department), 
				Collectors.averagingInt(Employee::salary))).entrySet().stream()
				.map(e -> new DepartmentSalary(e.getKey(), e.getValue())).toList();
	}

	@Override
	public List<SalaryDistribution> getSalaryDistribution(int interval) {
		return employees.values().stream().collect(Collectors.groupingBy(e -> e.salary() / interval, 
				Collectors.counting())).entrySet().stream().map(e -> 
				new SalaryDistribution(e.getKey() * interval, e.getKey() * interval + interval, e.getValue().intValue())).toList();
	}	

	@Override
	public void restore(String filePath) {
		if(Files.exists(Path.of(filePath))) {
			try(
					ObjectInputStream stream = new ObjectInputStream(new FileInputStream(filePath))){
				List<Employee> employeeRestore = (List<Employee>) stream.readObject();
				employeeRestore.forEach(e -> addEmployee(e));
			}catch(Exception e) {
				throw new RuntimeException(e.toString());
			}
		}
	}

	@Override
	public void save(String filePath) {
		try(
				ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(filePath))){
			stream.writeObject(getEmployees());
		}catch(Exception e) {
			throw new RuntimeException(e.toString());
		}

	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		Collection<Employee> depCol = employeeDep.get(department);
		ArrayList<Employee> list = new ArrayList<>();
		if(depCol != null) {
			list.addAll(depCol);
		}
		return list;
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		return employeeSalary.subMap(salaryFrom, true, salaryTo, true).values()
				.stream().flatMap(col -> col.stream().sorted((empl1, empl2) ->
				Long.compare(empl1.id(), empl2.id()))).toList();
	}

	@Override
	public List<Employee> getEmployeesByAge(int ageFrom, int ageTo) {
		LocalDate dateTo = LocalDate.now().minusYears(ageFrom);
		LocalDate dateFrom = LocalDate.now().minusYears(ageTo);
		return employeeAge.subMap(dateFrom, true, dateTo, true).values()
				.stream().flatMap(col -> col.stream().sorted((empl1, empl2) -> 
				Long.compare(empl1.id(), empl2.id()))).toList();
	}

	@Override
	public Employee updateSalary(long id, int newSalary) {
		Employee empl = removeEmployee(id);
		if(empl != null) {
			Employee newEmployee = new Employee(id, empl.name(), empl.department(), newSalary, empl.birthDate());
			addEmployee(newEmployee);
		}
		return empl;
	}

	@Override
	public Employee updateDepartment(long id, String newDepartment) {
		Employee empl = removeEmployee(id);
		if(empl != null) {
			Employee newEmployee = new Employee(id, empl.name(), newDepartment, empl.salary(), empl.birthDate());
			addEmployee(newEmployee);
		}
		return empl;
	}

}
