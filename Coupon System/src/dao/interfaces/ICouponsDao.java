package dao.interfaces;

import java.sql.Date;
import java.util.List;

import beans.Category;
import beans.Coupon;
import exceptions.DaoException;

public interface ICouponsDao<T> extends IDaoCrud<T> {
	/**
	 * purchase coupon
	 * 
	 * @param customerID
	 * @param couponID
	 * @throws DaoException
	 */
	void addCouponPurchase(int customerID, int couponID) throws DaoException;

	/**
	 * delete purchases coustomer's coupons
	 * 
	 * @param customerID
	 * @param couponID
	 * @throws DaoException
	 */
	// delete one coupon by couponID and customerID
	void deleteCouponPurchase(int customerID, int couponID) throws DaoException;

	/**
	 * delete purchase coupon by couponID
	 * 
	 * @param couponID
	 * @throws DaoException
	 */
	// delete all coupons purchase by couponID
	void deleteCouponsPurchase(int couponID) throws DaoException;

	/**
	 * get all customer's coupons
	 * 
	 * @param customerID
	 * @return
	 * @throws DaoException
	 */
	List<Coupon> getCustomerCoupons(int customerID) throws DaoException;

	/**
	 * get all customer's coupons by category
	 * 
	 * @param customerID
	 * @param category
	 * @return
	 * @throws DaoException
	 */
	List<Coupon> getCustomerCoupons(int customerID, Category category) throws DaoException;

	/**
	 * get all customer's coupons by maximum price
	 * 
	 * @param customerID
	 * @param maxPrice
	 * @return
	 * @throws DaoException
	 */
	List<Coupon> getCustomerCoupons(int customerID, double maxPrice) throws DaoException;

	/**
	 * get all company's coupons
	 * 
	 * @param companyID
	 * @return
	 * @throws DaoException
	 */
	List<Coupon> getCompanyCoupons(int companyID) throws DaoException;

	/**
	 * get all company's coupons by category
	 * 
	 * @param companyID
	 * @param category
	 * @return
	 * @throws DaoException
	 */
	List<Coupon> getCompanyCoupons(int companyID, Category category) throws DaoException;

	/**
	 * get all company's coupons by maximum price
	 * 
	 * @param companyID
	 * @param maxPrice
	 * @return
	 * @throws DaoException
	 */
	List<Coupon> getCompanyCoupons(int companyID, double maxPrice) throws DaoException;

	/**
	 * not exist other coupon with same title in this company
	 * 
	 * @param companyID
	 * @param title
	 * @return
	 * @throws DaoException
	 */
	boolean isCouponValid(int companyID, String title) throws DaoException;

	/**
	 * coupon's amount > 0
	 * 
	 * @param couponID
	 * @return
	 * @throws DaoException
	 */
	boolean isPurchaseCouponAmountValid(int couponID) throws DaoException;

	/**
	 * coupon was not bought by current customer
	 * 
	 * @param couponID
	 * @param customerID
	 * @return
	 * @throws DaoException
	 */
	boolean isPurchaseCouponCustomerValid(int couponID, int customerID) throws DaoException;

	List<T> get(Date date) throws DaoException;

	T get(int companyID, String title) throws DaoException;

}
