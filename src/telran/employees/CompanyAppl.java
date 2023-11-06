package telran.employees;

import telran.employees.service.CompanyImpl;

public class CompanyAppl {
	private static final String DEFAULT_FILE_NAME = "employees.data";
	private static String filename;
	
	public static void main(String[] args) {
		filename = args.length > 0 ? args[0] : DEFAULT_FILE_NAME;
		CompanyImpl company = new CompanyImpl();
		

	}

}
