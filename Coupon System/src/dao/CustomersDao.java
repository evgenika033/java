package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import beans.Customer;
import connectionPool.ConnectionPool;
import dao.interfaces.ICustomersDao;
import exceptions.DaoException;
import utils.StringHelper;

public class CustomersDao implements ICustomersDao<Customer> {

	private Connection connection;

	public CustomersDao() throws DaoException {
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
	public void add(Customer addObject) throws DaoException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Customer updateObject) throws DaoException {
		// TODO Auto-generated method stub

	}

	@Override
	public Customer get(int id) throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Customer> getAll() throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCustomerExist(String email, String password) throws DaoException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void delete(int objectID) throws DaoException {
		// TODO Auto-generated method stub

	}

}
