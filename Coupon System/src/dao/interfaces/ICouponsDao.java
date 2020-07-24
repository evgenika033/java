package dao.interfaces;

import java.util.List;

import beans.Category;
import beans.Coupon;
import exceptions.DaoException;

public interface ICouponsDao<T> extends IDaoCrud<T> {
	void addCouponPurchase(int customerID, int couponID) throws DaoException;

	void deleteCouponPurchase(int customerID, int couponID) throws DaoException;

	List<Coupon> getCustomerCoupons(int customerID) throws DaoException;

	List<Coupon> getCustomerCoupons(int customerID, Category category) throws DaoException;

	List<Coupon> getCustomerCoupons(int customerID, double maxPrice) throws DaoException;

}
