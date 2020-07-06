package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import beans.Company;
import connectionPool.ConnectionPool;
import dao.interfaces.ICompaniesDao;
import exceptions.DaoException;
import utils.StringHelper;

public class CompaniesDao implements ICompaniesDao<Company> {
	private Connection connection;

	public CompaniesDao() throws DaoException {
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
	public void add(Company addObject) throws DaoException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Company deleteObject) throws DaoException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Company updateObject) throws DaoException {
		// TODO Auto-generated method stub

	}

	@Override
	public Company get(int id) throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Company> getAll() throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCompanyExist(String email, String password) throws DaoException {
		// TODO Auto-generated method stub
		return false;
	}

}
