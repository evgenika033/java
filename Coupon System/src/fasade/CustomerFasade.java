package fasade;

import java.util.List;

import beans.Category;
import beans.Coupon;
import beans.Customer;
import exceptions.DaoException;

public class CustomerFasade extends ClientFasade {

	private int customerID;

	public int getCustomerID() {
		return customerID;
	}

	public CustomerFasade() {
		// TODO Auto-generated constructor stub
	}

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

	public void purchaseCoupon(Coupon coupon) {

	}

// לבדוק מה מחזירה מטודה
	public List<Coupon> getCustomerCoupons() throws DaoException {
		return couponsDao.getCustomerCoupons(customerID);

	}

	public List<Coupon> getCustomerCoupons(Category category) throws DaoException {
		return couponsDao.getCustomerCoupons(customerID, category);

	}

	public List<Coupon> getCustomerCoupons(double maxPrice) throws DaoException {
		return couponsDao.getCustomerCoupons(customerID, maxPrice);

	}

	public Customer getCustomerDetails() throws DaoException {
		return customersDao.get(customerID);

	}

}
