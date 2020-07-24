package clientFasade;

import java.util.List;

import beans.Category;
import beans.Coupon;
import beans.Customer;
import exceptions.DaoException;

public class CustomerFasade extends ClientFasade {

	private int customerID;

	public CustomerFasade(int customerID) {
		this.customerID = customerID;
	}

	@Override
	public boolean login(String email, String password) throws DaoException {
		return customersDao.isCustomerExist(email, password);

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
