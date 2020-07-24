package clientFasade;

import java.util.List;

import beans.Category;
import beans.Coupon;
import beans.Customer;
import exceptions.DaoException;

public class CompanyFacade extends ClientFasade {

	private int companyID;

	public CompanyFacade(int companyID) {
		super();
		this.companyID = companyID;
	}

	@Override
	public boolean login(String email, String password) throws DaoException {
		return companiesDao.isCompanyExist(email, password);
	}

	public void addCoupon(Coupon coupon) {

	}

	public void updateCoupon(Coupon coupon) {

	}

	public void deleteCoupon(int couponID) {

	}

	public List<Coupon> getCompanyCoupons() {
		return null;

	}

	public List<Coupon> getCompanyCoupons(Category category) {
		return null;

	}

	public List<Coupon> getCompanyCoupons(double maxPrice) {
		return null;

	}

	public Customer getCompanyDetails() {
		return null;

	}

}
