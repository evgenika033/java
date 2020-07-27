package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Company;
import connectionPool.ConnectionPool;
import dao.interfaces.ICompaniesDao;
import exceptions.DaoException;
import utils.StringHelper;

public class CompaniesDao implements ICompaniesDao<Company> {
	private Connection connection;

	private void getConnection() throws DaoException {
		try {
			connection = ConnectionPool.getInstance().getConnection();
		} catch (SQLException e) {
			throw new DaoException(StringHelper.DAO_EXEPTION_CONNECTION, e);
		}
	}

	private void returnConnection() throws DaoException {
		try {
			ConnectionPool.getInstance().returnConnection(connection);
			connection = null;
		} catch (SQLException e) {
			throw new DaoException(StringHelper.DAO_EXEPTION_CONNECTION, e);
		}
	}

	@Override
	public void add(Company addObject) throws DaoException {
		String sql = StringHelper.SQL_ADD;// "insert into _TABLE_NAME_ values(_ADD_PARAMETERS_)";
		// replace place_holders
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COMPANIES)
				.replaceAll(StringHelper.PARAMETERS_ADD_PLACE_HOLDER, StringHelper.ADD_PARAMETERS_COMPANIES);
		if (!isCompanyExistByNameOrEmail(addObject.getName(), addObject.getEmail())) {
			System.out.println(sql);
			getConnection();
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
				preparedStatement.setString(1, addObject.getName());
				preparedStatement.setString(2, addObject.getEmail());
				preparedStatement.setString(3, addObject.getPassword());
				preparedStatement.executeUpdate();
				System.out.println("inserted");
				returnConnection();
			} catch (SQLException e) {
				returnConnection();
				throw new DaoException(StringHelper.EXCEPTION_INSERT, e);
			}
		}

	}

	@Override
	public void update(Company updateObject) throws DaoException {
		String sql = StringHelper.SQL_UPDATE;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COMPANIES)
				.replaceAll(StringHelper.PARAMETERS_UPDATE_PLACE_HOLDER, StringHelper.UPDATE_PARAMETERS_COMPANIES);
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setString(1, updateObject.getName());
			preparedStatement.setString(2, updateObject.getEmail());
			preparedStatement.setString(3, updateObject.getPassword());
			preparedStatement.setInt(4, updateObject.getID());
			int result = preparedStatement.executeUpdate();
			returnConnection();
			// TODO need to remove
			if (result > 0) {
				System.out.println("updated true");
			} else {
				System.out.println("updated false");
			}
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_UPDATE, e);
		}

	}

	@Override
	public Company get(int id) throws DaoException {
		String sql = StringHelper.SQL_GET;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COMPANIES)
				.replaceAll(StringHelper.PARAMETERS_GET_PLACE_HOLDER, StringHelper.GET_PARAMETERS_COMPANIES);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int ID = resultSet.getInt("companyId");
				String name = resultSet.getString("companyName");
				String email = resultSet.getString("companyEmail");
				String password = resultSet.getString("companyPassword");
				returnConnection();
				return new Company(ID, name, email, password);
			}
			returnConnection();
			return null;
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}

	}

	@Override
	public List<Company> getAll() throws DaoException {
		List<Company> list = new ArrayList<>();
		String sql = StringHelper.sql_GET_ALL;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COMPANIES);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			// preparedStatement.setString(1, table);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int ID = resultSet.getInt("companyId");
				String name = resultSet.getString("companyName");
				String email = resultSet.getString("companyEmail");
				String password = resultSet.getString("companyPassword");
				list.add(new Company(ID, name, email, password));
			}
			returnConnection();
			return list;
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET_ALL, e);
		}

	}

	@Override
	public boolean isCompanyExist(String email, String password) throws DaoException {
		String sql = StringHelper.SQL_QUERY_COMPANY_COUNT;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COMPANIES);
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int counter = resultSet.getInt(1);
				returnConnection();
				return counter > 0;
			}
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET_ALL, e);
		}

		return false;
	}

	@Override
	public void delete(int objectID) throws DaoException {
		String sql = StringHelper.SQL_DELETE;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COMPANIES)
				.replaceAll(StringHelper.PARAMETERS_DELETE_PLACE_HOLDER, StringHelper.DELETE_PARAMETERS_COMPANIES);
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, objectID);
			int result = preparedStatement.executeUpdate();
			returnConnection();
			System.out.println("deleted: " + result);
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_DELETE, e);
		}
	}

	@Override
	public boolean isCompanyExistByNameOrEmail(String companyName, String companyEmail) throws DaoException {
		String sql = StringHelper.SQL_QUERY_GET_COMPANY_BY_NAME_OR_EMAIL;
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setString(1, companyName);
			preparedStatement.setString(2, companyEmail);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				if (resultSet.getInt(1) > 0) {
					returnConnection();
					throw new DaoException(StringHelper.EXCEPTION_COMPANY_ADD_ALREADY_EXIST);
				}
			}
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}
		returnConnection();
		return false;
	}

}
