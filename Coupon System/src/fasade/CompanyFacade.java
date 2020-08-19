package fasade;

import java.util.List;

import beans.Category;
import beans.Company;
import beans.Coupon;
import exceptions.DaoException;

public class CompanyFacade extends ClientFasade {

	private int companyID;

	/**
	 * ctor
	 */
	public CompanyFacade() {

	}

	/**
	 * ctor
	 * 
	 * @param companyID
	 */
	public CompanyFacade(int companyID) {
		super();
		this.companyID = companyID;
	}

	/**
	 * get company id
	 */
	public int getCompanyID() {
		return companyID;
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

	/**
	 * add Coupon
	 * 
	 * @param coupon
	 * @throws DaoException
	 */
	public void addCoupon(Coupon coupon) throws DaoException {
		couponsDao.add(coupon);
	}

	/**
	 * update Coupon
	 * 
	 * @param coupon
	 * @throws DaoException
	 */
	public void updateCoupon(Coupon coupon) throws DaoException {
		couponsDao.update(coupon);
	}

	/**
	 * delete Coupon
	 * 
	 * @param couponID
	 * @throws DaoException
	 */
	public void deleteCoupon(int couponID) throws DaoException {
		couponsDao.delete(couponID);
	}

	/**
	 * get Company Coupons
	 * 
	 * @return
	 * @throws DaoException
	 */
	public List<Coupon> getCompanyCoupons() throws DaoException {
		return couponsDao.getCompanyCoupons(companyID);

	}

	/**
	 * get company coupons by category
	 * 
	 * @param category
	 * @return
	 * @throws DaoException
	 */

	public List<Coupon> getCompanyCoupons(Category category) throws DaoException {
		return couponsDao.getCompanyCoupons(companyID, category);

	}

	/**
	 * get company coupon by title
	 * 
	 * @param title
	 * @return
	 * @throws DaoException
	 */
	public Coupon getCompanyCoupon(String title) throws DaoException {
		return couponsDao.get(companyID, title);

	}

	/**
	 * get company coupons by max price
	 * 
	 * @param maxPrice
	 * @return list of coupons
	 * @throws DaoException
	 */
	public List<Coupon> getCompanyCoupons(double maxPrice) throws DaoException {
		return couponsDao.getCompanyCoupons(companyID, maxPrice);

	}

	/**
	 * get company details
	 * 
	 * @return company
	 * @throws DaoException
	 */
	public Company getCompanyDetails() throws DaoException {
		Company company = companiesDao.get(companyID);
		company.setCoupons(getCompanyCoupons());
		return company;

	}

}
