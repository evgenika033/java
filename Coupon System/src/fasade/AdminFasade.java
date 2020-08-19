package fasade;

import java.util.List;

import beans.Company;
import beans.Coupon;
import beans.Customer;
import exceptions.DaoException;
import utils.StringHelper;

public class AdminFasade extends ClientFasade {

	/**
	 * ctor
	 */
	public AdminFasade() {
	}

	/**
	 * Verify user by email/password hard coded
	 */
	@Override
	public boolean login(String email, String password) throws DaoException {
		return email == StringHelper.ADMIN_EMAIL && password == StringHelper.ADMIN_PASSWORD;
	}

	/**
	 * add company
	 * 
	 * @param company
	 * @throws DaoException
	 */
	public void addCompany(Company company) throws DaoException {
		companiesDao.add(company);
	}

	/**
	 * update company
	 * 
	 * @param company
	 * @throws DaoException
	 */
	public void updateCompany(Company company) throws DaoException {
		companiesDao.update(company);
	}

	/**
	 * delete company
	 * 
	 * @param companyID
	 * @throws DaoException
	 */
	public void deleteCompany(int companyID) throws DaoException {
		// get companies coupons before delete
		List<Coupon> coupons = couponsDao.getCompanyCoupons(companyID);
		// delete coupons(in delete do delete history purchase too)
		for (Coupon coupon : coupons) {
			couponsDao.delete(coupon.getID());
		}
		// delete company
		companiesDao.delete(companyID);
	}

	/**
	 * get companies
	 * 
	 * @return list of companies
	 * @throws DaoException
	 */
	public List<Company> getCompanies() throws DaoException {
		List<Company> companies = companiesDao.getAll();
		for (Company company : companies) {
			int id = company.getID();
			List<Coupon> coupons = couponsDao.getCompanyCoupons(id);
			company.setCoupons(coupons);
		}
		return companies;
	}

	/**
	 * 
	 * @param companyID
	 * @return Company
	 * @throws DaoException
	 */
	public Company getCompany(int companyID) throws DaoException {
		Company company = companiesDao.get(companyID);
		int id = company.getID();
		List<Coupon> coupons = couponsDao.getCompanyCoupons(id);
		company.setCoupons(coupons);
		return company;
	}

	/**
	 * get company by name
	 * 
	 * @param companyName company's name
	 * @return Company
	 * @throws DaoException database exceptions
	 */
	public Company getCompany(String companyName) throws DaoException {
		Company company = companiesDao.get(companyName);
		int id = company.getID();
		List<Coupon> coupons = couponsDao.getCompanyCoupons(id);
		company.setCoupons(coupons);
		return company;
	}

	/**
	 * add customer
	 * 
	 * @param customer
	 * @throws DaoException
	 */
	public void addCustomer(Customer customer) throws DaoException {
		customersDao.add(customer);
	}

	/**
	 * update customer
	 * 
	 * @param customer
	 * @throws DaoException
	 */
	public void updateCustomer(Customer customer) throws DaoException {
		customersDao.update(customer);
	}

	/**
	 * delete customer
	 * 
	 * @param customerID
	 * @throws DaoException
	 */
	public void deleteCustomer(int customerID) throws DaoException {
		List<Coupon> coupons = couponsDao.getCustomerCoupons(customerID);
		for (Coupon coupon : coupons) {
			couponsDao.deleteCouponPurchase(customerID, coupon.getID());
		}
		customersDao.delete(customerID);
	}

	/**
	 * get customers
	 * 
	 * @return Customer
	 * @throws DaoException
	 */
	public List<Customer> getCustomers() throws DaoException {
		List<Customer> customers = customersDao.getAll();
		for (Customer customer : customers) {
			int id = customer.getID();
			List<Coupon> coupons = couponsDao.getCustomerCoupons(id);
			customer.setCoupons(coupons);
		}
		return customers;
	}

	/**
	 * get customer
	 * 
	 * @param CustomerID
	 * @return Customer
	 * @throws DaoException
	 */
	public Customer getCustomer(int CustomerID) throws DaoException {
		Customer customer = customersDao.get(CustomerID);
		int id = customer.getID();
		List<Coupon> coupons = couponsDao.getCustomerCoupons(id);
		customer.setCoupons(coupons);
		return customer;

	}

	/**
	 * get customer by email
	 * 
	 * @param email
	 * @return Customer
	 * @throws DaoException
	 */
	public Customer getCustomer(String email) throws DaoException {
		Customer customer = customersDao.get(email);
		int id = customer.getID();
		List<Coupon> coupons = couponsDao.getCustomerCoupons(id);
		customer.setCoupons(coupons);
		return customer;

	}

}
