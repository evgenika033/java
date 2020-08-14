package fasade;

import java.util.List;

import beans.Category;
import beans.Company;
import beans.Coupon;
import exceptions.DaoException;

public class CompanyFacade extends ClientFasade {

	private int companyID;

	public CompanyFacade() {
		// TODO Auto-generated constructor stub
	}

	public int getCompanyID() {
		return companyID;
	}

	public CompanyFacade(int companyID) {
		super();
		this.companyID = companyID;
	}

	@Override
	public boolean login(String email, String password) throws DaoException {
		Company company = companiesDao.companyLogin(email, password);
		if (company != null) {
			companyID = company.getID();
			return true;
		}
		return false;
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

	public Coupon getCompanyCoupon(String title) throws DaoException {
		return couponsDao.get(companyID, title);

	}

	public List<Coupon> getCompanyCoupons(double maxPrice) throws DaoException {
		return couponsDao.getCompanyCoupons(companyID, maxPrice);

	}

	public Company getCompanyDetails() throws DaoException {
		Company company = companiesDao.get(companyID);
		company.setCoupons(getCompanyCoupons());
		return company;

	}

}
