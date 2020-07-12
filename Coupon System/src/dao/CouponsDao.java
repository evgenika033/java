package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import beans.Coupon;
import connectionPool.ConnectionPool;
import dao.interfaces.ICouponsDao;
import exceptions.DaoException;
import utils.StringHelper;

public class CouponsDao implements ICouponsDao<Coupon> {
	private Connection connection;

	public CouponsDao() throws DaoException {
		init();
	}

	private void init() throws DaoException {
		try {
			connection = ConnectionPool.getInstance().getConnection();
		} catch (SQLException e) {
			throw new DaoException(StringHelper.DAO_EXEPTION_CONNECTION, e);
		}
	}

	@Override
	public void add(Coupon addObject) throws DaoException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Coupon updateObject) throws DaoException {
		// TODO Auto-generated method stub

	}

	@Override
	public Coupon get(int id) throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Coupon> getAll() throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCouponPurchase(int customerID, int couponID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteCouponPurchase(int customerID, int couponID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(int objectID) throws DaoException {
		// TODO Auto-generated method stub

	}

}
