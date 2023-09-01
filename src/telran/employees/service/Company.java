package telran.employees.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import telran.employees.dto.DepartmentSalary;
import telran.employees.dto.Employee;
import telran.employees.dto.SalaryDistribution;

public interface Company {
	boolean addEmployee(Employee empl);
	Employee removeEmployee(long id);
	Employee getEmployee(long id);
	List<Employee> getEmployees();
	List<DepartmentSalary> getDepartmentSalaryDistribution();
	List<SalaryDistribution> getSalaryDistribution(int interval);//returns salary values distribution 
	default void restore(String filePath) {
		if(Files.exists(Path.of(filePath))) {
			try(ObjectInputStream stream = new ObjectInputStream(new FileInputStream(filePath))){
				List<Employee> employees = (List<Employee>) stream.readObject();
				employees.forEach(e -> addEmployee(e));
			}catch(Exception e){
				throw new RuntimeException(e.toString());
			}
		}
	};
	default void save (String filePath) {
		try(ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(filePath))){
			stream.writeObject(getEmployees());
		}catch(Exception e){
			throw new RuntimeException(e.toString());
		}
	};	
	List<Employee> getEmployeesByDepartment(String department);
	List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo);
	List<Employee> getEmployeesByAge(int ageFrom, int ageTo);
	Employee updateSalary(long id, int newSalary);
	Employee updateDepartment(long id, String newDepartment);
	
}
