package fasade;

import java.util.List;

import beans.Category;
import beans.Coupon;
import beans.Customer;
import exceptions.DaoException;

public class CustomerFasade extends ClientFasade {

	private int customerID;

	/**
	 * ctor
	 */
	public CustomerFasade() {

	}

	/**
	 * ctor
	 * 
	 * @param customerID
	 */
	public CustomerFasade(int customerID) {
		this.customerID = customerID;
	}

	@Override
	public boolean login(String email, String password) throws DaoException {
		Customer customer = customersDao.customerLogin(email, password);
		if (customer != null) {
			customerID = customer.getID();
			return true;
		}
		return false;

	}

	/**
	 * purchase coupon
	 * 
	 * @param couponID
	 * @throws DaoException
	 */
	public void purchaseCoupon(int couponID) throws DaoException {
		couponsDao.addCouponPurchase(customerID, couponID);
	}

	/**
	 * check if this coupon can be purchase by current customer
	 * 
	 * @param couponID
	 * @return boolean
	 * @throws DaoException
	 */
	public boolean couponCanBePurchase(int couponID) throws DaoException {
		boolean forCustomerTest = true;
		return couponsDao.isPurchaseCouponCustomerValid(couponID, customerID, forCustomerTest);
	}

	/**
	 * get customer coupons
	 * 
	 * @return list of coupons
	 * @throws DaoException
	 */
	public List<Coupon> getCustomerCoupons() throws DaoException {
		return couponsDao.getCustomerCoupons(customerID);

	}

	/**
	 * get customer coupons by category
	 * 
	 * @param category
	 * @return list of coupons
	 * @throws DaoException
	 */
	public List<Coupon> getCustomerCoupons(Category category) throws DaoException {
		return couponsDao.getCustomerCoupons(customerID, category);

	}

	/**
	 * get customer coupons where price less then max price
	 * 
	 * @param maxPrice
	 * @return list of coupons
	 * @throws DaoException
	 */
	public List<Coupon> getCustomerCoupons(double maxPrice) throws DaoException {
		return couponsDao.getCustomerCoupons(customerID, maxPrice);

	}

	/**
	 * get customer details
	 * 
	 * @return Customer
	 * @throws DaoException
	 */
	public Customer getCustomerDetails() throws DaoException {
		return customersDao.get(customerID);

	}

	/**
	 * get customer id
	 * 
	 * @return customerID
	 */
	public int getCustomerID() {
		return customerID;
	}
}
