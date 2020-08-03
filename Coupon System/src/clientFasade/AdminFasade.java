package clientFasade;

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
	 * 
	 * @param company
	 * @throws DaoException
	 */
	public void addCompany(Company company) throws DaoException {
		companiesDao.add(company);
	}

	/**
	 * 
	 * @param company
	 * @throws DaoException
	 */
	public void updateCompany(Company company) throws DaoException {
		companiesDao.update(company);
	}

	/**
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
	 * 
	 * @return
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
	 * @param CompanyID
	 * @return
	 * @throws DaoException
	 */
	public Company getCompany(int CompanyID) throws DaoException {
		Company company = companiesDao.get(CompanyID);
		int id = company.getID();
		List<Coupon> coupons = couponsDao.getCompanyCoupons(id);
		company.setCoupons(coupons);
		return company;
	}

	/**
	 * 
	 * @param customer
	 * @throws DaoException
	 */
	public void addCustomer(Customer customer) throws DaoException {
		customersDao.add(customer);
	}

	/**
	 * 
	 * @param customer
	 * @throws DaoException
	 */
	public void updateCustomer(Customer customer) throws DaoException {
		customersDao.update(customer);
	}

	/**
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
	 * 
	 * @return
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
	 * 
	 * @param CustomerID
	 * @return
	 * @throws DaoException
	 */
	public Customer getCustomer(int CustomerID) throws DaoException {
		Customer customer = customersDao.get(CustomerID);
		int id = customer.getID();
		List<Coupon> coupons = couponsDao.getCustomerCoupons(id);
		customer.setCoupons(coupons);
		return customer;

	}

}
