package telran.employees;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import telran.employees.dto.*;
import telran.employees.service.*;
import telran.net.*;

public class CompanyProtocol implements ApplProtocol {
	private Company company;
public CompanyProtocol(Company company) {
	this.company = company;
}

	@Override
	public Response getResponse(Request request) {
		Response response = null;
		String requestType = request.requestType();
		Serializable data = request.requestData();
		try {
			requestType = requestType.replace("/", "_");
			Method method = this.getClass().getDeclaredMethod(requestType, Serializable.class);
			Serializable responseData = (Serializable) method.invoke(this, data);
			response = new Response(ResponseCode.OK, responseData);
		}catch(NoSuchMethodException e) {
			response = new Response(ResponseCode.WRONG_TYPE, requestType +
		    		" is unsupported in the Company Protocol");
		}
		catch (InvocationTargetException e) {
			Throwable ce = e.getCause();
			String errorMessage = ce instanceof ClassCastException ?
					"Mismatching of received data type" : ce.getMessage();
			
			response = new Response(ResponseCode.WRONG_DATA, errorMessage);
		}
		catch (Exception e) {
			response = new Response(ResponseCode.WRONG_DATA, e.toString());
		}
		return response;
	}
	Serializable employees_age(Serializable data) {
		FromTo fromTo = (FromTo) data;
		int from = fromTo.from();
		int to = fromTo.to();
		return new ArrayList<>(company.getEmployeesByAge(from, to));
	}
	Serializable employees_salary(Serializable data) {
		FromTo fromTo = (FromTo) data;
		int from = fromTo.from();
		int to = fromTo.to();
		return new ArrayList<>(company.getEmployeesBySalary(from, to));
	}
	Serializable employees_department(Serializable data) {
		String dep = (String) data;
		return new ArrayList<>(company.getEmployeesByDepartment(dep));
	}
	Serializable salary_distribution(Serializable data) {
		int interval = (int) data;
		return new ArrayList<>(company.getSalaryDistribution(interval));
	}
	Serializable department_salary_distribution(Serializable data) {
		return new ArrayList<>(company.getDepartmentSalaryDistribution());
	}
	Serializable employee_remove(Serializable data) {
		long id = (long) data;
		return company.removeEmployee(id);
	}
	
	Serializable salary_update(Serializable data) {
		UpdateData<Integer> updateData = (UpdateData<Integer>) data;
		long id = updateData.id();
		int salary = updateData.data();
		return company.updateSalary(id, salary);
	}
	
	Serializable employee_add(Serializable data) {
		Employee empl = (Employee) data;
		return company.addEmployee(empl);
	}
	Serializable employee_get(Serializable data) {
		long id = (long) data;
		return company.getEmployee(id);
	}
	Serializable employees_get(Serializable data) {
		return new ArrayList<>(company.getEmployees());
	}
	Serializable department_update(Serializable data) {
		UpdateData<String> updateData = (UpdateData<String>) data;
		long id = updateData.id();
		String dep = updateData.data();
		return company.updateDepartment(id, dep);
	}

}
