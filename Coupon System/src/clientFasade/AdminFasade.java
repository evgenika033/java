package clientFasade;

import java.util.List;

import beans.Company;
import beans.Customer;
import exceptions.DaoException;

public class AdminFasade extends ClientFasade {

	public AdminFasade() {
		super();

	}

	@Override
	boolean login(String email, String password) throws DaoException {

		return false;

	}

	public void addCompany(Company company) {

	}

	public void updateCompany(Company company) {

	}

	public void deleteCompany(int companyID) {

	}

	public List<Company> getAllCompanies() {
		return null;

	}

	public List<Company> getOneCompany(int CompanyID) {
		return null;

	}

	public void addCustomer(Customer customer) {

	}

	public void updateCustomer(Customer customer) {

	}

	public void deleteCustomer(int customerID) {

	}

	public List<Customer> getAllCustomers() {
		return null;

	}

	public List<Customer> getOneCustomer(int CustomerID) {
		return null;

	}

}
