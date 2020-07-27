package clientFasade;

import java.util.List;

import beans.Category;
import beans.Company;
import beans.Coupon;
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

	public void addCoupon(Coupon coupon) throws DaoException {
		couponsDao.add(coupon);
	}

	public void updateCoupon(Coupon coupon) throws DaoException {
		couponsDao.update(coupon);
	}

	public void deleteCoupon(int couponID) throws DaoException {
		couponsDao.delete(couponID);
	}

	public List<Coupon> getCompanyCoupons() throws DaoException {
		return couponsDao.getCompanyCoupons(companyID);

	}

	public List<Coupon> getCompanyCoupons(Category category) throws DaoException {
		return couponsDao.getCompanyCoupons(companyID, category);

	}

	public List<Coupon> getCompanyCoupons(double maxPrice) throws DaoException {
		return couponsDao.getCompanyCoupons(companyID, maxPrice);

	}

	public Company getCompanyDetails() throws DaoException {
		return companiesDao.get(companyID);

	}

}
