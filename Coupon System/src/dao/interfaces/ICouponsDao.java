package dao.interfaces;

public interface ICouponsDao<T> extends IDaoCrud<T> {
	void addCouponPurchase(int customerID, int couponID);

	void deleteCouponPurchase(int customerID, int couponID);

}
