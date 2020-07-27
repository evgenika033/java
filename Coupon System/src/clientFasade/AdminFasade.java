package clientFasade;

import java.util.List;

import beans.Company;
import beans.Customer;
import exceptions.DaoException;
import utils.StringHelper;

public class AdminFasade extends ClientFasade {

	public AdminFasade() {
		super();

	}

	@Override
	public boolean login(String email, String password) throws DaoException {

		return email == StringHelper.ADMIN_EMAIL && password == StringHelper.ADMIN_PASSWORD;

	}

	public void addCompany(Company company) throws DaoException {
		companiesDao.add(company);
	}

	public void updateCompany(Company company) throws DaoException {
		companiesDao.update(company);
	}

	public void deleteCompany(int companyID) throws DaoException {
		companiesDao.delete(companyID);
	}

	public List<Company> getCompanies() throws DaoException {
		return companiesDao.getAll();

	}

	public Company getCompany(int CompanyID) throws DaoException {

		return companiesDao.get(CompanyID);

	}

	public void addCustomer(Customer customer) throws DaoException {
		customersDao.add(customer);
	}

	public void updateCustomer(Customer customer) throws DaoException {
		customersDao.update(customer);
	}

	public void deleteCustomer(int customerID) throws DaoException {
		customersDao.delete(customerID);
	}

	public List<Customer> getCustomers() throws DaoException {
		return customersDao.getAll();

	}

	public Customer getCustomer(int CustomerID) throws DaoException {
		return customersDao.get(CustomerID);

	}

}
